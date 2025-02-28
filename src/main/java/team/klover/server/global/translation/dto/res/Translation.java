package team.klover.server.global.translation.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Translation { //단일 번역 결과(감지된 언어, 번역 텍스트)를 담는 응답 DTO
    private String detected_source_language;
    private String text;
}
