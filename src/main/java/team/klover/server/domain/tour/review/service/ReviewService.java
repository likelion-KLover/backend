package team.klover.server.domain.tour.review.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.dto.res.ReviewDto;

public interface ReviewService {
    // 해당 관광지 게시글에 작성된 리뷰 조회
    Page<ReviewDto> findByCommonPlaceId(String commonPlaceId, Pageable pageable);

    // 해당 관광지 게시글에 리뷰 생성
    void addReview(Long memberId, Long contentId, @Valid ReviewForm reviewForm);

    // 해당 리뷰 수정
    void updateReview(Long memberId, Long id, @Valid ReviewForm reviewForm);

    // 해당 리뷰 삭제
    void deleteReview(Long memberId, Long id);
}
