package team.klover.server.domain.community.commPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.dto.req.XYForm;
import team.klover.server.domain.community.commPost.dto.res.CombinedPostResponse;
import team.klover.server.domain.community.commPost.dto.res.CommPostDto;
import team.klover.server.domain.community.commPost.dto.res.DetailCommPostDto;
import team.klover.server.domain.community.commPost.entity.CommPostPage;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/api/v1/comm-post",produces = APPLICATION_JSON_VALUE)
@Tag(name="ApiV1CommPostController",description = "CommPost API")
@RequiredArgsConstructor
public class ApiV1CommPostController {
    private final CommPostService commPostService;

    // 사용자 위치 주변 게시글(관광지&사용자) 조회
    // http://localhost:8080/api/v1/comm-post/surroundings
    @GetMapping("/surroundings")
    @Operation(summary = "사용자 위치 주변 게시글(관광지&사용자) 조회")
    public CombinedPostResponse getSurroundings(@ModelAttribute CommPostPage request, @RequestBody @Valid XYForm xyForm) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return commPostService.findPostsWithinRadius(xyForm, pageable);
    }

    // 본인 게시글 조회
    // http://localhost:8080/api/v1/comm-post/me
    @GetMapping("/me")
    @Operation(summary = "본인 게시글 조회")
    public ApiResponse<CommPostDto> getMyCommPost(@ModelAttribute CommPostPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        return ApiResponse.of(KloverPage.of(commPostService.findByMemberId(currentMemberId, pageable)));
    }

    // 해당 게시글 상세 조회
    // http://localhost:8080/api/v1/comm-post/detail/1
    @GetMapping("/detail/{id}")
    @Operation(summary = "해당 게시글 상세 조회")
    public ApiResponse<DetailCommPostDto> getDetailCommPost(@PathVariable("id") Long id) {
        return ApiResponse.of(commPostService.findById(id));
    }

    // 사용자가 저장한 게시글 조회
    // http://localhost:8080/api/v1/comm-post/collection
    @GetMapping("/collection")
    @Operation(summary = "사용자가 저장한 게시글 조회")
    public ApiResponse<CommPostDto> getCollectionCommPost(@ModelAttribute CommPostPage request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        return ApiResponse.of(KloverPage.of(commPostService.getSavedCommPostByMember(currentMemberId, pageable)));
    }

    // 사용자 닉네임 & 게시글 내용 검색
    // http://localhost:8080/api/v1/comm-post?keyword=게시글
    @GetMapping
    @Operation(summary = "사용자 닉네임 & 게시글 내용 검색")
    public ApiResponse<CommPostDto> searchCommPost(@ModelAttribute CommPostPage request, @RequestParam("keyword") String keyword) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commPostService.searchByNicknameAndContent(keyword, pageable)));
    }

    // 해당 게시글 저장
    // http://localhost:8080/api/v1/comm-post/collection/1
    @PostMapping("/collection/{id}")
    @Operation(summary = "해당 게시글 저장")
    public ApiResponse<String> addCollectionCommPost(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.addCollectionCommPost(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 저장 취소
    // http://localhost:8080/api/v1/comm-post/collection/1
    @DeleteMapping("/collection/{id}")
    @Operation(summary = "해당 게시글 저장 취소")
    public ApiResponse<String> deleteCollectionCommPost(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.deleteCollectionCommPost(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요
    // http://localhost:8080/api/v1/comm-post/like/1
    @PostMapping("/like/{id}")
    @Operation(summary = "게시글 좋아요")
    public ApiResponse<String> addCommPostLike(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.addCommPostLike(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 좋아요 취소
    // http://localhost:8080/api/v1/comm-post/like/1
    @DeleteMapping("/like/{id}")
    @Operation(summary = "게시글 좋아요 취소")
    public ApiResponse<String> deleteCommPostLike(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.deleteCommPostLike(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 게시글 생성
    // http://localhost:8080/api/v1/comm-post
    @PostMapping
    @Operation(summary = "게시글 생성")
    public ApiResponse<String> addCommPost(@RequestBody @Valid CommPostForm commPostForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.addCommPost(currentMemberId, commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 수정
    // http://localhost:8080/api/v1/comm-post/1
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<String> updateCommPost(@PathVariable("id") Long id, @RequestBody @Valid CommPostForm commPostForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.updateCommPost(currentMemberId, id, commPostForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글 삭제
    // http://localhost:8080/api/v1/comm-post/1
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<String> deleteCommPost(@PathVariable("id") Long id) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commPostService.deleteCommPost(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
