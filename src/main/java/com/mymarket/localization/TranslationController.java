package com.mymarket.localization;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/localization/translations")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping
    public Map<String, String> translate(@Valid @RequestBody TranslationForm translationForm) {
        return translationService.translate(translationForm);
    }
}
