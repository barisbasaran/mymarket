package com.mymarket.web;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class ApplicationLocaleHolder {

    public static Locale getLocale() {
        var locale = LocaleContextHolder.getLocale();
        return Locale.ENGLISH.equals(locale) ? Locale.US : locale;
    }
}
