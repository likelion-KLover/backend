package team.klover.server.global.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.test.entity.TestMember;
import team.klover.server.domain.member.test.repository.TestMemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final TestMemberRepository testMemberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TestMember testMember = testMemberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(testMember.getEmail())
                .password(testMember.getPassword()) // 패스워드는 보통 사용 안 함 (JWT 기반 인증)
                .authorities("ROLE_USER") // 권한 설정 (추후 DB에서 가져올 수 있음)
                .build();
    }
}
