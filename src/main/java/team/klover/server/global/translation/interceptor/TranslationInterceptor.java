package team.klover.server.global.translation.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import team.klover.server.global.translation.service.TranslationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor // API 요청에서 언어 정보를 추출( Accept-Language 헤더를 처리)
public class TranslationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Accept-Language 헤더에서 언어 정보 추출
        String language = request.getHeader("Accept-Language");

        // 기본값 설정 및 정규화
        if (language == null || language.isEmpty()) {
            language = "EN";
        } else if (language.length() >= 2) {
            // 언어 코드만 추출 (예: ko-KR -> KO)
            language = language.substring(0, 2).toUpperCase();
        }

        // 언어 정보를 ThreadLocal에 저장
        TranslationContext.setLanguage(language);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // ThreadLocal 정리
        TranslationContext.clear();
    }
}
