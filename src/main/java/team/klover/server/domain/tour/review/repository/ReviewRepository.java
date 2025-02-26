package team.klover.server.domain.tour.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 해당 관광지에 작성된 모든 리뷰 조회
    Page<Review> findByCommonPlaceId(String commonPlaceId, Pageable pageable);

    // 해당 관광지의 리뷰 평점 구하기
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.tourPost.contentId = :tourPostContentId")
    Double findAverageRatingByTourPostId(@Param("tourPostContentId") Long tourPostContentId);
}
