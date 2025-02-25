package team.klover.server.domain.community.commPost.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
public class CommPost extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "commPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommPostLike> likedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "commPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommPostSave> savedMembers = new ArrayList<>();

    @Column(length = 3000)
    @Size(max = 3000)
    private String content;

    private String nickname;
    private Double mapX;
    private Double mapY;
    private String imageUrl;
}
