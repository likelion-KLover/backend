package team.klover.server.domain.community.commPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Repository
public interface CommPostRepository extends JpaRepository<CommPost, Long> {
    Page<CommPost> findByTestMemberId(Long id, Pageable pageable);
}
