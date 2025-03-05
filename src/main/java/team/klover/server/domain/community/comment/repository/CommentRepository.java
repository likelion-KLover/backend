package team.klover.server.domain.community.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.comment.entity.Comment;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 해당 게시글에 작성된 모든 댓글 페이지 조회
    Page<Comment> findByCommPostIdOrderByCreateDateDesc(Long commPostId, Pageable pageable);

    // 해당 댓글의 모든 하위 댓글 삭제
    List<Comment> findBySuperCommentId(Long superCommentId);

    // 해당 게시글에 작성된 모든 댓글 리스트 조회
    List<Comment> findByCommPost(CommPost commPost);

    List<Comment> findAllByMember(Member member);

    @Query("""
select coalesce(count(c.id),0) from Comment c
where c.commPost.id = :commpostId
""")
    long countCommPostComment(Long commpostId);
}
