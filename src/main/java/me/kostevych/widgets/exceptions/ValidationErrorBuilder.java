package me.kostevych.widgets.exceptions;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationErrorBuilder {

    public static ValidationError fromBindingErrors(List<ObjectError> errors) {
        ValidationError error = new ValidationError("Validation failed. " + errors.size() + " error(s)");
        for (ObjectError objectError : errors) {
            error.addValidationError(objectError.getObjectName()+" "+objectError.getDefaultMessage());
        }
        return error;
    }
}
