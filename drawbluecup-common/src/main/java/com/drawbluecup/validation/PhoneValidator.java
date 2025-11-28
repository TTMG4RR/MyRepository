package com.drawbluecup.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 具体手机号校验逻辑：11 位，且以 13/14/15/17/18/19 开头。
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(13|14|15|17|18|19)\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        return PHONE_PATTERN.matcher(trimmed).matches();
    }
}


