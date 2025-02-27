package team.klover.server.domain.tour.review.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.dto.res.ReviewDto;
import team.klover.server.domain.tour.review.entity.Review;
import team.klover.server.domain.tour.review.entity.ReviewPage;
import team.klover.server.domain.tour.review.entity.ReviewTourPost;
import team.klover.server.domain.tour.review.repository.ReviewRepository;
import team.klover.server.domain.tour.review.repository.ReviewTourPostRepository;
import team.klover.server.domain.tour.review.service.ReviewService;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final TourPostRepository tourPostRepository;
    private final MemberV1Repository memberV1Repository;
    private final ReviewTourPostRepository reviewTourPostRepository;

    // 해당 관광지 게시글에 작성된 리뷰 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByCommonPlaceId(String commonPlaceId, Pageable pageable) {
        checkPageSize(pageable.getPageSize());
        Page<Review> reviews = reviewRepository.findByCommonPlaceId(commonPlaceId, pageable);
        return reviews.map(this::convertToReviewDto);
    }

    // 해당 관광지 게시글에 리뷰 생성
    @Override
    @Transactional
    public void addReview(Long currentMemberId, Long commonPlaceId, @Valid ReviewForm reviewForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(currentMemberId).orElseThrow(() ->
                new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        List<TourPost> tourPosts = tourPostRepository.findByCommonPlaceId(commonPlaceId);
        if (tourPosts.isEmpty()) {
            throw new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY);
        }
        // 첫 번째 TourPost에서 사용자가 이미 리뷰를 남겼는지 확인
        boolean alreadyReviewed = tourPosts.get(0).getReviewTourPosts().stream()
                .anyMatch(rt -> rt.getReview().getMember().getId().equals(currentMemberId));
        if (alreadyReviewed) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }

        // 평점은(1,2,3,4,5)만 가능
        if (reviewForm.getRating() > 5 || reviewForm.getRating() < 0) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
        Review review = Review.builder()
                .member(member)
                .content(reviewForm.getContent())
                .rating(reviewForm.getRating())
                .build();
        reviewRepository.save(review);

        // 모든 TourPost에 대해 ReviewTourPost 저장
        for (TourPost tourPost : tourPosts) {
            ReviewTourPost reviewTourPost = ReviewTourPost.builder()
                    .review(review)
                    .tourPost(tourPost)
                    .build();
            reviewTourPostRepository.save(reviewTourPost);
        }
    }

    // 본인 리뷰 수정
    @Override
    @Transactional
    public void updateReview(Long currentMemberId, Long reviewId, @Valid ReviewForm reviewForm){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        if (!review.getMember().getId().equals(currentMemberId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        // 평점은(1,2,3,4,5)만 가능
        if (reviewForm.getRating() > 5 || reviewForm.getRating() < 0) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
        review.setContent(reviewForm.getContent());
        review.setRating(reviewForm.getRating());
        reviewRepository.save(review);
    }

    // 본인 리뷰 삭제
    @Override
    @Transactional
    public void deleteReview(Long currentMemberId, Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        if (!review.getMember().getId().equals(currentMemberId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        reviewTourPostRepository.deleteByReviewId(reviewId);
        reviewRepository.delete(review);
    }

    // 요청 페이지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = ReviewPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // Review를 ReviewDto로 변환
    private ReviewDto convertToReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .memberId(review.getMember().getId())
                .nickname(review.getMember().getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .build();
    }
}
