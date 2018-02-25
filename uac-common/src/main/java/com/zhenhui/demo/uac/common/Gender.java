package com.zhenhui.demo.uac.common;

import java.util.Arrays;

public enum Gender {
    UNKNOWN(0, "未知"),
    MAN(1, "男"),
    MALE(2, "女"),
    OTHER(3, "其他")
    ;

    public final int code;
    public final String comment;

    Gender(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static Gender valueOf(final int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
