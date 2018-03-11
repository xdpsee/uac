package com.zhenhui.demo.uac.security.utils;

import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.security.exception.ExpiresTokenException;
import com.zhenhui.demo.uac.security.exception.InvalidTokenException;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

@SuppressWarnings("unused")
public final class SecurityUtils {

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_SOCIAL_TYPE = "social_type";
    private static final String KEY_OPEN_ID = "open_id";
    private static final String KEY_AUTHORITIES = "roles";

    private static final Key signingKey = new SecretKeySpec(
            DatatypeConverter.parseBase64Binary("gkzhLvOlWsayqv8TSJMAM16C60sPd286"), SignatureAlgorithm.HS512.getJcaName()
    );


    public static String createToken(Principal principal, long ttlMillis) {

        long nowMillis = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_USER_ID, principal.getUserId());
        claims.put(KEY_PHONE, principal.getPhone());
        claims.put(KEY_SOCIAL_TYPE, principal.getType());
        claims.put(KEY_OPEN_ID, principal.getOpenId());
        claims.put(KEY_AUTHORITIES, principal.getAuthorities());

        JwtBuilder builder = Jwts.builder()
                .setSubject(getSubject(principal))
                .setIssuer("toptop.mobi")
                .setIssuedAt(new Date(nowMillis))
                .setClaims(claims)
                .setExpiration(new Date(nowMillis + ttlMillis))
                .signWith(SignatureAlgorithm.HS512, signingKey);


        return builder.compact();
    }

    public static Principal parseToken(String token) throws InvalidTokenException, ExpiresTokenException {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token).getBody();

            Principal principal = new Principal();

            principal.setUserId(claims.get(KEY_USER_ID, Long.class));
            principal.setPhone(claims.get(KEY_PHONE, String.class));
            principal.setType(SocialType.valueOf(claims.get(KEY_SOCIAL_TYPE, String.class)));
            principal.setUserId(claims.get(KEY_OPEN_ID, Long.class));
            principal.setAuthorities((List<String>) claims.get(KEY_AUTHORITIES));

            return principal;
        } catch (ExpiredJwtException e) {
            throw new ExpiresTokenException("expired token", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidTokenException("invalid token", e);
        }
    }

    public static String getSubject(Principal principal) {

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

    public static void main(String[] args) {

        long a = System.currentTimeMillis();

        Principal principal = new Principal();
        principal.setUserId(1);
        principal.setPhone("1340202208");
        principal.setType(SocialType.NONE);
        principal.setOpenId(0);
        principal.setAuthorities(Arrays.asList("USER", "ADMIN"));

        String token = SecurityUtils.createToken(principal, 12232232323L);
        System.out.println(token);
        long b = System.currentTimeMillis();
        System.out.println(b - a);

        try {
            Principal ps = SecurityUtils.parseToken(token);
            System.out.println(System.currentTimeMillis() - b);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Principal ps = SecurityUtils.parseToken("eyJhbGciOiJIUzUxMiJ9.eyJzb2NpYWxfdHlwZSI6Ik5PTkUiLCJ1c2VyX2lkIjoxLCJwaG9uZSI6IjE4NjIxODE2MjMzIiwib3Blbl9pZCI6MCwicm9sZXMiOlsiVVNFUiIsIkFETUlOIl0sImV4cCI6MTUyMjA1MDE3Mn0.X2sTB2GPunddKUCIbhoftI3dLkV7cS4i6Q8FTsk2uliqhpW8zbHss83Ya2nWT6PvXdgJvddI7XZiHM0FPBW20g");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


