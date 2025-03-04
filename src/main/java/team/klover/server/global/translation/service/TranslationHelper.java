package team.klover.server.global.translation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.klover.server.global.translation.dto.TranslatedContent;

@Component
@RequiredArgsConstructor
public class TranslationHelper {//번역 서비스를 쉽게 사용할 수 있는 래퍼 컴포넌트
    private final TranslationService translationService;

    //현재 요청의 언어 설정에 따라 텍스트 번역
    public String translateForCurrentLanguage(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String targetLanguage = TranslationContext.getLanguage();  // 현재 요청의 언어 설정 가져오기
        String sourceLanguage = translationService.detectLanguage(text);   // 원본 언어 감지

        if (sourceLanguage.equalsIgnoreCase(targetLanguage)) {
            return text;
        }

        String translatedText = translationService.translateText(text, targetLanguage);

        return translatedText;
    }

    public TranslatedContent translateToMultipleLanguages(String text) {
        return translationService.translateToDefaultLanguages(text);
    }

}
