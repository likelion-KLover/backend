package team.klover.server.global.translation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslatedContent { //원본 텍스트와 다양한 언어 번역 결과를 저장
    private String originalText;
    private String sourceLanguage;
    @Builder.Default
    private Map<String, String> translations = new HashMap<>();

    public void addTranslation(String languageCode, String translatedText) {
        translations.put(languageCode.toUpperCase(), translatedText);
    }
}
