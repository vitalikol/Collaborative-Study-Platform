package com.vitalioleksenko.csp.util.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorBuilder {
    public static String fromBindingErrors(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        return errorMsg.toString();
    }
}
