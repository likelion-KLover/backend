package team.klover.server.global.translation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.klover.server.global.translation.client.controller.ApiV1TranslateClientController;
import team.klover.server.global.translation.dto.TranslatedContent;
import team.klover.server.global.translation.dto.req.TranslationRequest;
import team.klover.server.global.translation.dto.res.Translation;
import team.klover.server.global.translation.dto.res.TranslationResponse;
import team.klover.server.global.translation.service.TranslationService;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/translate",produces =APPLICATION_JSON_VALUE )
@Tag(name = "ApiV1TranslateController",description = "Translate API") //DeepLAPI 를 호출하는 REST 컨트롤러
public class ApiV1TranslateController {
    private final TranslationService translationService;
    private final ApiV1TranslateClientController translateClientController;

    //여러 언어로 번역 요청 처리
    @PostMapping("/multi-language")
    public ResponseEntity<TranslatedContent> translateToMultiLanguages(@RequestBody String text) {
        TranslatedContent translatedContent = translationService.translateToDefaultLanguages(text);
        return ResponseEntity.ok(translatedContent);
    }

    //언어 감지 요청 처리
    @PostMapping("/detect-language")
    public ResponseEntity<String> detectLanguage(@RequestBody String text) {
        String detectedLanguage = translationService.detectLanguage(text);
        return ResponseEntity.ok(detectedLanguage);
    }

    //특정 언어로 번역 처리
    @PostMapping
    public ResponseEntity<TranslationResponse> translate(@RequestBody TranslationRequest request) {
        TranslationResponse response = translateClientController.translate(request);
        return ResponseEntity.ok(response);
    }
    // 변수를 보존하면서 번역 처리
    @PostMapping("/preserve-variables")
    public ResponseEntity<TranslationResponse> translatePreservingVariables(@RequestBody TranslationRequest request) {
        // 변수 보존 번역 로직 적용
        TranslationResponse response = new TranslationResponse();

        if (request.getText() != null && !request.getText().isEmpty()) {
            String text = request.getText().get(0);
            String translatedText = translationService.translateTextPreservingVariables(text, request.getTarget_lang());

            Translation translation = new Translation();
            translation.setText(translatedText);
            translation.setDetected_source_language("KO"); // 또는 실제 감지된 언어

            response.setTranslations(Collections.singletonList(translation));
        }

        return ResponseEntity.ok(response);
    }

    // 변수를 보존하면서 다국어 번역 엔드포인트 추가
    @PostMapping("/multi-language/preserve-variables")
    public ResponseEntity<TranslatedContent> translateToMultiLanguagesPreservingVariables(@RequestBody String text) {
        TranslatedContent translatedContent = translationService.translateToDefaultLanguagesPreservingVariables(text);
        return ResponseEntity.ok(translatedContent);
    }

    private HttpEntity<TranslationRequest> createHttpEntity(TranslationRequest request, HttpHeaders headers) {
        return new HttpEntity<>(request, headers);
    }
}
