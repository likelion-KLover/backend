package team.klover.server.domain.community.commPost.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import team.klover.server.domain.member.test.entity.TestMember;
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
    @JoinColumn(name = "test_member_id", nullable = false)
    private TestMember testMember;

    @OneToMany(mappedBy = "commPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommPostLike> likedMembers = new ArrayList<>();

    private Float mapX;
    private Float mapY;

    @Column(length = 3000)
    @Size(max = 3000)
    private String content;

    private String imageUrl;
}
