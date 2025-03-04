package team.klover.server.global.translation.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.klover.server.global.translation.service.TranslationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;


@Component
@RequiredArgsConstructor // API 요청에서 언어 정보를 추출( Accept-Language 헤더를 처리)
public class TranslationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 모든 헤더 출력 (디버깅용)
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
        }
        // Accept-Language 헤더에서 언어 정보 추출
        String language = request.getHeader("Accept-Language");

        // 기본값 설정 및 정규화
        if (language == null || language.isEmpty()) {
            language = "EN";
        } else {
            // 복잡한 Accept-Language 파싱 (예: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7)
            String[] languages = language.split(",");
            if (languages.length > 0) {
                language = languages[0].split("-")[0].toUpperCase();
            }
        }


        TranslationContext.setLanguage(language);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // ThreadLocal 정리
        TranslationContext.clear();
    }
}
