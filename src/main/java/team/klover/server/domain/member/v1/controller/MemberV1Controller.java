package team.klover.server.domain.member.v1.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.klover.server.domain.member.v1.dto.MemberInfo;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.service.MemberV1Service;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.redis.RedisService;
import team.klover.server.global.util.AuthUtil;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberV1Controller {
    private final MemberV1Service memberService;
    private final RedisService redisService;


    //보통 앱들도 이미지, 닉네임 등 내 정보를 한 번에 바꿀 수 있게 되어있으므로, 그러는 게 좋을 것이라고 사료됨.
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse modifyUserInfo(@RequestPart("request") MemberUpdateParam param,
                                      @RequestPart(value = "imageFile", required = false) MultipartFile imageFile // 이미지 파일 (선택 사항)
    ) {

        Long memberId = AuthUtil.getCurrentMemberId();

        memberService.updateMember(memberId, param
                //        , imageFile
        );

        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    @DeleteMapping
    public ApiResponse deleteAccount(){

        Long memberId = AuthUtil.getCurrentMemberId();
        Member member = memberService.getMemberById(memberId);

        redisService.deleteRefreshToken(member.getEmail());

        memberService.deleteMember(memberId);

        return ApiResponse.of(ReturnCode.SUCCESS);
    }


    @GetMapping("/{memberId}")
    public ApiResponse<MemberInfo> getOtherUserPage(@PathVariable("memberId")Long memberId){
        return ApiResponse.of(memberService.getMemberInfo(memberId));
    }

    @GetMapping
    public ApiResponse<MemberInfo> getMyPage(){
       Long memberId = AuthUtil.getCurrentMemberId();
       return ApiResponse.of(memberService.getMemberInfo(memberId));
    }

}
