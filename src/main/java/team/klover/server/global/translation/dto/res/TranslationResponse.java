package team.klover.server.global.translation.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationResponse {
    private List<Translation> translations;
}
