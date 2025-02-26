package team.klover.server.domain.community.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.community.comment.dto.req.CommentForm;
import team.klover.server.domain.community.comment.dto.res.CommentDto;
import team.klover.server.domain.community.comment.entity.CommentPage;
import team.klover.server.domain.community.comment.service.CommentService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/api/v1/comm-post/comment",produces = APPLICATION_JSON_VALUE)
@Tag(name="ApiV1CommentController",description = "Comment API")
@RequiredArgsConstructor
public class ApiV1CommentController {
    private final CommentService commentService;

    // 해당 게시글에 작성된 모든 댓글 조회
    // http://localhost:8080/api/v1/comm-post/comment/1
    @GetMapping("/{commPostId}")
    @Operation(summary = "댓글 전체 조회")
    public ApiResponse<CommentDto> findByCommPostId(@ModelAttribute CommentPage request, @PathVariable("commPostId") Long commPostId){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commentService.findByCommPostId(commPostId, pageable)));
    }

    // 댓글 좋아요
    // http://localhost:8080/api/v1/comm-post/comment/like/1
    @PostMapping("/like/{id}")
    @Operation(summary = "댓글 좋아요")
    public ApiResponse<String> addCommentLike(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.addCommentLike(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 댓글 좋아요 취소
    // http://localhost:8080/api/v1/comm-post/comment/like/1
    @DeleteMapping("/like/{id}")
    @Operation(summary = "좋아요 취소")
    public ApiResponse<String> deleteCommentLike(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.deleteCommentLike(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글에 댓글 생성
    // http://localhost:8080/api/v1/comm-post/comment/1
    @PostMapping("/{commPostId}")
    @Operation(summary = "댓글 생성")
    public ApiResponse<String> addComment(@PathVariable("commPostId") Long commPostId, @RequestBody @Valid CommentForm commentForm){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.addComment(currentMemberId, commPostId, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 수정
    // http://localhost:8080/api/v1/comm-post/comment/1
    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정")
    public ApiResponse<String> updateComment(@PathVariable("id") Long id, @RequestBody @Valid CommentForm commentForm){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.updateComment(currentMemberId, id, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 삭제
    // http://localhost:8080/api/v1/comm-post/comment/1
    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제")
    public ApiResponse<String> deleteComment(@PathVariable("id") Long id){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.deleteComment(currentMemberId, id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
