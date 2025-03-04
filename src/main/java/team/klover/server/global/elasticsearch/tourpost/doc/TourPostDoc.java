package team.klover.server.global.elasticsearch.tourpost.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.ValueConverter;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.global.util.CustomZonedDateTimeConverter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"@timestamp"})
public class TourPostDoc {
    private Long content_id;
    private Long common_place_id;
    private String first_image;
    private String addr1;
    private String title;
    private String overview;
    private GeoPoint location;
    @ValueConverter(CustomZonedDateTimeConverter.class)
    private ZonedDateTime create_date;
    private String cat1;
    private String cat2;
    private String cat3;
    private Country language;
    private Float rating_average;
    private String area_code;
    private String content_type_id;
    private Long review_count;
}
