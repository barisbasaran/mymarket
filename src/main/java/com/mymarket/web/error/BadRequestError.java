package com.mymarket.web.error;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BadRequestError {

    String field;

    String defaultMessage;
}
