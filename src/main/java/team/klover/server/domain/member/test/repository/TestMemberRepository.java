package team.klover.server.domain.member.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.member.test.entity.TestMember;

import java.util.Optional;

@Repository
public interface TestMemberRepository extends JpaRepository<TestMember, Long> {
    // 이메일로 회원 찾기
    Optional<TestMember> findByEmail(String email);

}
