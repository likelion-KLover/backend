package team.klover.server.domain.community.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.klover.server.domain.community.comment.entity.CommentLike;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


    List<CommentLike> findAllByMember(Member member);

    @Query("""
select cl from CommentLike cl
where cl.member.id = :memberId and cl.comment.id = :commentId
""")
    Optional<CommentLike> haveLiked(@Param("commentId") Long commentId, @Param("memberId")Long memberId);
}
