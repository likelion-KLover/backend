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
import team.klover.server.domain.member.test.entity.TestMember;
import team.klover.server.domain.member.test.repository.TestMemberRepository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommPostRepository commPostRepository;
    private final TestMemberRepository testMemberRepository;

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
    public void addCommentLike(Long id){
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        Comment comment = commentRepository.findById(id).orElse(null);

        boolean alreadySaved = comment.getLikedMembers()
                .stream()
                .anyMatch(savedMember -> savedMember.getId().equals(1L));
        if (alreadySaved) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }
        CommentLike commentLike = new CommentLike(testMember, comment);
        comment.getLikedMembers().add(commentLike);
    }

    // 댓글 좋아요 취소
    @Override
    @Transactional
    public void deleteCommentLike(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        CommentLike commentLike = comment.getLikedMembers()
                .stream()
                .filter(m -> m.getTestMember().getId().equals(1L))
                .findFirst()
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        comment.getLikedMembers().remove(commentLike);
    }

    // 해당 게시글에 댓글 생성
    @Override
    @Transactional
    public void addComment(Long commPostId, @Valid CommentForm commentForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        CommPost commPost = commPostRepository.findById(commPostId).orElse(null);

        Comment comment = Comment.builder()
                .testMember(testMember)
                .commPost(commPost)
                .nickname(testMember.getNickname())
                .content(commentForm.getContent())
                .superCommentId(commentForm.getSuperCommentId())
                .build();
        commentRepository.save(comment);
    }

    // 해당 댓글 수정
    @Override
    @Transactional
    public void updateComment(Long id, @Valid CommentForm commentForm){
        Comment comment = commentRepository.findById(id).orElse(null);

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        Long currentUserId = testMember.getId();
        if (!comment.getTestMember().getId().equals(currentUserId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        comment.setContent(commentForm.getContent());
        commentRepository.save(comment);
    }

    // 해당 댓글 삭제
    @Override
    @Transactional
    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY);
        }

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        TestMember testMember = testMemberRepository.findById(1L).orElse(null); // 임시 - 변경 필요
        Long currentUserId = testMember.getId();
        if (!comment.getTestMember().getId().equals(currentUserId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        deleteChildComments(id);
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
                .testMemberId(comment.getTestMember().getId())
                .nickname(comment.getNickname())
                .likeCount(comment.getLikedMembers().size())
                .content(comment.getContent())
                .superCommentId(comment.getSuperCommentId())
                .createDate(comment.getCreateDate())
                .build();
    }
}
