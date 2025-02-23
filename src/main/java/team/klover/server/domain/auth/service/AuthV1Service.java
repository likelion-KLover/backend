package team.klover.server.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.auth.dto.SignupRequestDto;
import team.klover.server.domain.member.v1.dto.MemberDto;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.auth.dto.MobileSocialLoginParam;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.MemberRole;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.exception.KloverLogicException;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthV1Service {
    private final MemberV1Repository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(SignupRequestDto requestDto) {
        //같은 이메일이 존재하는 경우인데 Social Provider가 Server가 아닌 친구라면?
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new KloverRequestException(ReturnCode.ALREADY_EXIST);
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .role(MemberRole.USER)
                .socialProvider(SocialProvider.SERVER)
                .build();

        try {
            memberRepository.save(member);
        } catch (Exception e) {
            throw new KloverLogicException(ReturnCode.INTERNAL_ERROR);
        }

        return member;
    }

    // 마이페이지 - 비밀번호 수정 (소셜 로그인 사용자는 비밀번호 변경 불가)
    @Transactional
    public void updatePassword(Long memberId, String oldPassword, String newPassword) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        if (member.getSocialProvider() != SocialProvider.SERVER) {
            throw new KloverRequestException(ReturnCode.INVALID_REQUEST);
        }

        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new KloverRequestException(ReturnCode.WRONG_PASSWORD);
        }

        member.setPassword(passwordEncoder.encode(newPassword));
    }




    @Transactional
    public MemberDto processMobileSocialLogin(MobileSocialLoginParam param) {
        Optional<Member> target = memberRepository.findMemberByEmailAndSocialProvider(param.getEmail(),param.getProvider());
        Optional<Member> sameEmail = memberRepository.findMemberByEmail(param.getEmail());

        //존재하지 않으면 회원 가입 처리
        if(target.isEmpty() && sameEmail.isEmpty()){
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            Member member = Member.builder()
                    .email(param.getEmail())
                    .providerId(param.getProviderId())
                    .password(password)
                    .role(MemberRole.USER)
                    .socialProvider(param.getProvider())
                    .nickname(param.getNickname())
                    .build();

            memberRepository.save(member);

            return new MemberDto(member);
        }

        //소셜 로그인을 했을 때 같은 이메일을 가진 사람에 대해 어떻게 할지는 논의가 필요.
        return target.map(MemberDto::new).orElseGet(() -> new MemberDto(sameEmail.get()));

    }
}
