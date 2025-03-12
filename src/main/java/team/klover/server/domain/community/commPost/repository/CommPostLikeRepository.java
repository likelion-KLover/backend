package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.klover.server.domain.community.commPost.entity.CommPostLike;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CommPostLikeRepository extends JpaRepository<CommPostLike, Long> {
    List<CommPostLike> findAllByMember(Member member);

    @Query("""
select count(cl) from CommPostLike cl
where cl.commPost.id = :commPostId
""")
    long countCommPostLike(@Param("commPostId") Long commPostId);

    @Query("""
select cl from CommPostLike cl
where cl.commPost.id = :commPostId and cl.member.id = :memberId
""")
    Optional<CommPostLike> haveLiked(@Param("commPostId")Long CommPostId, @Param("memberId")Long memberId);
}
