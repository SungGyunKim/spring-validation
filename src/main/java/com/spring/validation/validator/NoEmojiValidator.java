package com.spring.validation.validator;

import com.spring.validation.constraint.NoEmoji;
import com.vdurmont.emoji.EmojiParser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NoEmojiValidator implements ConstraintValidator<NoEmoji, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        return EmojiParser.parseToAliases(value).equals(value);
    }
}
