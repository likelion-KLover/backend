package team.klover.server.domain.tour.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Post {
    @Id
    private String contentId;
    private String title;
    private String areaCode;
    private String sigungucode;
    private String addr1;
    private String firstImage;
    private String contentTypeId;
    private String map_x;
    private String map_y;
    private String cat1;
    private String cat2;
    private String cat3;
    private String overview;
    private String cpyrhtDivCd;
    private String language;
}
