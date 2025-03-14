package com.mymarket.web.error;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Builder
@Value
public class ApplicationError {
    String message;
    HttpStatus httpStatus;
    int statsCode;
}
