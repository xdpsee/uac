package com.zhenhui.demo.uac.security;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.JSONUtil;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.security.exception.ExpiresTokenException;
import com.zhenhui.demo.uac.security.exception.InvalidTokenException;
import com.zhenhui.demo.uac.security.utils.SecurityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static java.util.stream.Collectors.toList;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    // Http request header
    private static final String AUTHORIZATION = "Authorization";
    // Authorization value head
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(BEARER)) {
                final String token = authHeader.substring(BEARER.length() + 1); // The part after "Bearer "
                Principal principal = SecurityUtils.parseToken(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal
                            , null
                            , principal.getAuthorities().stream()
                            .map(GrantedAuthorityImpl::new)
                            .collect(toList()));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            handleJwtException(response, e);
            return;
        }

        chain.doFilter(request, response);
    }

    private class GrantedAuthorityImpl implements GrantedAuthority {

        private final String authority;

        public GrantedAuthorityImpl(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }

    private void handleJwtException(HttpServletResponse response, Exception e) throws RuntimeException, IOException {

        final PrintWriter writer = response.getWriter();

        try {
            if (e instanceof InvalidTokenException) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                writer.append(JSONUtil.toJsonString(Response.error(ErrorCode.TOKEN_INVALID)));
                return;
            }

            if (e instanceof ExpiresTokenException) {
                response.setCharacterEncoding("UTF-8");
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
