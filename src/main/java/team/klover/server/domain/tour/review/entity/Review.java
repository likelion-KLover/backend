package team.klover.server.domain.tour.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import team.klover.server.domain.member.test.entity.TestMember;
import team.klover.server.domain.tour.tourPost.entity.TourPost;
import team.klover.server.global.jpa.BaseEntity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Review extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_member_id", nullable = false)
    private TestMember testMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_post_contentid", nullable = false)
    private TourPost tourPost;

    @Column(length = 1000)
    @Size(max = 1000)
    private String content;

    @Column(length = 1)
    @Size(max = 1)
    private int rating;
}
