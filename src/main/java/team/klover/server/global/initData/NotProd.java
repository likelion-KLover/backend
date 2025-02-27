package team.klover.server.global.initData;

import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.auth.dto.SignupRequestDto;
import team.klover.server.domain.auth.service.AuthV1Service;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;

import java.util.List;
import java.util.Locale;

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
            TourApiService tourApiService,
            AuthV1Service authV1Service,
            CommPostService commPostService
    ) {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                // Apis, Kopis 스케쥴러 실행
                //
                //
                //
                /*
                apisScheduler.getApisApiData();



                // Member 1,2,3 생성
                Member Member1 = authV1Service.signup(SignupRequestDto.builder()
                                .email("test1@test.com")
                                .nickname("test1")
                                .password("1234")
                        .build());
                Member Member2 = authV1Service.signup(SignupRequestDto.builder()
                        .email("test2@test.com")
                        .nickname("test2")
                        .password("1234")
                        .build());
                Member Member3 = authV1Service.signup(SignupRequestDto.builder()
                        .email("test3@test.com")
                        .nickname("test3")
                        .password("1234")
                        .build());
            }

                 */


//                long start = System.currentTimeMillis();
//                List<Member> members =  MemberRepository.findAll();
//                Locale[] locales = {Locale.of("ko","KR"), Locale.of("en", "US"),  Locale.of("ja", "JP"), Locale.of("zh", "CN")};
//                for(Locale locale: locales){
//                    System.out.println("Locale:"+locale);
//                }
//                for(int i=0;i<200;i++){
//                    System.out.println("idx of faker:"+(i%4));
//                    System.out.println("value of locale:"+locales[(i%4)]);
//                    String content="";
//                    for(int j=0;j<5;j++){
//                        content += new Faker(locales[i%4]).ancient().hero()+" ";
//                    }
//                    System.out.println("content:"+content);
//                    CommPostForm commPostForm = CommPostForm.builder()
//                            .mapX(0.0)
//                            .mapY(0.0)
//                            .content(content)
//                            .build();
//                    Member member = members.get((i%3));
//                    commPostService.addCommPost(member.getId(), commPostForm);
//                }
//                long elapsed = System.currentTimeMillis() - start;
//                System.out.println("elapsed time(ms):"+elapsed);




            }

        };

    }
}
