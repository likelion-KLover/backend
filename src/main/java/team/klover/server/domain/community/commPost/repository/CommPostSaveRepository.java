package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.klover.server.domain.community.commPost.entity.CommPostLike;
import team.klover.server.domain.community.commPost.entity.CommPostSave;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;
import java.util.Optional;

public interface CommPostSaveRepository extends JpaRepository<CommPostSave, Long> {
    List<CommPostSave> findAllByMember(Member member);

    @Query("""
select cs from CommPostSave cs
where cs.commPost.id = :commPostId and cs.member.id = :memberId
""")
    Optional<CommPostSave> haveSaved(@Param("commPostId")Long CommPostId, @Param("memberId")Long memberId);
}
