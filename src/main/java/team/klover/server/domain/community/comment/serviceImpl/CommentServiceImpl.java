package team.klover.server.domain.community.comment.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.repository.CommPostRepository;
import team.klover.server.domain.community.comment.dto.req.CommentForm;
import team.klover.server.domain.community.comment.dto.res.CommentDto;
import team.klover.server.domain.community.comment.entity.Comment;
import team.klover.server.domain.community.comment.entity.CommentLike;
import team.klover.server.domain.community.comment.entity.CommentPage;
import team.klover.server.domain.community.comment.repository.CommentRepository;
import team.klover.server.domain.community.comment.service.CommentService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommPostRepository commPostRepository;
    private final MemberV1Repository memberV1Repository;

    // 해당 게시글에 작성된 모든 댓글 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto> findByCommPostId(Long commPostId, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<Comment> comments = commentRepository.findByCommPostId(commPostId, pageable);
        return comments.map(this::convertToCommentDto);
    }

    // 댓글 좋아요
    @Override
    @Transactional
    public void addCommentLike(Long memberId, Long id){
        Member member = memberV1Repository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        Comment comment = commentRepository.findById(id).orElse(null);

        boolean alreadySaved = comment.getLikedMembers()
                .stream()
                .anyMatch(savedMember -> savedMember.getId().equals(member.getId()));
        if (alreadySaved) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }
        CommentLike commentLike = new CommentLike(member, comment);
        comment.getLikedMembers().add(commentLike);
    }

    // 댓글 좋아요 취소
    @Override
    @Transactional
    public void deleteCommentLike(Long memberId, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        CommentLike commentLike = comment.getLikedMembers()
                .stream()
                .filter(m -> m.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        comment.getLikedMembers().remove(commentLike);
    }

    // 해당 게시글에 댓글 생성
    @Override
    @Transactional
    public void addComment(Long memberId, Long commPostId, @Valid CommentForm commentForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        CommPost commPost = commPostRepository.findById(commPostId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        Comment comment = Comment.builder()
                .member(member)
                .commPost(commPost)
                .nickname(member.getNickname())
                .content(commentForm.getContent())
                .superCommentId(commentForm.getSuperCommentId())
                .build();
        commentRepository.save(comment);
    }

    // 해당 댓글 수정
    @Override
    @Transactional
    public void updateComment(Long memberId, Long id, @Valid CommentForm commentForm){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        Member member = memberV1Repository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        if (!comment.getMember().getId().equals(member.getId())) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        comment.setContent(commentForm.getContent());
        commentRepository.save(comment);
    }

    // 해당 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long memberId, Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        Member member = memberV1Repository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        Long currentUserId = member.getId();
        if (!comment.getMember().getId().equals(currentUserId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        deleteChildComments(id);
        commentRepository.save(comment); // 답글 삭제 후 더티 체킹
        commentRepository.delete(comment);
    }

    // 해당 댓글의 모든 하위 댓글 삭제
    private void deleteChildComments(Long superCommentId) {
        List<Comment> childComments = commentRepository.findBySuperCommentId(superCommentId);
        for (Comment childComment : childComments) {
            deleteChildComments(childComment.getId()); // 재귀 호출
            commentRepository.delete(childComment);
        }
    }

    // 요청 페이지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = CommentPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // Comment를 CommentDto로 변환
    private CommentDto convertToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getNickname())
                .likeCount(comment.getLikedMembers().size())
                .content(comment.getContent())
                .superCommentId(comment.getSuperCommentId())
                .createDate(comment.getCreateDate())
                .build();
    }
}
