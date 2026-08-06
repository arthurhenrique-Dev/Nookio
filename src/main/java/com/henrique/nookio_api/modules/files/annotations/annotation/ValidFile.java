package com.henrique.nookio_api.modules.files.annotations.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
public @interface ValidFile {

    String message() default "Arquivo inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowedTypes() default {};
}
