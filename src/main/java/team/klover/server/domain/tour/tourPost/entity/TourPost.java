package team.klover.server.domain.tour.tourPost.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TourPost {
    @Id
    private String contentId;
    private String title;
    private String areaCode;
    private String sigungucode;
    private String addr1;
    private String firstImage;
    private String homepage;
    private String contentTypeId;
    private String mapX;
    private String mapY;
    private String cat1;
    private String cat2;
    private String cat3;
    private String overview;
    private String cpyrhtDivCd;
    private String language;

    @OneToMany(mappedBy = "tourPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourPostSave> savedMembers = new ArrayList<>();
}
