package team.klover.server.global.translation.client.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team.klover.server.global.exception.KloverException;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.translation.dto.req.TranslationRequest;
import team.klover.server.global.translation.dto.res.TranslationResponse;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class ApiV1TranslateClientController {
    private static final Logger logger = LoggerFactory.getLogger(ApiV1TranslateClientController.class);
    private final RestTemplate restTemplate  ;

    @Value("${translation.deepl.api-url}")
    private String deeplApiUrl;

    @Value("${translation.deepl.api-key}")
    private String authKey;

    public TranslationResponse translate(TranslationRequest request) {
        try {
            String targetLang = request.getTarget_lang();
            String apiUrl = deeplApiUrl + "?target_lang=" + targetLang;

            HttpHeaders headers = createHeaders();
            HttpEntity<TranslationRequest> httpEntity = new HttpEntity<>(request, headers);

            return restTemplate.postForObject(apiUrl, httpEntity, TranslationResponse.class);
        } catch (RestClientException e) {
            logger.error("DeepL API 통신 실패: {}", e.getMessage());
            throw new KloverException(ReturnCode.TRANSLATION_API_ERROR);
        }
    }
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "DeepL-Auth-Key " + authKey);
        return headers;
    }
}
