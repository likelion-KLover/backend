package team.klover.server.domain.tour.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.klover.server.domain.tour.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 저장된 Apis 데이터에서 선별
    void deleteByCat3NotIn(List<String> cat3List);

    // 관광지별 개요 데이터 추가를 위해 관광지별 고유 ID 가져오기
    @Query("SELECT p.contentId FROM Post p")
    List<String> findAllContentIds();

    // 관광지별 고유 ID로 해당 관광지 데이터 가져오기
    Optional<Post> findByContentId(String contentId);
}
