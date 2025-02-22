package team.klover.server.domain.tour.tourPost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.tour.tourPost.dto.res.DetailTourPostDto;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;
import team.klover.server.domain.tour.tourPost.entity.TourPostPage;
import team.klover.server.domain.tour.tourPost.service.TourPostService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;

@RestController
@RequestMapping("/api/v1/tour-post")
@RequiredArgsConstructor
public class ApiV1TourPostController {
    private final TourPostService tourPostService;

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    @GetMapping("/{areaCode}")
    public ApiResponse<TourPostDto> getAreaPost(@ModelAttribute TourPostPage request, @PathVariable("areaCode") String areaCode) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(tourPostService.findByLanguageAndAreaCode(areaCode, pageable)));
    }

    // 사용자 언어 & 관광지 상세 정보 조회
    @GetMapping("/detail/{contentId}")
    public ApiResponse<DetailTourPostDto> getDetailTourPost(@PathVariable("contentId") String contentId) {
        return ApiResponse.of(tourPostService.findByContentId(contentId));
    }
}
