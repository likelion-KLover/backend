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
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.community.comment.dto.req.CommentForm;
import team.klover.server.domain.community.comment.service.CommentService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.service.ReviewService;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.global.util.ChineseLoremGenerator;

import java.util.List;
import java.util.Locale;
import java.util.Random;

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
            CommentService commentService,
            ReviewService reviewService,
            TourPostRepository tourPostRepository
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
                /*


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


                 */

                /*
                for(int i=0;i<50;i++){
                    authV1Service.signup(SignupRequestDto.builder()
                            .email("test"+(i+1)+"@test.com")
                            .nickname("test"+(i+1))
                            .password("1234")
                            .build()
                    );
                }

                long start = System.currentTimeMillis();
                List<Member> members = MemberRepository.findAll();
                Locale[] locales = {Locale.of("ko", "KR"), Locale.of("en", "US"), Locale.of("ja", "JP"), Locale.of("zh", "CN")};
                for (int i = 0; i < 200; i++) {
                    String content = "";
                    for (int j = 0; j < 5; j++) {
                        if ((i % 4) != 3) {
                            content = String.join(" ", new Faker(locales[i % 4]).lorem().sentences(2));
                        } else {
                            content = ChineseLoremGenerator.generate(8);
                        }
                    }
                    System.out.println("content:" + content);
                    CommPostForm commPostForm = CommPostForm.builder()
                            .mapX(0.0)
                            .mapY(0.0)
                            .content(content)
                            .build();
                    Member member = members.get((i % members.size()));
                    CommPost post = commPostService.addCommPost(member.getId(), commPostForm);

                    int randomCount = new Random(System.currentTimeMillis()).nextInt(1, members.size());
                    for (int j = 0; j <= randomCount; j++) {
                        int memberIdx = j%members.size();
                        commPostService.addCommPostLike(members.get(memberIdx).getId(), post.getId());
                        CommentForm commentForm = CommentForm.builder()
                                .content("테에스트으으")
                                .build();
                        commentService.addComment(members.get(memberIdx).getId(), post.getId(), commentForm);
                    }
                }
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("elapsed time(ms):" + elapsed);


                List<TourPost> tourPostList = tourPostRepository.findAll();

                for (TourPost tourPost : tourPostList) {
                    int randomCount = new Random(System.currentTimeMillis()).nextInt(1, 6);
                    for (int j = 0; j <= randomCount; j++) {
                        ReviewForm reviewForm = ReviewForm.builder()
                                .content("테에에스트")
                                .rating(new Random(System.currentTimeMillis()).nextInt(6))
                                .build();
                        reviewService.addReview(members.get(new Random(System.currentTimeMillis()).nextInt(members.size())).getId(), tourPost.getContentId(), reviewForm);
                    }
                }
                */

            }

        };

    }
}
