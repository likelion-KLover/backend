package team.klover.server.domain.community.commPost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.tour.review.entity.ReviewPage;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;

@RestController
@RequestMapping("/api/v1/comm-post")
@RequiredArgsConstructor
public class ApiV1CommPostController {
    private final CommPostService commPostService;

    // 사용자 위치 주변 게시글 조회

    // 본인 게시글 조회
    @GetMapping("/me")
    public ApiResponse<CommPostDto> getMyCommPost(@ModelAttribute ReviewPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.findByTestMemberId(pageable)));
    }

    // 사용자가 저장한 게시글 조회
    @GetMapping("/collection")
    public ApiResponse<CommPostDto> getCollectionCommPost(@ModelAttribute ReviewPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.getSavedCommPostByTestMember(pageable)));
    }

    // 해당 게시글 저장
    @PostMapping("/collection/{id}")
    public ApiResponse<String> addCollectionCommPost(@PathVariable("id") Long id){
        commPostService.addCollectionCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 저장 취소
    @DeleteMapping("/collection/{id}")
    public ApiResponse<String> deleteCollectionCommPost(@PathVariable("id") Long id){
        commPostService.deleteCollectionCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요
    @PostMapping("/like/{id}")
    public ApiResponse<String> addlikeCommPost(@PathVariable("id") Long id){
        commPostService.addlikeCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/like/{id}")
    public ApiResponse<String> deletelikeCommPost(@PathVariable("id") Long id){
        commPostService.deletelikeCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 생성
    @PostMapping
    public ApiResponse<String> addCommPost(@RequestBody @Valid CommPostForm commPostForm) {
        commPostService.addCommPost(commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 수정
    @PutMapping("/{id}")
    public ApiResponse<String> updateCommPost(@PathVariable("id") Long id, @RequestBody @Valid CommPostForm commPostForm) {
        commPostService.updateCommPost(id, commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCommPost(@PathVariable("id") Long id) {
        commPostService.deleteCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
