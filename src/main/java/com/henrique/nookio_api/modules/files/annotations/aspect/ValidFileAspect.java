package com.henrique.nookio_api.modules.files.annotations.aspect;

import com.henrique.nookio_api.modules.files.annotations.annotation.ValidFile;
import com.henrique.nookio_api.modules.files.annotations.facade.ValidateFilesFacade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidFileAspect
        implements ConstraintValidator<ValidFile, MultipartFile> {

    private String[] allowedTypes;
    private final ValidateFilesFacade facade;


    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return facade.facade(value, allowedTypes);
    }
}
