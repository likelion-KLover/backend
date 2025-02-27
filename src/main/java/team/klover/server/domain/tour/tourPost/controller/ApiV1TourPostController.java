package team.klover.server.domain.tour.tourPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/api/v1/tour-post",produces = APPLICATION_JSON_VALUE)
@Tag(name="ApiV1TourPostController",description = "TourPost API")
@RequiredArgsConstructor
public class ApiV1TourPostController {
    private final TourPostService tourPostService;
    // areaCode: 서울(1) 인천(2) 부산(6) 제주(39)
    // language: KorService1(한국어) EngService1(영어) JpnService1(일본어) ChsService1(중국어간체)

    // 사용자 언어 & 지역기반 관광지 데이터 조회
    // http://localhost:8080/api/v1/tour-post/EngService1/1?page=0&size=15
    @GetMapping("/{language}/{areaCode}")
    @Operation(summary = "사용자 언어 & 지역기반 관광지 데이터 조회")
    public ApiResponse<TourPostDto> getAreaPost(@ModelAttribute TourPostPage request, @PathVariable("language") String language,
                                                @PathVariable("areaCode") String areaCode) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(tourPostService.findByLanguageAndAreaCode(language, areaCode, pageable)));
    }

    // 해당 관광지 상세 조회
    // http://localhost:8080/api/v1/tour-post/detail/264107
    @GetMapping("/detail/{contentId}")
    @Operation(summary = "해당 관광지 상세 조회")
    public ApiResponse<DetailTourPostDto> getDetailTourPost(@PathVariable("contentId") Long contentId) {
        return ApiResponse.of(tourPostService.findByContentId(contentId));
    }

    // 사용자가 저장한 관광지 조회
    // http://localhost:8080/api/v1/tour-post/collection?page=0&size=15
    @GetMapping("/collection")
    @Operation(summary = "사용자가 저장한 관광지 조회")
    public ApiResponse<TourPostDto> getCollectionTourPost(@ModelAttribute TourPostPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        return ApiResponse.of(KloverPage.of(tourPostService.getSavedTourPostByMember(currentMemberId, pageable)));
    }

    // 사용자 언어 & 관광지명/지역명 검색
    // http://localhost:8080/api/v1/tour-post/EngService1?keyword=압구정&page=0&size=15
    @GetMapping("/{language}")
    @Operation(summary = "사용자 언어 & 관광지명/지역명 검색")
    public ApiResponse<TourPostDto> searchTourPost(@ModelAttribute TourPostPage request, @PathVariable("language") String language,
                                                   @RequestParam("keyword") String keyword) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(tourPostService.searchByLanguageAndKeyword(language, keyword, pageable)));
    }

    // 해당 관광지 저장
    // http://localhost:8080/api/v1/tour-post/collection/264107
    @PostMapping("/collection/{contentId}")
    @Operation(summary = "해당 관광지 저장")
    public ApiResponse<String> addCollectionTourPost(@PathVariable("contentId") Long contentId){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        tourPostService.addCollectionTourPost(currentMemberId, contentId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 관광지 저장 취소
    // http://localhost:8080/api/v1/tour-post/collection/264107
    @DeleteMapping("/collection/{contentId}")
    @Operation(summary = "해당 관광지 저장 취소")
    public ApiResponse<String> deleteCollectionTourPost(@PathVariable("contentId") Long contentId){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        tourPostService.deleteCollectionTourPost(currentMemberId, contentId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
