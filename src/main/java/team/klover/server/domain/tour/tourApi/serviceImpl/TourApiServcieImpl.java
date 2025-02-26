package team.klover.server.domain.tour.tourApi.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.tour.tourApi.service.TourApiService;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiServcieImpl implements TourApiService {
    private final TourPostRepository tourPostRepository;

    @PersistenceContext
    private EntityManager entityManager;  // EntityManager 주입

    // Apis의 데이터를 Post에 저장
    @Override
    @Transactional
    public void saveApis(String jsonResponse, String language) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<TourPost> tourList = new ArrayList<>();

            for (JsonNode item : items) {
                TourPost entity = TourPost.builder()
                        .contentId(item.path("contentid").asLong())
                        .title(item.path("title").asText())
                        .areaCode(item.path("areacode").asText())
                        .sigungucode(item.path("sigungucode").asText())
                        .addr1(item.path("addr1").asText())
                        .firstImage(item.path("firstimage").asText())
                        .contentTypeId(item.path("contenttypeid").asText())
                        .mapX(item.path("mapx").asDouble())
                        .mapY(item.path("mapy").asDouble())
                        .cat1(item.path("cat1").asText())
                        .cat2(item.path("cat2").asText())
                        .cat3(item.path("cat3").asText())
                        .cpyrhtDivCd(item.path("cpyrhtDivCd").asText())
                        .language(language)
                        .build();
                tourList.add(entity);
            }
            tourPostRepository.saveAll(tourList);
            log.info(tourList.toString());
        } catch (Exception e) {
            log.error("saveApis 처리 중 오류 발생", e);
            throw new RuntimeException("saveApis 처리 중 오류 발생", e);
        }
    }

    // 저장된 Apis 데이터에서 선별
    @Override
    @Transactional
    public void sortApis() {
        List<String> cat3List = Arrays.asList(
                "A01010100", "A01010500", "A01010600", "A02010100", "A02010200", "A02010300",
                "A02010600", "A02010800", "A02011000", "A02020200", "A02020300", "A02020400",
                "A02020600", "A02030100", "A02030200", "A02030300", "A02030400", "A02030600",
                "A02040600", "A02050200", "A02050600", "A02060100", "A02060200", "A02060700",
                "A02060800", "A02061100", "A02061300", "A02070100", "A02070200", "A02080100",
                "A02080200", "A02080300", "A02080500", "A02080600", "A02080800", "A02081000",
                "A02081200", "C01120001", "C01130001", "C01140001", "C01150001", "C01160001",
                "C01170001", "A03010200", "A03020600", "A03021300", "A03021400", "A03021700",
                "A03022000", "A03022100", "A03022200", "A03022300", "A03022700", "A03030100",
                "A03030400", "A03040300", "A03040400", "B02010100", "B02011100", "B02011200",
                "B02011600", "A04010100", "A04010400", "A04010700", "A04010900", "A05020100",
                "A05020700", "A05020900"
        );
        tourPostRepository.deleteByCat3NotIn(cat3List);
    }

    // 관광지의 X, Y 좌표가 모든 언어에 공통으로 있는 관광지 선별
    @Override
    @Transactional
    public void getCommonPlace() {
        List<TourPost> allPosts = tourPostRepository.findAllPosts(); // 모든 데이터 가져오기

        // mapX, mapY 기준으로 그룹핑하여 개수 확인
        Map<String, List<TourPost>> groupedByCoordinates = allPosts.stream()
                .collect(Collectors.groupingBy(t -> t.getMapX() + "," + t.getMapY()));
        int commonPlaceId = 1; // commonPlaceId 초기값 설정

        for (Map.Entry<String, List<TourPost>> entry : groupedByCoordinates.entrySet()) {
            List<TourPost> tourPosts = entry.getValue();
            if (tourPosts.size() == 4) { // 4개인 경우 commonPlaceId 부여
                for (TourPost tourPost : tourPosts) {
                    tourPost.setCommonPlaceId(String.valueOf(commonPlaceId));
                }
                commonPlaceId++; // 다음 그룹을 위해 증가
            } else { // 4개가 아닌 경우 삭제
                String[] coordinates = entry.getKey().split(",");
                String mapX = coordinates[0];
                String mapY = coordinates[1];
                tourPostRepository.deleteByMapCoordinates(mapX, mapY);
            }
        }
        tourPostRepository.saveAll(allPosts);
    }

    // contentId 기준으로 오름차순 정렬
    @Override
    @Transactional
    public void sortAsc() {
        tourPostRepository.backupSortedData();  // 정렬된 데이터를 임시 테이블에 저장
        tourPostRepository.deleteAllPostsAndReviews();    // 기존 데이터 삭제
        tourPostRepository.restoreSortedData(); // 정렬된 데이터를 원래 테이블에 삽입
        tourPostRepository.dropBackupTable();     // 임시 테이블 삭제 (선택 사항)
    }

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    @Override
    @Transactional(readOnly = true)
    public List<Long> getAllContentIds() {
        return tourPostRepository.findAllContentIds();
    }

    // 관광지별 개요 데이터 추가 및 저장
    @Override
    @Transactional
    public void addOverview(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode item = root.path("response").path("body").path("items").path("item");

            // JSON 응답에서 contentId, overview, homepage 추출
            Long contentId = item.path("contentid").asLong();
            String overview = item.path("overview").asText();
            String homepage = item.path("homepage").asText();

            // 해당 contentId를 가진 Post 엔터티 조회
            TourPost tourPost = tourPostRepository.findByContentId(contentId);
            if (tourPost != null) {
                // overview, homepage 필드 업데이트
                tourPost.setOverview(overview);
                tourPost.setHomepage(homepage);
                // 변경 사항 저장
                tourPostRepository.save(tourPost);
                log.info("contentId = {} 개요를 업데이트했습니다. 개요: {}", contentId, overview);
            } else {
                log.warn("contentId = {} 에 해당하는 게시물을 찾을 수 없습니다.", contentId);
            }
        } catch (Exception e) {
            log.error("addOverview 처리 중 오류 발생", e);
            throw new RuntimeException("addOverview 처리 중 오류 발생", e);
        }
    }
}
