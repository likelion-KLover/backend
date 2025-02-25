package team.klover.server.domain.member.v1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.klover.server.domain.member.v1.dto.MemberDto;
import team.klover.server.domain.member.v1.dto.MemberInfo;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.exception.KloverLogicException;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.s3.S3Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberV1Service {
    private final MemberV1Repository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public void updateMember(Long memberId, MemberUpdateParam param
                                     , MultipartFile imageFile
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));


        String imageUrl = null;
        try {
            if (imageFile != null && !imageFile.isEmpty()) {

                if(member.getProfileUrl() != null) s3Service.deleteFile(member.getProfileUrl());

                System.out.println("업로드를 하는 중입니다.");
                imageUrl = s3Service.uploadFile(imageFile, "profile-images");
                param.changeProfileUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new KloverLogicException(ReturnCode.INTERNAL_ERROR);
        }


        member.update(param);
    }

    public MemberDto getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        return MemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    @Transactional
    public void deleteMember(Long memberId){
        Optional<Member> target = memberRepository.findById(memberId);
        if(target.isEmpty()) throw new KloverException(ReturnCode.NOT_FOUND_ENTITY);

        memberRepository.deleteById(memberId);
    }

    public Member findByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() ->new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
    }

    public Member findServerMember(String email) {
        return memberRepository.findMemberByEmailAndSocialProvider(email, SocialProvider.SERVER)
                .orElseThrow(() ->  new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
    }

    public MemberInfo getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        return new MemberInfo(member);
    }

}
