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
    // areaCode: 서울(1) 인천(2) 부산(6) 제주(39)
    // language: KorService1(한국어) EngService1(영어) JpnService1(일본어) ChsService1(중국어간체)

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    @GetMapping("{language}/{areaCode}")
    public ApiResponse<TourPostDto> getAreaPost(@ModelAttribute TourPostPage request, @PathVariable("language") String language,
                                                @PathVariable("areaCode") String areaCode) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(tourPostService.findByLanguageAndAreaCode(language, areaCode, pageable)));
    }

    // 사용자 언어 & 관광지 상세 정보 조회
    @GetMapping("{language}/detail/{contentId}")
    public ApiResponse<DetailTourPostDto> getDetailTourPost(@PathVariable("language") String language, @PathVariable("contentId") String contentId) {
        return ApiResponse.of(tourPostService.findByLanguageAndContentId(language, contentId));
    }

    // 사용자 언어 & 관광지명/지역명 검색
    @GetMapping("{language}")
    public ApiResponse<TourPostDto> searchTourPost(@ModelAttribute TourPostPage request, @PathVariable("language") String language,
                                                   @RequestParam("keyword") String keyword) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(tourPostService.searchByLanguageAndKeyword(language, keyword, pageable)));
    }
}
