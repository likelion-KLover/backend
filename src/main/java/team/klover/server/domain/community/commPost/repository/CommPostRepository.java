package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Repository
public interface CommPostRepository extends JpaRepository<CommPost, Long> {
    // 본인 게시글 조회
    Page<CommPost> findByTestMemberId(Long id, Pageable pageable);

    // 사용자가 저장한 게시글 조회
    @Query("SELECT p FROM CommPost p JOIN p.savedMembers sm WHERE sm.testMember.id = :testMemberId")
    Page<CommPost> findSavedCommPostByTestMemberId(Long testMemberId, Pageable pageable);


}
