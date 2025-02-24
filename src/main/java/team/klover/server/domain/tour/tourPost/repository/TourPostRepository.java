package team.klover.server.domain.tour.tourPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourPostRepository extends JpaRepository<TourPost, Long> {
    // 저장된 Apis 데이터에서 선별
    void deleteByCat3NotIn(List<String> cat3List);

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    @Query("SELECT p.contentId FROM TourPost p")
    List<String> findAllContentIds();

    // 관광지별 고유 ID로 해당 관광지 데이터 가져오기
    TourPost findByContentId(String contentId);

    // 사용자 언어 & 관광지 상세 정보 조회
    TourPost findByLanguageAndContentId(String contentId, String language);

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    Page<TourPost> findByLanguageAndAreaCode(String language, String areaCode, Pageable pageable);

    // 사용자 언어 & 관광지명/지역명 검색
    @Query("SELECT t FROM TourPost t WHERE t.language = :language AND (t.title LIKE %:keyword% OR t.addr1 LIKE %:keyword%)")
    Page<TourPost> searchByLanguageAndKeyword(@Param("language") String language, @Param("keyword") String keyword, Pageable pageable);

    // 사용자가 저장한 관광지 조회
    @Query("SELECT p FROM TourPost p JOIN p.savedMembers sm WHERE sm.testMember.id = :testMemberId")
    Page<TourPost> findSavedTourPostsByTestMemberId(@Param("testMemberId") Long testMemberId, Pageable pageable);
}
