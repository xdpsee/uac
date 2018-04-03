package com.zhenhui.demo.uac.security.auth;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.JSONUtil;
import com.zhenhui.demo.uac.common.Response;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        final PrintWriter writer = response.getWriter();
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            writer.append(JSONUtil.toJsonString(Response.error(ErrorCode.ACCESS_DENIED)));
        } finally {
            writer.close();
        }
    }
}

