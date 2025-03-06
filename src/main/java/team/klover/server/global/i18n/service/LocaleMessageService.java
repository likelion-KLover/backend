package team.klover.server.global.i18n.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class LocaleMessageService {

    private static MessageSource messageSource;

    public LocaleMessageService(MessageSource messageSource) {
        LocaleMessageService.messageSource = messageSource;
    }

    public static String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, @Nullable Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }
}
