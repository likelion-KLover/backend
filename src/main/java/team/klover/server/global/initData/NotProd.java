package team.klover.server.global.initData;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.member.test.entity.TestMember;
import team.klover.server.domain.member.test.repository.TestMemberRepository;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;

@Configuration
@Profile("!prod")
public class NotProd {
    private final TestMemberRepository testMemberRepository;

    public NotProd(TestMemberRepository testMemberRepository) {
        this.testMemberRepository = testMemberRepository;
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
                TestMember testMember1 = TestMember.builder()
                        .email("test1@test.com")
                        .password("1234")
                        .nickname("test1")
                        .build();
                testMemberRepository.save(testMember1);
                TestMember testMember2 = TestMember.builder()
                        .email("test2@test.com")
                        .password("1234")
                        .nickname("test2")
                        .build();
                testMemberRepository.save(testMember2);
                TestMember testMember3 = TestMember.builder()
                        .email("test3@test.com")
                        .password("1234")
                        .nickname("test3")
                        .build();
                testMemberRepository.save(testMember3);
            }
        };
    }
}
