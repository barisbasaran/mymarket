package com.mymarket.web.error;

import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.springframework.validation.BindingResult;

public interface ApplicationValidator<T> {

    Validator getValidator();

    void addCustomErrors(T object, BindingResult bindingResult);

    @SneakyThrows
    default void validate(T object, BindingResult bindingResult) {
        addCustomErrors(object, bindingResult);

        var violations = getValidator().validate(object);
        violations.forEach(violation -> {
            var field = violation.getPropertyPath().toString();
            bindingResult.rejectValue(field, "", violation.getMessage());
        });

        if (bindingResult.hasErrors()) {
            throw new ApplicationBindException(bindingResult);
        }
    }
}
