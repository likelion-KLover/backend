package team.klover.server.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.klover.server.domain.notification.dto.FCMTokenParam;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static team.klover.server.global.redis.RedisConst.*;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

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
}
