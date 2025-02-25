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
    private final MemberV1Repository MemberRepository;

    public NotProd(MemberV1Repository MemberRepository) {
        this.MemberRepository = MemberRepository;
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



                // Member 1,2,3 생성
                Member Member1 = Member.builder()
                        .email("test1@test.com")
                        .password("1234")
                        .nickname("test1")
                        .build();
                MemberRepository.save(Member1);
                Member Member2 = Member.builder()
                        .email("test2@test.com")
                        .password("1234")
                        .nickname("test2")
                        .build();
                MemberRepository.save(Member2);
                Member Member3 = Member.builder()
                        .email("test3@test.com")
                        .password("1234")
                        .nickname("test3")
                        .build();
                MemberRepository.save(Member3);
            }
        };
    }
}
