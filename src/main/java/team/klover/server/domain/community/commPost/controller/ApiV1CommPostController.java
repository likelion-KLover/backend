package team.klover.server.domain.community.commPost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.req.XYForm;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.dto.res.DetailCommPostDto;
import team.klover.server.domain.community.commPost.entity.CommPostPage;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;

@RestController
@RequestMapping("/api/v1/comm-post")
@RequiredArgsConstructor
public class ApiV1CommPostController {
    private final CommPostService commPostService;

    // 사용자 위치 주변 게시글 조회
    // http://localhost:8090/api/v1/comm-post/surroundings
    @GetMapping("/surroundings")
    public ApiResponse<CommPostDto> getSurroundings(@ModelAttribute CommPostPage request, @RequestBody @Valid XYForm xyForm) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.findPostsWithinRadius(xyForm, pageable)));
    }

    // 본인 게시글 조회
    // http://localhost:8090/api/v1/comm-post/me
    @GetMapping("/me")
    public ApiResponse<CommPostDto> getMyCommPost(@ModelAttribute CommPostPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.findByMemberId(pageable)));
    }

    // 해당 게시글 상세 조회
    // http://localhost:8090/api/v1/comm-post/detail/1
    @GetMapping("/detail/{id}")
    public ApiResponse<DetailCommPostDto> getDetailCommPost(@PathVariable("id") Long id) {
        return ApiResponse.of(commPostService.findById(id));
    }

    // 사용자가 저장한 게시글 조회
    // http://localhost:8090/api/v1/comm-post/collection
    @GetMapping("/collection")
    public ApiResponse<CommPostDto> getCollectionCommPost(@ModelAttribute CommPostPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.getSavedCommPostByMember(pageable)));
    }

    // 사용자 닉네임 & 게시글 내용 검색
    // http://localhost:8090/api/v1/comm-post?keyword=게시글
    @GetMapping
    public ApiResponse<CommPostDto> searchCommPost(@ModelAttribute CommPostPage request, @RequestParam("keyword") String keyword) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.searchByNicknameAndContent(keyword, pageable)));
    }

    // 해당 게시글 저장
    // http://localhost:8090/api/v1/comm-post/collection/1
    @PostMapping("/collection/{id}")
    public ApiResponse<String> addCollectionCommPost(@PathVariable("id") Long id){
        commPostService.addCollectionCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 저장 취소
    // http://localhost:8090/api/v1/comm-post/collection/1
    @DeleteMapping("/collection/{id}")
    public ApiResponse<String> deleteCollectionCommPost(@PathVariable("id") Long id){
        commPostService.deleteCollectionCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요
    // http://localhost:8090/api/v1/comm-post/like/1
    @PostMapping("/like/{id}")
    public ApiResponse<String> addCommPostLike(@PathVariable("id") Long id){
        commPostService.addCommPostLike(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요 취소
    // http://localhost:8090/api/v1/comm-post/like/1
    @DeleteMapping("/like/{id}")
    public ApiResponse<String> deleteCommPostLike(@PathVariable("id") Long id){
        commPostService.deleteCommPostLike(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 생성
    // http://localhost:8090/api/v1/comm-post
    @PostMapping
    public ApiResponse<String> addCommPost(@RequestBody @Valid CommPostForm commPostForm) {
        commPostService.addCommPost(commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 수정
    // http://localhost:8090/api/v1/comm-post/1
    @PutMapping("/{id}")
    public ApiResponse<String> updateCommPost(@PathVariable("id") Long id, @RequestBody @Valid CommPostForm commPostForm) {
        commPostService.updateCommPost(id, commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 삭제
    // http://localhost:8090/api/v1/comm-post/1
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCommPost(@PathVariable("id") Long id) {
        commPostService.deleteCommPost(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
