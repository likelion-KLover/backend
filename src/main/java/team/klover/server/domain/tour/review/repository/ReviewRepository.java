package team.klover.server.domain.tour.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.review.entity.Review;

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
}
