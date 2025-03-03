package team.klover.server.global.translation.service;

public class TranslationContext {//요청별 번역 언어 정보를 저장하는 컨텍스트
    private static final ThreadLocal<String> languageContext = new ThreadLocal<>();

    public static void setLanguage(String language) {
        languageContext.set(language);
    }

    public static String getLanguage() {
        return languageContext.get() != null ? languageContext.get() : "EN";
    }

    public static void clear() {
        languageContext.remove();
    }
}
