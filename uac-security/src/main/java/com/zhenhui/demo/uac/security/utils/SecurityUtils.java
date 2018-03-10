package com.zhenhui.demo.uac.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.deploy.util.StringUtils;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.security.exception.ExpiresTokenException;
import com.zhenhui.demo.uac.security.exception.InvalidTokenException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("unused")
public final class SecurityUtils {

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_SOCIAL_TYPE = "social_type";
    private static final String KEY_OPEN_ID = "open_id";
    private static final String KEY_AUTHORITIES = "roles";

    private static Algorithm algorithm;
    static  {
        try {
            algorithm = Algorithm.HMAC256("N*#.QrPW:Wk:gv~fT*3urN[^`KLQCTN$k,%fgt,gt!{ymcwKD~wbb9J+/=g6&_km");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String createToken(Principal principal, Date expireAt) throws JWTCreationException {

        return JWT.create()
                .withIssuer("top-top")
                .withIssuedAt(new Date())
                .withAudience("top-top-app")
                .withSubject(getSubject(principal))
                .withExpiresAt(expireAt)
                .withClaim(KEY_USER_ID, principal.getUserId())
                .withClaim(KEY_PHONE, principal.getPhone())
                .withClaim(KEY_SOCIAL_TYPE, (principal.getType() == null ? SocialType.NONE : principal.getType()).name())
                .withClaim(KEY_OPEN_ID, principal.getOpenId())
                .withClaim(KEY_AUTHORITIES, StringUtils.join(principal.getAuthorities(), ","))
                .sign(algorithm);
    }

    public static Principal parseToken(String token) throws InvalidTokenException, ExpiresTokenException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("top-top")
                .build();

        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            if (e instanceof TokenExpiredException) {
                throw new ExpiresTokenException("token expired!", e);
            } else {
                throw new InvalidTokenException("invalid token", e);
            }
        }

        Principal principal = new Principal();
        Map<String, Claim> claims = jwt.getClaims();
        claims.forEach((k, v) -> {
            switch (k) {
                case KEY_USER_ID:
                    principal.setUserId(v.asLong());
                    break;
                case KEY_PHONE:
                    principal.setPhone(v.asString());
                    break;
                case KEY_SOCIAL_TYPE:
                    principal.setType(SocialType.valueOf(v.asString()));
                    break;
                case KEY_OPEN_ID:
                    principal.setOpenId(v.asLong());
                    break;
                case KEY_AUTHORITIES:
                    String value= v.asString();
                    principal.setAuthorities(Arrays.asList(value.split(",")));
                    break;
                default:
                    break;
            }
        });

        return principal;
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
        principal.setOpenId(0);
        principal.setUserId(1);
        principal.setType(SocialType.NONE);
        principal.setOpenId(0);
        principal.setPhone("1340202208");
        principal.setAuthorities(Arrays.asList("USER", "ADMIN"));

        String token = SecurityUtils.createToken(principal, new Date(System.currentTimeMillis() + 12232232323L));
        System.out.println(token);
        long b = System.currentTimeMillis();
        System.out.println(b - a);

        try {
            Principal ps = SecurityUtils.parseToken(token);
            System.out.println(System.currentTimeMillis() - b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


