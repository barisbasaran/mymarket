package com.mymarket.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

public class ApplicationLocaleHolder {

    @Setter
    @Getter
    private static List<Locale> supportedLocales;

    public static Locale getLocale() {
        var locale = LocaleContextHolder.getLocale();
        return supportedLocales.contains(locale) ? locale : Locale.US;
    }
}
