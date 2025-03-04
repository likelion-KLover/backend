package team.klover.server.global.translation.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.translation.client.controller.ApiV1TranslateClientController;
import team.klover.server.global.translation.controller.ApiV1TranslateController;
import team.klover.server.global.translation.dto.TranslatedContent;
import team.klover.server.global.translation.dto.req.TranslationRequest;
import team.klover.server.global.translation.dto.res.TranslationResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private final ApiV1TranslateClientController translateClientController;
    private static final List<String> DEFAULT_TARGET_LANGUAGES = Arrays.asList("EN", "JA", "KO", "ZH");

    //텍스트를 지정된 언어로 번역
    public String translateText(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }
        try {
            TranslationRequest request = new TranslationRequest();
            request.setText(Collections.singletonList(text));
            request.setTarget_lang(normalizeLanguageCode(targetLang));

            TranslationResponse response = translateClientController.translate(request);

            if (response != null && !response.getTranslations().isEmpty()) {
                return response.getTranslations().get(0).getText();
            }
        } catch (KloverException e) {
            logger.error("텍스트 번역 중 오류 발생: {}", e.getMessage());
        }
        return text;
    }

    //텍스트를 기본 지원 언어 모두로 번역
    public TranslatedContent translateToDefaultLanguages(String text) {
        if (text == null || text.trim().isEmpty()) {
            return TranslatedContent.builder()
                    .originalText(text)
                    .sourceLanguage("EN")
                    .build();
        }
        String detectedLanguage = detectLanguage(text);
        TranslatedContent translatedContent = TranslatedContent.builder()
                .originalText(text)
                .sourceLanguage(detectedLanguage)
                .build();
        for (String targetLang : DEFAULT_TARGET_LANGUAGES) {
            // 소스 언어와 타겟 언어가 같으면 번역 생략
            if (targetLang.equalsIgnoreCase(detectedLanguage)) {
                translatedContent.addTranslation(targetLang, text);
                continue;
            }

            String translated = translateText(text, targetLang);
            translatedContent.addTranslation(targetLang, translated);
        }

        return translatedContent;
    }

    //텍스트의 언어 감지
    public String detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "EN";
        }

        try {
            TranslationRequest request = new TranslationRequest();
            request.setText(Collections.singletonList(text));
            request.setTarget_lang("EN");

            TranslationResponse response = translateClientController.translate(request);

            if (response != null && !response.getTranslations().isEmpty()) {
                return response.getTranslations().get(0).getDetected_source_language();
            }
        } catch (KloverException e) {
            logger.error("언어 감지 중 오류 발생: {}", e.getMessage());
        }

        return "EN"; // 기본값
    }

    //언어 코드 정규화 (소문자/대문자 변환 등)
    private String normalizeLanguageCode(String langCode) {
        if (langCode == null || langCode.isEmpty()) {
            return "EN";
        }
        return langCode.toUpperCase();
    }
}
