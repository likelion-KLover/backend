package team.klover.server.domain.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import team.klover.server.domain.auth.dto.*;
import team.klover.server.domain.member.v1.dto.*;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.auth.service.AuthV1Service;
import team.klover.server.domain.member.v1.service.MemberV1Service;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.redis.RedisService;
import team.klover.server.global.security.custom.CustomUserDetailsService;
import team.klover.server.global.security.provider.JwtTokenProvider;
import team.klover.server.global.util.AuthUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthV1Controller {
    private final AuthV1Service authService;
    private final MemberV1Service memberService;
    private final BCryptPasswordEncoder passwordEncoder; // 로그인 시 비밀번호 검증에 필요
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${web.google.client-id}")
    private String googleClientId;
    @Value("${android.google.client-id}")
    private String googleAndroidClientId;
    @Value("${android.line.client-id}")
    private String lineAndroidClientId;



    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ApiResponse.of(ReturnCode.SUCCESS);

    }

    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequestDto requestDto) {
        Member member = memberService.findServerMember(requestDto.getEmail());

        if (member == null || !passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }

        MemberDto memberDto = new MemberDto(member);

        String accessToken = jwtTokenProvider.generateAccessToken(memberDto);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());


        redisService.saveRefreshToken(member.getEmail(), refreshToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(member.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ApiResponse.of(new JwtResponse(accessToken, refreshToken));

    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody RefreshRequest refreshRequest) {
        // 1. Spring Security Context Logout 처리

        Authentication authentication = AuthUtil.getAuthentication();
        if (authentication == null) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        //3. RefreshToken 삭제 - 앱
        String refreshToken = refreshRequest.getRefreshToken();
        if (refreshToken == null) {
            throw new KloverRequestException(ReturnCode.INVALID_REQUEST);
        }

        String emailFromToken = jwtTokenProvider.getMemberEmailFromToken(refreshToken);

        if(emailFromToken ==null){
            throw new KloverRequestException(ReturnCode.EXPIRED_TOKEN);
        }

        String redisToken = redisService.getRefreshToken(emailFromToken);
        redisService.deleteRefreshToken(emailFromToken);

        //어쨌든 변조된 걸 지운 후에 이상하다고 오류를 내야하기 때문
        if(!redisToken.equals(refreshToken)) {
            throw new KloverRequestException(ReturnCode.FORGED_TOKEN);
        }


        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    @GetMapping("/authinfo")
    public ApiResponse<MemberDto> authorize() {
        MemberDto realMember = new MemberDto(memberService.getMemberById(AuthUtil.getCurrentMemberId()));
        return ApiResponse.of(realMember);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtResponse> refresh(@RequestBody RefreshRequest refreshRequest) {

        String refreshToken = refreshRequest.getRefreshToken();
        if (refreshToken == null) {
            throw new KloverRequestException(ReturnCode.INVALID_REQUEST);
        }

        if (!jwtTokenProvider.isTokenValid(refreshToken)) {
            throw new KloverRequestException(ReturnCode.EXPIRED_TOKEN);
        }

        String email = jwtTokenProvider.getMemberEmailFromToken(refreshToken);
        String savedRefreshToken = redisService.getRefreshToken(email);

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            redisService.deleteRefreshToken(email);
            throw new KloverRequestException(ReturnCode.FORGED_TOKEN);
        }

        Member member = memberService.findByEmail(email);

        MemberDto memberDto = new MemberDto(
                member
        );

        String newAccessToken = jwtTokenProvider.generateAccessToken(memberDto);
        return ApiResponse.of(new RefreshResponse(newAccessToken));

    }

    @PatchMapping("/password")
    public ApiResponse changePassword(@RequestBody PasswordChangeRequest request) {

        Long memberId = AuthUtil.getCurrentMemberId();
        authService.updatePassword(memberId, request.getOldPassword(), request.getNewPassword());

        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    @PostMapping("/google")
    public ApiResponse<JwtResponse> googleNativeLogin(@RequestBody Map<String,String> request){
        GoogleIdToken.Payload payload = verifyGoogleIdToken(request.get("idToken")).block();
        if (payload != null) {
            payload.forEach((k,v)-> System.out.println("key:"+k+", value:"+v));
        }

        String providerId = (String) payload.get("sub");
        String email = payload.getEmail();
        String nickname = (String) payload.get("name");
        String aud = (String)payload.getAudience();
        if(!aud.equals(googleAndroidClientId) && !aud.equals(googleClientId))  throw new KloverRequestException(ReturnCode.INVALID_REQUEST);

        System.out.println("nickname:"+nickname);
        MobileSocialLoginParam param = MobileSocialLoginParam.builder()
                .nickname(nickname)
                .email(email)
                .providerId(providerId)
                .provider(SocialProvider.GOOGLE)
                .build();

        return generateJwtToken(param);

    }

    private Mono<GoogleIdToken.Payload> verifyGoogleIdToken(String idToken) {
        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tokeninfo")
                        .queryParam("id_token", idToken)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .mapNotNull(response -> {
                    if (response.containsKey("email")) {
                        //response.forEach((k,v)-> System.out.println("key:"+k+", value:"+v));
                        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
                        payload.setEmail((String) response.get("email"));
                        payload.set("name", response.get("name"));
                        payload.set("sub", response.get("sub"));
                        payload.setAudience(response.get("aud"));
                        return payload;
                    }
                    return null;
                })
                .onErrorResume(WebClientResponseException.class, ex -> Mono.empty());
    }

    @PostMapping("/line")
    public ApiResponse<JwtResponse> lineLogin(@RequestBody Map<String,String> request){
        Map payload = verifyLineIdToken(request.get("idToken")).block();
        if (payload == null || payload.isEmpty()) {
            throw new KloverRequestException(ReturnCode.INVALID_REQUEST);
        }


        String providerId = (String) payload.get("sub");
        String email = (String)payload.get("email");
        String nickname = (String) payload.get("name");
        String aud = (String)payload.get("aud");
        if(!aud.equals(lineAndroidClientId)) throw new KloverRequestException(ReturnCode.INVALID_REQUEST);

        MobileSocialLoginParam param = MobileSocialLoginParam.builder()
                .nickname(nickname)
                .email(email)
                .providerId(providerId)
                .provider(SocialProvider.LINE)
                .build();

        return generateJwtToken(param);

    }

    private ApiResponse<JwtResponse> generateJwtToken(MobileSocialLoginParam param) {
        MemberDto memberDto = authService.processMobileSocialLogin(param);

        String accessToken = jwtTokenProvider.generateAccessToken(memberDto);
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberDto.getEmail());


        JwtResponse authResponse =  JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        redisService.saveRefreshToken(memberDto.getEmail(), refreshToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberDto.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ApiResponse.of(authResponse);
    }

    private Mono<Map> verifyLineIdToken(String idToken) {
        WebClient webClient = WebClient.create("https://api.line.me/oauth2/v2.1");
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/verify")
                        .build())
                .header("Content-Type", "application/x-www-form-urlencoded") // 명시적으로 설정
                .body(BodyInserters.fromFormData("id_token", idToken)
                        .with("client_id", lineAndroidClientId)) // 여러 개의 값 추가 가능
                .retrieve()
                .bodyToMono(Map.class)
                .mapNotNull(response -> {
                    if (response.containsKey("email")) {
                        //response.forEach((k,v)-> System.out.println("key:"+k+", value:"+v));
                        return response;
                    }
                    return null;
                })
                .onErrorResume(WebClientResponseException.class, ex -> Mono.empty());
    }


    @GetMapping("/test")
    public ApiResponse<String> test(){
        return ApiResponse.of("인증인가가 잘 되는 중임");
    }

}
