package team.klover.server.global.initData;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;

@Configuration
@Profile("!prod")
public class NotProd {
    private final MemberV1Repository memberV1Repository;

    public NotProd(MemberV1Repository memberV1Repository) {
        this.memberV1Repository = memberV1Repository;
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
//                apisScheduler.getApisApiData();



                // TestMember 1,2,3 생성
                Member testMember1 = Member.builder()
                        .email("test1@test.com")
                        .password("1234")
                        .nickname("test1")
                        .build();
                memberV1Repository.save(testMember1);
                Member testMember2 = Member.builder()
                        .email("test2@test.com")
                        .password("1234")
                        .nickname("test2")
                        .build();
                memberV1Repository.save(testMember2);
                Member testMember3 = Member.builder()
                        .email("test3@test.com")
                        .password("1234")
                        .nickname("test3")
                        .build();
                memberV1Repository.save(testMember3);
            }
        };
    }
}
