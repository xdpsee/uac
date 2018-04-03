package com.zhenhui.demo.uac.security.auth;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.JSONUtil;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.security.exception.ExpiresTokenException;
import com.zhenhui.demo.uac.security.exception.InvalidTokenException;
import com.zhenhui.demo.uac.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    // Http request header
    private static final String AUTH_TOKEN = "token";

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        try {
            final String token = parseToken(request);
            if (token != null) {
                Principal principal = securityUtils.parseToken(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(principal);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            handleJwtException(response, e);
            return;
        }

        chain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {

        String authHeader = request.getHeader(AUTH_TOKEN);
        if (authHeader != null) {
            return authHeader;
        }

        return request.getParameter(AUTH_TOKEN);
    }

    private void handleJwtException(HttpServletResponse response, Exception e) throws RuntimeException, IOException {

        final PrintWriter writer = response.getWriter();

        try {
            if (e instanceof InvalidTokenException) {
                response.setContentType("application/json; charset=utf-8");
                writer.append(JSONUtil.toJsonString(Response.error(ErrorCode.TOKEN_INVALID)));
                return;
            }

            if (e instanceof ExpiresTokenException) {
                response.setContentType("application/json; charset=utf-8");
                writer.append(JSONUtil.toJsonString(Response.error(ErrorCode.TOKEN_EXPIRED)));
                return;
            }
        } finally {
            writer.close();
        }

        throw new RuntimeException(e);
    }
}
