package team.klover.server.global.util;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import org.springframework.stereotype.Component;
import team.klover.server.domain.member.v1.enums.Country;

@Component
public class LanguageDetect {
    private final LanguageDetector detector= LanguageDetectorBuilder
            .fromLanguages(Language.KOREAN,Language.ENGLISH,Language.CHINESE,Language.JAPANESE)
            .build();

    public Country execute(String text){
        Language result = detector.detectLanguageOf(text);

        switch (result){
            case KOREAN -> {return Country.KO;}
            case ENGLISH -> {return Country.EN;}
            case CHINESE -> {return Country.ZH;}
            default -> {return Country.JA;}
        }
    }
}
