package team.klover.server.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.auth.dto.SignupRequestDto;
import team.klover.server.domain.member.v1.dto.MemberDto;
import team.klover.server.domain.auth.dto.MobileSocialLoginParam;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.MemberRole;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverLogicException;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

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
        if (memberRepository.existsByEmail(requestDto.getEmail()) || memberRepository.existsByNickname(requestDto.getNickname())) {
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
        } catch (Exception e){
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
        Optional<Member> sameEmailInServerUser = memberRepository.findMemberByEmailAndSocialProvider(param.getEmail(),SocialProvider.SERVER);



        //존재하지 않으면 회원 가입 처리
        if(target.isEmpty() && sameEmailInServerUser.isEmpty()){
            String password = passwordEncoder.encode(UUID.randomUUID().toString());

            boolean isDuplicateNickname = memberRepository.existsByNickname(param.getNickname());

            String nickname = param.getNickname();
            if(isDuplicateNickname) nickname += UUID.randomUUID().toString().substring(0,8);

            Member member = Member.builder()
                    .email(param.getEmail())
                    .providerId(param.getProviderId())
                    .password(password)
                    .role(MemberRole.USER)
                    .socialProvider(param.getProvider())
                    .nickname(nickname)
                    .build();

            memberRepository.save(member);

            return new MemberDto(member);
        }

        //소셜 로그인을 했을 때 같은 이메일을 가진 서버 사람에 대해서는 그냥 소셜 로그인으로 강제 통합
        return target.map(MemberDto::new).orElseGet(() -> {
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            Member severUser = sameEmailInServerUser.get();
            severUser.setSocialProvider(param.getProvider());
            severUser.setProviderId(param.getProviderId());
            severUser.setPassword(password);
            return new MemberDto(severUser);
        }
        );

    }
}
