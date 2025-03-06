package team.klover.server.global.initData;

import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.klover.server.domain.auth.dto.SignupRequestDto;
import team.klover.server.domain.auth.service.AuthV1Service;
import team.klover.server.domain.community.commPost.dto.req.CommPostForm;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.repository.CommPostRepository;
import team.klover.server.domain.community.commPost.service.CommPostService;
import team.klover.server.domain.community.comment.dto.req.CommentForm;
import team.klover.server.domain.community.comment.service.CommentService;
import team.klover.server.domain.member.v1.dto.MemberUpdateParam;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.domain.member.v1.enums.SocialProvider;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.domain.member.v1.service.MemberV1Service;
import team.klover.server.domain.tour.review.dto.req.ReviewForm;
import team.klover.server.domain.tour.review.service.ReviewService;
import team.klover.server.domain.tour.tourApi.scheduler.ApisScheduler;
import team.klover.server.domain.tour.tourApi.service.TourApiService;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.domain.tour.tourPost.repository.TourPostRepository;
import team.klover.server.global.util.ChineseLoremGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Configuration
@Profile("!prod")
public class NotProd {
    private final MemberV1Repository MemberRepository;
    private final MemberV1Repository memberV1Repository;

    public NotProd(MemberV1Repository MemberRepository, MemberV1Repository memberV1Repository) {
        this.MemberRepository = MemberRepository;
        this.memberV1Repository = memberV1Repository;
    }

    @Bean
    public ApplicationRunner applicationRunner(
            ApisScheduler apisScheduler,
            TourApiService tourApiService,
            AuthV1Service authV1Service,
            CommPostService commPostService,
            CommentService commentService,
            ReviewService reviewService,
            TourPostRepository tourPostRepository,
            CommPostRepository commPostRepository,
            MemberV1Service memberV1Service
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


                for(int i=0;i<10;i++){
                    authV1Service.signup(SignupRequestDto.builder()
                            .email("test"+(i+1)+"@test.com")
                            .nickname("test"+(i+1))
                            .password("1234")
                            .build()
                    );
                }

                List<Member> memberForUpdate = MemberRepository.findAll();
                for(Member member:memberForUpdate){
                    Long memberId = member.getId();
                    String nickname;
                    Country country;
                    int randomIdx = new Random(System.currentTimeMillis()).nextInt(0,4);
                    switch (randomIdx){
                        case 0 -> {
                            nickname=new Faker(Locale.of("zh","CN")).name().fullName();
                            country= Country.ZH;
                        }
                        case 1 -> {
                            nickname= new Faker(Locale.of("ja","JP")).name().fullName();
                            country=Country.JA;
                        }
                        case 2 -> {
                            nickname=new Faker(Locale.of("ko","KR")).name().fullName();
                            country=Country.KO;
                        }
                        default -> {
                            nickname=new Faker(Locale.of("en","US")).name().fullName();
                            country=Country.EN;
                        }
                    }
                    MemberUpdateParam memberUpdateParam = MemberUpdateParam.builder()
                            .nickname(nickname)
                            .country(country)
                            .build();
                    memberV1Service.updateMember(memberId,memberUpdateParam,null);
                }

                long start = System.currentTimeMillis();
                List<Member> members = MemberRepository.findAll();
                Locale[] locales = {Locale.of("ko", "KR"), Locale.of("en", "US"), Locale.of("ja", "JP"), Locale.of("zh", "CN")};

                // 1. 10x10 흑백 이미지 생성
                BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_GRAY);

                // 2. 바이트 배열로 변환
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);

                byte[] imageBytes = baos.toByteArray();
                MultipartFile imageFile = new MultipartFile() {
                    @Override
                    public String getName() {
                        return "dummy";
                    }

                    @Override
                    public String getOriginalFilename() {
                        return "dummy.jpeg";
                    }

                    @Override
                    public String getContentType() {
                        return MediaType.IMAGE_JPEG_VALUE;
                    }

                    @Override
                    public boolean isEmpty() {
                        return imageBytes.length==0;
                    }

                    @Override
                    public long getSize() {
                        return imageBytes.length;
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        return imageBytes;
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ByteArrayInputStream(imageBytes);
                    }

                    @Override
                    public void transferTo(File dest) throws IOException, IllegalStateException {
                        try (FileOutputStream fos = new FileOutputStream(dest)) {
                            fos.write(imageBytes);
                        }
                    }
                };

                List<MultipartFile> dummy = new ArrayList<>();
                dummy.add(imageFile);

                for (int i = 0; i < 20; i++) {
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
                    commPostService.addCommPost(member.getId(), commPostForm, dummy);

                }

                List<CommPost> commPosts = commPostRepository.findAll();
                for(CommPost post:commPosts) {
                    int randomCount = new Random(System.currentTimeMillis()).nextInt(1, members.size());
                    for (int j = 0; j <= randomCount; j++) {
                        int memberIdx = j % members.size();
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
                Set<Long> commonPlaceList = new HashSet<>();
                for(TourPost tourPost : tourPostList){
                    commonPlaceList.add(tourPost.getCommonPlaceId());
                }

                for (Long commonPlaceId : commonPlaceList) {
                    int randomCount = new Random(System.currentTimeMillis()).nextInt(0, 6);
                    for (int j = 0; j <= randomCount; j++) {
                        ReviewForm reviewForm = ReviewForm.builder()
                                .content("테에에스트")
                                .rating(new Random(System.currentTimeMillis()).nextInt(6))
                                .build();
                        reviewService.addReview(members.get(j).getId(), commonPlaceId, reviewForm);
                    }
                }

                List<CommPost> forUpdate = commPostRepository.findAll();
                for(CommPost commPost : forUpdate){
                    Member member = commPost.getMember();
                    Double newMapX = new Random(System.currentTimeMillis()).nextDouble(126, 130);
                    Double newMapY = new Random(System.currentTimeMillis()).nextDouble(33,38);
                    CommPostForm commPostForm= CommPostForm.builder()
                            .content(commPost.getContent())
                            .mapX(newMapX)
                            .mapY(newMapY)
                            .build();
                    commPostService.updateCommPost(member.getId(),commPost.getId(),commPostForm,dummy);
                }


                 */

            }
        };
    }
}
