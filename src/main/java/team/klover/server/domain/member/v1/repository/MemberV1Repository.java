package team.klover.server.domain.member.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberV1Repository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmailAndSocialProvider(String email, SocialProvider provider);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    List<Member> findAllByNicknameIn(List<String> mentionedNames);

    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByProviderId(String providerId);

    @Query("""
    select m from Member m
    where m.nickname like concat('%',:nickname,'%')
    """)
    Page<Member> searchMember(@Param("nickname") String nickname, Pageable pageable);
}
