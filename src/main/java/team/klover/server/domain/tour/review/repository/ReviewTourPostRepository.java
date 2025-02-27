package team.klover.server.domain.tour.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.review.entity.ReviewTourPost;

@Repository
public interface ReviewTourPostRepository extends JpaRepository<ReviewTourPost, Long> {
    void deleteByReviewId(Long reviewId);
}
