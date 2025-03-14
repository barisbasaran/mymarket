package com.mymarket.localization;

import com.mymarket.web.ApplicationLocaleHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationService {

    private final MessageSource messageSource;

    public Map<String, String> translate(TranslationForm translationForm) {
        var locale = ApplicationLocaleHolder.getLocale();
        Map<String, String> translations = new HashMap<>();
        translationForm.getKeys().forEach(key -> {
            try {
                translations.put(key, messageSource.getMessage(key, null, locale));
            } catch (NoSuchMessageException e) {
                log.error(e.getMessage());
            }
        });
        return translations;
    }

    public String translate(String key) {
        return messageSource.getMessage(key, null, ApplicationLocaleHolder.getLocale());
    }

    public String translate(String key, String[] args) {
        return messageSource.getMessage(key, args, ApplicationLocaleHolder.getLocale());
    }
}
