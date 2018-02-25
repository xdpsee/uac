package com.zhenhui.demo.uac.service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");

    public static boolean isValid(String phone) {
        Matcher matcher = MOBILE_PATTERN.matcher(phone);
        return matcher.matches();
    }


}
