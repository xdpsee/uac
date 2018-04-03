package com.zhenhui.demo.uac.security.auth;

import com.zhenhui.demo.uac.common.Principal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import static java.util.stream.Collectors.toList;

public final class TokenBasedAuthentication extends AbstractAuthenticationToken {

    private final Principal principal;

    TokenBasedAuthentication(Principal principal) {
        super(principal.getAuthorities().stream().map(GrantedAuthorityImpl::new).collect(toList()));
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public static class GrantedAuthorityImpl implements GrantedAuthority {

        private final String authority;

        public GrantedAuthorityImpl(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
}
