package team.klover.server.global.translation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final ApiV1TranslateController translateController;


    private static final List<String> DEFAULT_TARGET_LANGUAGES = Arrays.asList("EN", "JA", "KO", "ZH");


    public String translateText(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        TranslationRequest request = new TranslationRequest();
        request.setText(Collections.singletonList(text));
        request.setTarget_lang(targetLang);

        TranslationResponse response = translateController.translate(request).getBody();

        if (response != null && !response.getTranslations().isEmpty()) {
            return response.getTranslations().get(0).getText();
        }

        return text;
    }


    public TranslatedContent translateToDefaultLanguages(String text) {
        TranslatedContent translatedContent = new TranslatedContent();
        translatedContent.setOriginalText(text);

        // Get source language from first translation
        String detectedLanguage = detectLanguage(text);
        translatedContent.setSourceLanguage(detectedLanguage);



        if (text == null || text.trim().isEmpty()) {
            return translatedContent;
        }

        // Translate to each target language
        for (String targetLang : DEFAULT_TARGET_LANGUAGES) {
            // Skip if target is same as source
            if (targetLang.equalsIgnoreCase(detectedLanguage)) {
                translatedContent.addTranslation(targetLang, text);
                continue;
            }

            String translated = translateText(text, targetLang);
            translatedContent.addTranslation(targetLang, translated);
        }

        return translatedContent;
    }


    public String detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "EN"; // Default to English for empty text
        }

        // Use DeepL to detect language by translating to English
        TranslationRequest request = new TranslationRequest();
        request.setText(Collections.singletonList(text));
        request.setTarget_lang("EN");

        TranslationResponse response = translateController.translate(request).getBody();

        if (response != null && !response.getTranslations().isEmpty()) {
            return response.getTranslations().get(0).getDetected_source_language();
        }

        return "EN"; // Default to English if detection fails
    }
}
