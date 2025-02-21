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
    private String tourContentId;
    private String tourTitle;
    private String tourAreaCode;
    private String tourAddr1;
    private String tourFirstImage;
    private String tourContentTypeId;
    private String map_x;
    private String map_y;
    private String 
}
