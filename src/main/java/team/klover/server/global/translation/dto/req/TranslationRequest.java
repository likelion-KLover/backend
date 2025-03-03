package team.klover.server.global.translation.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TranslationRequest { //번역 요청 데이터(텍스트 리스트, 타겟 언어)를 담는 DTO
    private List<String> text;
    private String target_lang;
}
