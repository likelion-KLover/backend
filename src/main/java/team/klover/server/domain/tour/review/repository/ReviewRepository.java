package team.klover.server.domain.tour.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.tour.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 해당 관광지 게시글에 작성된 모든 리뷰 조회
    Page<Review> findByTourPostContentId(String contentId, Pageable pageable);
}
