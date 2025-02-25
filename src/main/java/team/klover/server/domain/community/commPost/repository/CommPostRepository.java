package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Repository
public interface CommPostRepository extends JpaRepository<CommPost, Long> {
    // 사용자 위치 주변 게시글 조회
    @Query(value = """
        SELECT * FROM comm_post 
        WHERE earth_distance(ll_to_earth(:mapY, :mapX), ll_to_earth(mapy, mapx)) <= :radius
        ORDER BY create_date DESC
        """, nativeQuery = true)
    Page<CommPost> findPostsWithinRadius(@Param("mapX") Double mapX, @Param("mapY") Double mapY, @Param("radius") Integer radius, Pageable pageable);

    // 본인 게시글 조회
    Page<CommPost> findByMemberId(Long id, Pageable pageable);

    // 사용자가 저장한 게시글 조회
    @Query("SELECT p FROM CommPost p JOIN p.savedMembers sm WHERE sm.member.id = :testMemberId")
    Page<CommPost> findSavedCommPostByTestMemberId(@Param("testMemberId") Long testMemberId, Pageable pageable);

    // 사용자 닉네임 & 게시글 내용 검색
    @Query("SELECT c FROM CommPost c WHERE c.nickname LIKE %:keyword% OR c.content LIKE %:keyword%")
    Page<CommPost> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
