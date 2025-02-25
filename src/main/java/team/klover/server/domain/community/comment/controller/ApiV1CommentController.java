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

@RestController
@RequestMapping("/api/v1/comm-post/comment")
@RequiredArgsConstructor
public class ApiV1CommentController {
    private final CommentService commentService;

    // 해당 게시글에 작성된 모든 댓글 조회
    @GetMapping("/{commPostId}")
    public ApiResponse<CommentDto> findByCommPostId(@ModelAttribute CommentPage request, @PathVariable("commPostId") Long commPostId){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(commentService.findByCommPostId(commPostId, pageable)));
    }

    // 댓글 좋아요
    @PostMapping("/like/{id}")
    public ApiResponse<String> addCommentLike(@PathVariable("id") Long id){
        commentService.addCommentLike(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/like/{id}")
    public ApiResponse<String> deleteCommentLike(@PathVariable("id") Long id){
        commentService.deleteCommentLike(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 게시글에 댓글 생성
    @PostMapping("/{commPostId}")
    public ApiResponse<String> addComment(@PathVariable("commPostId") Long commPostId, @RequestBody @Valid CommentForm commentForm){
        commentService.addComment(commPostId, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 수정
    @PutMapping("/{id}")
    public ApiResponse<String> updateComment(@PathVariable("id") Long id, @RequestBody @Valid CommentForm commentForm){
        commentService.updateComment(id, commentForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 댓글 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteComment(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
