package team.klover.server.domain.community.comment.controller;

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

@RestController
@RequestMapping("/api/v1/comm-post/comment")
@RequiredArgsConstructor
public class ApiV1CommentController {
    private final CommentService commentService;

    // 해당 게시글에 작성된 모든 댓글 조회
    // http://localhost:8080/api/v1/comm-post/comment/1
    @GetMapping("/{commPostId}")
    public ApiResponse<CommentDto> findByCommPostId(@ModelAttribute CommentPage request, @PathVariable("commPostId") Long commPostId){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commentService.findByCommPostId(commPostId, pageable)));
    }

    // 댓글 좋아요
    // http://localhost:8080/api/v1/comm-post/comment/like/1
    @PostMapping("/like")
    public ApiResponse<String> addCommentLike(){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.addCommentLike(currentMemberId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 댓글 좋아요 취소
    // http://localhost:8080/api/v1/comm-post/comment/like/1
    @DeleteMapping("/like")
    public ApiResponse<String> deleteCommentLike(){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.deleteCommentLike(currentMemberId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글에 댓글 생성
    // http://localhost:8080/api/v1/comm-post/comment/1
    @PostMapping("/{commPostId}")
    public ApiResponse<String> addComment(@PathVariable("commPostId") Long commPostId, @RequestBody @Valid CommentForm commentForm){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.addComment(currentMemberId, commPostId, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 수정
    // http://localhost:8080/api/v1/comm-post/comment/1
    @PutMapping
    public ApiResponse<String> updateComment(@RequestBody @Valid CommentForm commentForm){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.updateComment(currentMemberId, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 삭제
    // http://localhost:8080/api/v1/comm-post/comment/1
    @DeleteMapping
    public ApiResponse<String> deleteComment(){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        commentService.deleteComment(currentMemberId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
