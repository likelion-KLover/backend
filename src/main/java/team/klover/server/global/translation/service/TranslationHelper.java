package team.klover.server.global.translation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslationHelper {//번역 서비스를 쉽게 사용할 수 있는 래퍼 컴포넌트
    private final TranslationService translationService;

    /**
     * 현재 요청의 언어 설정에 따라 텍스트 번역
     */
    public String translateForCurrentLanguage(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // 현재 요청의 언어 설정 가져오기
        String targetLanguage = TranslationContext.getLanguage();

        // 원본 언어 감지
        String sourceLanguage = translationService.detectLanguage(text);

        // 원본 언어와 대상 언어가 같으면 번역하지 않음
        if (sourceLanguage.equalsIgnoreCase(targetLanguage)) {
            return text;
        }

        // 대상 언어로 번역
        return translationService.translateText(text, targetLanguage);
    }


}
