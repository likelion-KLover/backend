package team.klover.server.domain.tour.review.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.test.entity.TestMember;
import team.klover.server.domain.member.test.repository.TestMemberRepository;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.dto.res.ReviewDto;
import team.klover.server.domain.tour.review.entity.Review;
import team.klover.server.domain.tour.review.entity.ReviewPage;
import team.klover.server.domain.tour.review.repository.ReviewRepository;
import team.klover.server.domain.tour.review.service.ReviewService;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final TourPostRepository tourPostRepository;
    private final TestMemberRepository testMemberRepository;

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
    public void addReview(String contentId, @Valid ReviewForm reviewForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        TourPost tourPost = tourPostRepository.findByContentId(contentId);

        // 평점은(1,2,3,4,5)만 가능
        if (reviewForm.getRating() > 5 || reviewForm.getRating() < 0) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
        Review review = Review.builder()
                .testMember(testMember)
                .tourPost(tourPost)
                .content(reviewForm.getContent())
                .rating(reviewForm.getRating())
                .commonPlaceId(tourPost.getCommonPlaceId())
                .build();
        reviewRepository.save(review);
    }

    // 해당 리뷰 수정
    @Override
    @Transactional
    public void updateReview(Long id, @Valid ReviewForm reviewForm){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        Long currentUserId = testMember.getId();
        if (!review.getTestMember().getId().equals(currentUserId)) {
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

    // 해당 리뷰 삭제
    @Override
    @Transactional
    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        Long currentUserId = testMember.getId();
        if (!review.getTestMember().getId().equals(currentUserId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
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
                .testMemberId(review.getTestMember().getId())
                .testMemberNickname(review.getTestMember().getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .build();
    }
}
