package com.mymarket.localization;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TranslationForm {

    @NotNull
    private List<String> keys;
}
