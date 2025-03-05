package team.klover.server.global.translation.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.translation.client.controller.ApiV1TranslateClientController;
import team.klover.server.global.translation.dto.TranslatedContent;
import team.klover.server.global.translation.dto.req.TranslationRequest;
import team.klover.server.global.translation.dto.res.TranslationResponse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private final ApiV1TranslateClientController translateClientController;
    private static final List<String> DEFAULT_TARGET_LANGUAGES = Arrays.asList("EN", "JA", "KO", "ZH");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{([^}]+)}");

    // 텍스트를 지정된 언어로 번역
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

    // 텍스트의 언어 감지
    public String detectLanguage(String text) {
        logger.info("Detecting language for text: {}", text);

        if (text == null || text.trim().isEmpty()) {
            logger.warn("Empty text provided for language detection");
            return "EN";
        }

        try {
            TranslationRequest request = new TranslationRequest();
            request.setText(Collections.singletonList(text));
            request.setTarget_lang("EN");

            logger.info("Translation request: {}", request);

            TranslationResponse response = translateClientController.translate(request);

            logger.info("Translation response: {}", response);

            if (response != null && !response.getTranslations().isEmpty()) {
                String detectedLanguage = response.getTranslations().get(0).getDetected_source_language();
                logger.info("Detected language: {}", detectedLanguage);
                return detectedLanguage;
            }
        } catch (KloverException e) {
            logger.error("언어 감지 중 오류 발생: {}", e.getMessage(), e);
        }

        return "EN"; // 기본값
    }

    // 언어 코드 정규화 (소문자/대문자 변환 등)
    private String normalizeLanguageCode(String langCode) {
        if (langCode == null || langCode.isEmpty()) {
            return "EN";
        }
        return langCode.toUpperCase();
    }

    // 텍스트를 기본 지원 언어 모두로 번역
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

    // 변수를 보존하는 번역 메서드
    public String translateTextPreservingVariables(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        try {
            // 1. 변수 추출 및 임시 플레이스홀더 치환
            Map<String, String> variableMap = new HashMap<>();
            String processedText = extractAndReplaceVariables(text, variableMap);

            // 2. 플레이스홀더가 있는 텍스트 번역
            TranslationRequest request = new TranslationRequest();
            request.setText(Collections.singletonList(processedText));
            request.setTarget_lang(normalizeLanguageCode(targetLang));

            TranslationResponse response = translateClientController.translate(request);

            // 3. 번역된 텍스트에 원래 변수 복원
            if (response != null && !response.getTranslations().isEmpty()) {
                String translatedText = response.getTranslations().get(0).getText();
                return restoreVariables(translatedText, variableMap);
            }
        } catch (KloverException e) {
            logger.error("변수 보존 텍스트 번역 중 오류 발생: {}", e.getMessage());
        }
        return text;
    }

    // 변수를 추출하고 임시 플레이스홀더로 치환하는 헬퍼 메서드
    private String extractAndReplaceVariables(String text, Map<String, String> variableMap) {
        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        int index = 0;
        while (matcher.find()) {
            String variable = matcher.group(0); // {receiverNickname} 형태
            String placeholder = "##VAR_" + index + "##";
            variableMap.put(placeholder, variable);
            matcher.appendReplacement(buffer, placeholder);
            index++;
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    // 번역된 텍스트에 원래 변수를 복원하는 헬퍼 메서드
    private String restoreVariables(String translatedText, Map<String, String> variableMap) {
        String result = translatedText;
        for (Map.Entry<String, String> entry : variableMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    // 변수를 보존하면서 기본 지원 언어로 모두 번역
    public TranslatedContent translateToDefaultLanguagesPreservingVariables(String text) {
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
            if (targetLang.equalsIgnoreCase(detectedLanguage)) {
                translatedContent.addTranslation(targetLang, text);
                continue;
            }

            String translated = translateTextPreservingVariables(text, targetLang);
            translatedContent.addTranslation(targetLang, translated);
        }

        return translatedContent;
    }
}
