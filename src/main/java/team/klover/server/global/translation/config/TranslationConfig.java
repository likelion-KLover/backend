package team.klover.server.global.translation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.klover.server.global.translation.interceptor.TranslationInterceptor;

@Configuration
@RequiredArgsConstructor
//선택적 번역 기능을 위한 설정 클래스
public class TranslationConfig implements WebMvcConfigurer {
    private final TranslationInterceptor translationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 커뮤니티와 투어/리뷰 API에만 번역 인터셉터 적용
        registry.addInterceptor(translationInterceptor)
                // 커뮤니티 게시글 및 댓글 API
                .addPathPatterns("/api/v1/comm-post/**")
                // 투어 리뷰 API
                .addPathPatterns("/api/v1/tour-post/review/**");
    }
}
