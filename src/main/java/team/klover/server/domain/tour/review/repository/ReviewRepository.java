package team.klover.server.domain.tour.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.review.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 해당 관광지에 작성된 모든 리뷰 조회 (ReviewTourPost를 통해 연결)
    @EntityGraph(attributePaths = {"reviewTourPosts"})
    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN r.reviewTourPosts rtp " +
            "JOIN rtp.tourPost tp " +
            "WHERE tp.commonPlaceId = :commonPlaceId")
    Page<Review> findByCommonPlaceId(@Param("commonPlaceId") String commonPlaceId, Pageable pageable);

    // 본인이 작성한 리뷰 조회
    Review findByMemberId(Long currentMemberId);

    // 해당 관광지의 리뷰 평점 구하기 (ReviewTourPost 경유)
    @Query("SELECT COALESCE(AVG(r.rating), 0) " +
            "FROM Review r " +
            "JOIN r.reviewTourPosts rtp " +
            "JOIN rtp.tourPost tp " +
            "WHERE tp.contentId = :tourPostContentId")
    Double findAverageRatingByTourPostId(@Param("tourPostContentId") Long tourPostContentId);

    //하나의 공통 장소에 대해 해당 유저가 작성한 리뷰를 가져온다.
    //distinct 를 사용하는 이유는 Review와 ReviewTourPost와의 join의 경우 리뷰는 게시물당 하나라는 제약이 있어 뻥튀기 문제가 없다.
    //하지만 where 절에서 보면 join조차 안 한 TourPost의 필드를 가져다 쓰고 있다.
    //네이티브 SQL 입장에서 생각해본다면 Review와도 join이 일어나는데, 이 때는 ReviewTourPost의 tour post id와 TourPost의 id를 기반으로 join한다.
    //ReviewTourPost에서 리뷰 컬럼은 unique하지만 포스트 컬럼은 unique하지 않다. 리뷰를 여러 개 가질 수 있기 때문이다.
    //따라서 2번째 언급한 곳에서 뻥튀기가 일어나고, 따라서 distinct가 필요하다.
    @Query("""
    select distinct r from Review r
    join ReviewTourPost rtp on r.id = rtp.review.id
    where r.member.id = :memberId and rtp.tourPost.commonPlaceId = :commonPlaceId
    """)
    Optional<Review> findCommonPlaceReviewReviewWrittenByMember(Long memberId, Long commonPlaceId);
}
