package team.klover.server.global.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import team.klover.server.domain.notification.dto.FCMTokenParam;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostCountMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostDeletionMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.CommPostModificationMessage;
import team.klover.server.global.elasticsearch.commpost.springevent.message.NicknameModificationMessage;
import team.klover.server.global.elasticsearch.tourpost.springevent.message.TourPostCountMessage;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static team.klover.server.global.redis.RedisConst.*;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    public void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(email, refreshToken, 7, TimeUnit.DAYS); // RefreshToken 만료 시간과 동일하게 설정
    }

    public String getRefreshToken(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }

    public void saveOrUpdateFCMToken(FCMTokenParam param) {
        String key = TOKEN_KEY_PREFIX + param.getMemberId();
        redisTemplate.opsForValue().set(key, param.getFcmToken(), TOKEN_TIMEOUT, TimeUnit.DAYS); // refreshToken과 맞춤
    }

    public String getFCMToken(Long memberId) {
        String key = TOKEN_KEY_PREFIX + memberId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void saveFCMMessage(Long memberId, String serializedNotification) {
        String key = MESSAGE_LIST_PREFIX + memberId;

        redisTemplate.opsForList().leftPush(key, serializedNotification);

        // 최신 50개만 유지 (리스트 길이가 50을 초과하면 오래된 데이터 삭제)
        redisTemplate.opsForList().trim(key, 0, MAX_MESSAGE_COUNT);
    }

    public List<String> getFCMMessages(Long memberId) {
        String key = MESSAGE_LIST_PREFIX + memberId;

        List<Object> objectList = redisTemplate.opsForList().range(key, 0, -1);
        if (objectList == null) {
            return Collections.emptyList();
        }

        return objectList.stream()
                .map(obj -> obj != null ? obj.toString() : "")
                .collect(Collectors.toList());
    }

    public void saveCommPostCountMessage(CommPostCountMessage message) {
        String key = ES_COMMPOST_PREFIX+message.getId()+ES_COMMPOST_COUNT_POSTFIX;
        System.out.println("저장한 키:"+key);
        String lockKey = key+":lock";
        RLock lock = redissonClient.getLock(lockKey); // Redisson의 분산 락

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 락을 획득한 상태에서 데이터 조회 및 삭제
            redisTemplate.opsForValue().set(key, message.getLanguage());
            redisTemplate.expire(key, Duration.ofMinutes(3));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public Map<Long, Object> getAllCommPostCount(){
        String keyPattern = ES_COMMPOST_PREFIX + "*" + ES_COMMPOST_COUNT_POSTFIX;

        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(keyPattern)
                .build());

        List<String> keys = cursor.stream().toList();
        Map<Long, Object> result = new HashMap<>();
        keys.forEach(
                key -> {
                    String idStr = key.split(":")[1];
                    System.out.println("카운트 이벤트 키:"+key);
                    result.put(Long.parseLong(idStr),getCommPostCountLanguage(key));
                }
        );

        return result;
    }
    private Object getCommPostCountLanguage(String key){
        String lockKey = key+":lock";

        RLock lock = redissonClient.getLock(lockKey); // Redisson의 분산 락

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 락을 획득한 상태에서 데이터 조회 및 삭제
            Object result = redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public void saveCommPostModificationMessage(CommPostModificationMessage message){
        String key = ES_COMMPOST_PREFIX + message.getId() + ES_COMMPOST_UPDATE_POSTFIX;
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 🔹 최대 10초 동안 락을 기다림, 락을 획득하면 5초 후 자동 해제
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 🔹 데이터 저장
            Map<String, Object> value = new HashMap<>();
            value.put("mapx", message.getMapx());
            value.put("mapy", message.getMapy());
            value.put("content", message.getContent());
            value.put("language", message.getLanguage());
            value.put("member_id",message.getMember_id());
            value.put("create_date",message.getCreate_date());
            value.put("modify_date",message.getModify_date());
            value.put("image_urls",message.getImage_urls());

            redisTemplate.opsForHash().putAll(key, value);
            redisTemplate.expire(key, Duration.ofMinutes(3)); // 3분 만료

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }

    }

    public Map<Long, Map<Object, Object>> getAllCommPostModification(){
        String keyPattern = ES_COMMPOST_PREFIX + "*" + ES_COMMPOST_UPDATE_POSTFIX;

        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(keyPattern)
                .build());

        List<String> keys = cursor.stream().toList();
        Map<Long, Map<Object, Object>> result = new HashMap<>();
        keys.forEach(
                key -> {
                    String idStr = key.split(":")[1];
                    System.out.println("count key:"+key);
                    result.put(Long.parseLong(idStr),getCommPostModification(key));
                }
        );

        return result;
    }

    private Map<Object, Object> getCommPostModification(String key){
        String lockKey = key + ":lock";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }


            // 락을 획득한 상태에서 데이터 조회 및 삭제
            Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
            redisTemplate.delete(key);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public void saveCommPostDeletionMessage(CommPostDeletionMessage message){
        String key = ES_COMMPOST_PREFIX+message.getId()+ES_COMMPOST_DELETE_POSTFIX;
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 🔹 최대 10초 동안 락을 기다림, 락을 획득하면 5초 후 자동 해제
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 🔹 데이터 저장
            redisTemplate.opsForValue().set(key, message.getLanguage());
            redisTemplate.expire(key,Duration.ofMinutes(3));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public Map<Long, Object> getALLCommPostDeletion(){
        String keyPattern = ES_COMMPOST_PREFIX + "*" + ES_COMMPOST_DELETE_POSTFIX;

        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(keyPattern)
                .build());

        List<String> keys = cursor.stream().toList();
        Map<Long, Object> result = new HashMap<>();
        keys.forEach(
                key -> {
                    String idStr = key.split(":")[1];
                    result.put(Long.parseLong(idStr),getCommPostDeletion(key));
                }
        );

        return result;
    }

    private Object getCommPostDeletion(String key){
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey); // Redisson의 분산 락

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 락을 획득한 상태에서 데이터 조회 및 삭제
            Object result = redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public void saveNicknameModificationMessage(NicknameModificationMessage message){
        String key = ES_MEMBER_PREFIX+message.getMember_id();
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 🔹 최대 10초 동안 락을 기다림, 락을 획득하면 5초 후 자동 해제
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 🔹 데이터 저장
            redisTemplate.opsForValue().set(key, message.getNickname());
            redisTemplate.expire(key,Duration.ofMinutes(3));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public Map<Long, Object> getAllNickname(){
        String keyPattern = ES_MEMBER_PREFIX+"*";

        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(keyPattern)
                .build());

        List<String> keys = cursor.stream().toList();
        Map<Long, Object> result = new HashMap<>();
        keys.forEach(
                key -> {
                    String idStr = key.split(":")[1];
                    result.put(Long.parseLong(idStr),getNickname(key));
                }
        );

        return result;
    }

    private Object getNickname(String key){
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey); // Redisson의 분산 락

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 락을 획득한 상태에서 데이터 조회 및 삭제
            Object result = redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

    public void saveTourPostCountMessage(TourPostCountMessage message){
        String key = ES_TOURPOST_PREFIX+message.getContent_id()+ES_TOURPOST_COUNT_PREFIX;
        String lockKey = key + ":lock";

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 🔹 최대 10초 동안 락을 기다림, 락을 획득하면 5초 후 자동 해제
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 🔹 데이터 저장
            Map<String, Object> values = new HashMap<>();
            values.put("common_place_id",message.getCommon_place_id());
            values.put("language",message.getLanguage());
            redisTemplate.opsForHash().putAll(key, values);
            redisTemplate.expire(key,Duration.ofMinutes(3));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }
    public Map<Long, Map<Object,Object>> getAllTourPostCount(){
        String keyPattern = ES_TOURPOST_PREFIX+"*"+ES_TOURPOST_COUNT_PREFIX;

        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(keyPattern)
                .build());

        List<String> keys = cursor.stream().toList();
        Map<Long, Map<Object, Object>> result = new HashMap<>();
        keys.forEach(
                key -> {
                    String idStr = key.split(":")[1];
                    result.put(Long.parseLong(idStr),getTourPostLanguage(key));
                }
        );

        return result;
    }

    private Map<Object, Object> getTourPostLanguage(String key){
        String lockKey =  key + ":lock";

        RLock lock = redissonClient.getLock(lockKey); // Redisson의 분산 락

        try {
            boolean lockAcquired = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new RuntimeException("Failed to acquire lock");
            }

            // 락을 획득한 상태에서 데이터 조회 및 삭제
            Map<Object,Object> result =  redisTemplate.opsForHash().entries(key);
            redisTemplate.delete(key);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 락 해제
            }
        }
    }

}
