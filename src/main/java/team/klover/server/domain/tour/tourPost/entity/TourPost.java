package team.klover.server.domain.tour.tourPost.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.klover.server.domain.tour.review.entity.ReviewTourPost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class TourPost {
    @Id
    private Long contentId;
    private Long commonPlaceId;
    private String title;
    private String areaCode;
    private String sigungucode;
    private String addr1;
    private String firstImage;
    private String homepage;
    private String contentTypeId;
    private Double mapX;
    private Double mapY;
    private String cat1;
    private String cat2;
    private String cat3;
    private String overview;
    private String cpyrhtDivCd;
    private String language;

    @OneToMany(mappedBy = "tourPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TourPostSave> savedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "tourPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewTourPost> reviewTourPosts = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
}
