package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.klover.server.domain.community.commPost.entity.CommPostSave;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;

public interface CommPostSaveRepository extends JpaRepository<CommPostSave, Long> {
    List<CommPostSave> findAllByMember(Member member);
}
