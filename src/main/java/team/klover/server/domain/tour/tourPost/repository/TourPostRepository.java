package team.klover.server.domain.tour.tourPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

import java.util.List;
import java.util.Optional;

public interface TourPostRepository extends JpaRepository<TourPost, Long> {
    // 저장된 Apis 데이터에서 선별
    void deleteByCat3NotIn(List<String> cat3List);

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    @Query("SELECT p.contentId FROM TourPost p")
    List<String> findAllContentIds();

    // 관광지별 고유 ID로 해당 관광지 데이터 가져오기
    Optional<TourPost> findByContentId(String contentId);

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    Page<TourPost> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable);
}
