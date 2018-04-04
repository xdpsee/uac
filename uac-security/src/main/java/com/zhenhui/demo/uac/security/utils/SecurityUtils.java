package com.zhenhui.demo.uac.security.utils;

import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.security.exception.ExpiresTokenException;
import com.zhenhui.demo.uac.security.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public final class SecurityUtils implements InitializingBean {

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_SOCIAL_TYPE = "social_type";
    private static final String KEY_OPEN_ID = "open_id";
    private static final String KEY_AUTHORITIES = "roles";

    private Key signingKey;

    @Value("${jwt.auth.issuer}")
    private String issuer;

    @Value("${jwt.auth.expires-in}")
    private Long expiresInSeconds;

    @Value("${jwt.auth.sign-key}")
    private String signKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        signingKey = new SecretKeySpec(
                signKey.getBytes("UTF-8"), SignatureAlgorithm.HS512.getJcaName()
        );
    }

    public String createToken(Principal principal) {

        long nowMillis = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_USER_ID, principal.getUserId());
        claims.put(KEY_PHONE, principal.getPhone());
        claims.put(KEY_SOCIAL_TYPE, principal.getType());
        claims.put(KEY_OPEN_ID, principal.getOpenId());
        claims.put(KEY_AUTHORITIES, principal.getAuthorities());

        JwtBuilder builder = Jwts.builder()
                .setSubject(getSubject(principal))
                .setIssuer(issuer)
                .setIssuedAt(new Date(nowMillis))
                .setClaims(claims)
                .setExpiration(new Date(nowMillis + expiresInSeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, signingKey);

        return builder.compact();
    }

    public Principal parseToken(String token) throws InvalidTokenException, ExpiresTokenException {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .requireIssuer(issuer)
                    .parseClaimsJws(token).getBody();

            Principal principal = new Principal();

            principal.setUserId(claims.get(KEY_USER_ID, Long.class));
            principal.setPhone(claims.get(KEY_PHONE, String.class));
            principal.setType(SocialType.valueOf(claims.get(KEY_SOCIAL_TYPE, String.class)));
            principal.setOpenId(claims.get(KEY_OPEN_ID, Long.class));
            principal.setAuthorities((List<String>) claims.get(KEY_AUTHORITIES));

            return principal;
        } catch (ExpiredJwtException e) {
            throw new ExpiresTokenException("expired token", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidTokenException("invalid token", e);
        }
    }

    public String getSubject(Principal principal) {

        if (null == principal.getType() || SocialType.NONE == principal.getType()) {
            return "PH" + principal.getPhone();
        }

        switch (principal.getType()) {
            case WEIXIN:
                return "WX" + principal.getOpenId();
            case QQ:
                return "QQ" + principal.getOpenId();
            case WEIBO:
                return "WB" + principal.getOpenId();
            default:
                throw new IllegalArgumentException("No implement found for " + principal.getType().name());
        }
    }
}


