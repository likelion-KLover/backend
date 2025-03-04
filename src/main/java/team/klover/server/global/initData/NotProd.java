package team.klover.server.global.initData;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.auth.service.AuthV1Service;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.review.service.ReviewService;
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
            TourApiService tourApiService,
            AuthV1Service authV1Service,
            CommPostService commPostService,
            ReviewService reviewService
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
//
//
//
//                // Member 1,2,3 생성
//                Member Member1 = authV1Service.signup(SignupRequestDto.builder()
//                                .email("test1@test.com")
//                                .nickname("test1")
//                                .password("1234")
//                        .build());
//                Member Member2 = authV1Service.signup(SignupRequestDto.builder()
//                        .email("test2@test.com")
//                        .nickname("test2")
//                        .password("1234")
//                        .build());
//                Member Member3 = authV1Service.signup(SignupRequestDto.builder()
//                        .email("test3@test.com")
//                        .nickname("test3")
//                        .password("1234")
//                        .build());
//
//                // 번역api리뷰
//                Long commonPlaceId = 1L;
//                // 한국어 리뷰
//                ReviewForm koreanReview = ReviewForm.builder()
//                        .content("이 장소는 정말 아름다웠어요. 특히 봄에 방문하면 벚꽃이 만발해서 더욱 좋습니다.")
//                        .rating(5)
//                        .build();
//                reviewService.addReview(Member1.getId(), commonPlaceId, koreanReview);
//
//                // 영어 리뷰
//                ReviewForm englishReview = ReviewForm.builder()
//                        .content("This place was truly beautiful. Especially if you visit in spring, it's even better with the cherry blossoms in full bloom.")
//                        .rating(4)
//                        .build();
//                reviewService.addReview(Member2.getId(), commonPlaceId, englishReview);
//
//                // 일본어 리뷰
//                ReviewForm japaneseReview = ReviewForm.builder()
//                        .content("この場所は本当に美しかったです。特に春に訪れると、桜が満開でさらに良いです。")
//                        .rating(5)
//                        .build();
//                reviewService.addReview(Member3.getId(), commonPlaceId, japaneseReview);
//
//                // 중국어 리뷰
//                ReviewForm chineseReview = ReviewForm.builder()
//                        .content("这个地方真的很漂亮。尤其是在春天访问时，樱花盛开，更加美丽。")
//                        .rating(4)
//                        .build();
//                reviewService.addReview(Member1.getId(), commonPlaceId, chineseReview);
//
//                System.out.println("Test review data for translation has been created!");
//            }
//


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

