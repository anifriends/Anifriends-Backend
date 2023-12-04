package com.clova.anifriends.domain.common.util;

import java.text.MessageFormat;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailMasker {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final int OPEN_FIRST_INDEX = 0;
    private static final int OPEN_LAST_INDEX = 4;

    public static String masking(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new CommonBadRequestException("입력값이 이메일 형식이 아닙니다.");
        }
        String subEmail = email.substring(OPEN_FIRST_INDEX, OPEN_LAST_INDEX);
        return MessageFormat.format("{0}****", subEmail);
    }
}
