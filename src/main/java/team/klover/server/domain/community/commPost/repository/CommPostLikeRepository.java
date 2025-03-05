package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.klover.server.domain.community.commPost.entity.CommPostLike;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;

public interface CommPostLikeRepository extends JpaRepository<CommPostLike, Long> {
    List<CommPostLike> findAllByMember(Member member);

    @Query("""
select coalesce(count(cl),0) from CommPostLike cl
where cl.commPost.id = :commpostId
""")
    long countCommPostLike(Long commpostId);
}
