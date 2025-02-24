package team.klover.server.domain.tour.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.dto.res.ReviewDto;
import team.klover.server.domain.tour.review.entity.ReviewPage;
import team.klover.server.domain.tour.review.service.ReviewService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;

@RestController
@RequestMapping("/api/v1/tour-post/review")
@RequiredArgsConstructor
public class ApiV1ReviewController {
    private final ReviewService reviewService;

    // 해당 관광지 게시글에 작성된 모든 리뷰 조회
    // http://localhost:8090/api/v1/tour-post/review/2542774?page=0&size=10
    @GetMapping("/{contentId}")
    public ApiResponse<ReviewDto> getReview(@ModelAttribute ReviewPage request, @PathVariable("contentId") String contentId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(reviewService.findByContentId(contentId, pageable)));
    }

    // 해당 관광지 게시글에 리뷰 생성
    // http://localhost:8090/api/v1/tour-post/review/2542774
    @PostMapping("/{contentId}")
    public ApiResponse<String> addReview(@PathVariable("contentId") String contentId, @RequestBody @Valid ReviewForm reviewForm) {
        reviewService.addReview(contentId, reviewForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 리뷰 수정
    // http://localhost:8090/api/v1/tour-post/review/1
    @PutMapping("/{id}")
    public ApiResponse<String> updateReview(@PathVariable("id") String id, @RequestBody @Valid ReviewForm reviewForm) {
        reviewService.updateReview(id, reviewForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 리뷰 삭제
    // http://localhost:8090/api/v1/tour-post/review/1
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteReview(@PathVariable("id") String id) {
        reviewService.deleteReview(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
