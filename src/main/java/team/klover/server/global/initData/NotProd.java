package team.klover.server.global.initData;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.auth.dto.SignupRequestDto;
import team.klover.server.domain.auth.service.AuthV1Service;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;

@Configuration
@Profile("!prod")

public class NotProd {
    private final AuthV1Service authV1Service;

    public NotProd(AuthV1Service authV1Service) {
        this.authV1Service = authV1Service;
    }

    @Bean
    public ApplicationRunner applicationRunner(
            ApisScheduler apisScheduler,
            TourApiService tourApiService
    ) {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                // Apis, Kopis 스케쥴러 실행
                //
                //
                //
                apisScheduler.getApisApiData();


//                try {
//                    // Member 1,2,3 생성
//                    SignupRequestDto Member1 = SignupRequestDto.builder()
//                            .email("test1@test.com")
//                            .password("1234")
//                            .nickname("test1")
//                            .build();
//                    authV1Service.signup(Member1);
//                    SignupRequestDto Member2 = SignupRequestDto.builder()
//                            .email("test2@test.com")
//                            .password("1234")
//                            .nickname("test2")
//                            .build();
//                    authV1Service.signup(Member2);
//                    SignupRequestDto Member3 = SignupRequestDto.builder()
//                            .email("test3@test.com")
//                            .password("1234")
//                            .nickname("test3")
//                            .build();
//                    authV1Service.signup(Member3);
//                } catch (Exception e) {
//                    System.out.println("Duplicate sign up");
//                }
            }
        };
    }
}

