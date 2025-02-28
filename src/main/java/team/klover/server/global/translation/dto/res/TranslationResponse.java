package team.klover.server.global.translation.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationResponse { //번역 API 응답을 담는 DTO(번역 목록 포함)
    private List<Translation> translations;
}
