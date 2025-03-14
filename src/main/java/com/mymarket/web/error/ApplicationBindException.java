package com.mymarket.web.error;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class ApplicationBindException extends BindException {

    public ApplicationBindException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
