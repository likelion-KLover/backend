package team.klover.server.domain.community.comment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.global.jpa.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comm_post_id", nullable = false)
    private CommPost commPost;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likedMembers = new ArrayList<>();

    private String nickname;
    private String content;
    private Long superCommentId;
}
