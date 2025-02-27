package team.klover.server.domain.community.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.community.comment.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 해당 게시글에 작성된 모든 댓글 조회
    Page<Comment> findByCommPostId(Long commPostId, Pageable pageable);

    // 해당 댓글의 모든 하위 댓글 삭제
    List<Comment> findBySuperCommentId(Long superCommentId);

    Comment findByMemberId(Long memberId);
}
