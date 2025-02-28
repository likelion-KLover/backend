package team.klover.server.global.elasticsearch.commpost.doc;

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
public class CommPostDoc {
    private Long member_id;
    private String content;

    @ValueConverter(CustomZonedDateTimeConverter.class)
    private ZonedDateTime create_date;
    @ValueConverter(CustomZonedDateTimeConverter.class)
    private ZonedDateTime modify_date;

    private GeoPoint location;
    private String nickname;
    private Long comment_count;
    private Long like_count;
    private Country language;
}
