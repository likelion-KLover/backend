package team.klover.server.domain.tour.tourPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

import java.util.List;

@Repository
public interface TourPostRepository extends JpaRepository<TourPost, Long> {
    // 저장된 Apis 데이터에서 선별
    void deleteByCat3NotIn(List<String> cat3List);

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    @Query("SELECT p.contentId FROM TourPost p")
    List<String> findAllContentIds();

    // 관광지별 공통 ID로 해당 관광지 데이터 가져오기
    TourPost findByContentId(String commonPlaceId);

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    Page<TourPost> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable);

    // 사용자 언어 & 관광지명/지역명 검색
    @Query("SELECT t FROM TourPost t WHERE t.language = :language AND (t.title LIKE %:keyword% OR t.addr1 LIKE %:keyword%)")
    Page<TourPost> searchByLanguageAndKeyword(@Param("language") String language, @Param("keyword") String keyword, Pageable pageable);

    // 사용자가 저장한 관광지 조회
    @Query("SELECT p FROM TourPost p JOIN p.savedMembers sm WHERE sm.Member.id = :testMemberId")
    Page<TourPost> findSavedTourPostsByTestMemberId(@Param("testMemberId") Long testMemberId, Pageable pageable);

    // 모든 관광지 데이터 가져오기
    @Query("SELECT t FROM TourPost t")
    List<TourPost> findAllPosts();

    // X, Y 기준 그룹핑 개수가 4개가 아닌 경우 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM TourPost t WHERE t.mapX = :mapX AND t.mapY = :mapY")
    void deleteByMapCoordinates(@Param("mapX") String mapX, @Param("mapY") String mapY);
}
