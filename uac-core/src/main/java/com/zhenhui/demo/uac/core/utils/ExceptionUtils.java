package com.zhenhui.demo.uac.core.utils;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;

public class ExceptionUtils {

    private static final String DUPLICATION_ENTRY = "Duplicate entry";


    public static boolean isDuplicateEntryException(Exception e) {

        if (e instanceof DuplicateKeyException
                || (e instanceof MyBatisSystemException && e.getMessage().contains(DUPLICATION_ENTRY))) {
            return true;
        }

        if (e.getCause() != null && e.getCause() instanceof MyBatisSystemException
                && e.getCause().getMessage().contains(DUPLICATION_ENTRY)) {
            return true;
        }

        return e.getCause() != null
                && e.getCause().getMessage().contains(DUPLICATION_ENTRY);

    }

}
