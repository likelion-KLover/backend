package team.klover.server.global.translation.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Translation {
    private String detected_source_language;
    private String text;
}
