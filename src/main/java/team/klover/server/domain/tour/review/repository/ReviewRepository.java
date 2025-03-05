package team.klover.server.domain.tour.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.tour.review.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 해당 관광지에 작성된 모든 리뷰 조회 (ReviewTourPost를 통해 연결)
    @EntityGraph(attributePaths = {"reviewTourPosts"})
    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN r.reviewTourPosts rtp " +
            "JOIN rtp.tourPost tp " +
            "WHERE tp.commonPlaceId = :commonPlaceId " +
            "ORDER BY r.createDate DESC")
    Page<Review> findByCommonPlaceIdOrderByCreateDateDesc(@Param("commonPlaceId") String commonPlaceId, Pageable pageable);

    // 해당 관광지의 리뷰 평점 구하기 (ReviewTourPost 경유)
    @Query("SELECT COALESCE(AVG(r.rating), 0) " +
            "FROM Review r " +
            "JOIN r.reviewTourPosts rtp " +
            "JOIN rtp.tourPost tp " +
            "WHERE tp.contentId = :tourPostContentId")
    Double findAverageRatingByTourPostId(@Param("tourPostContentId") Long tourPostContentId);

    //하나의 공통 장소에 대해 해당 유저가 작성한 리뷰를 가져온다.
    //distinct 를 사용하는 이유는 데이터가 뻥튀기 되기 때문이다.
    //저장 방식을 보면 모든 연관된 포스트에 대해 리뷰와의 관계가 맺어진다.
    //따라서 그냥 inner join을 하면 리뷰가 4배가 되어버림. 하지만 한 유저가 한 장소에 쓴 리뷰는 오직 하나뿐.
    @Query("""
    select distinct r from Review r
    join ReviewTourPost rtp on r.id = rtp.review.id
    where r.member.id = :memberId and rtp.tourPost.commonPlaceId = :commonPlaceId
    """)
    Optional<Review> findCommonPlaceReviewReviewWrittenByMember(Long memberId, Long commonPlaceId);

    List<Review> findAllByMember(Member member);

    @Query("""
select count(distinct r) from Review r
left join ReviewTourPost rtp on r.id = rtp.review.id
where rtp.tourPost.commonPlaceId = :commonPlaceId
""")
    long countTourPostReview(Long commonPlaceId);

    @Query("""
select avg(distinct r.rating) from Review r
left join ReviewTourPost rtp on r.id = rtp.review.id
where rtp.tourPost.commonPlaceId = :commonPlaceId
""")
    double getTourPostAvgRating(Long commonPlaceId);
}
