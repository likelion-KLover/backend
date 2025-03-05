package team.klover.server.domain.community.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.klover.server.domain.community.comment.entity.CommentLike;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


    List<CommentLike> findAllByMember(Member member);
}
