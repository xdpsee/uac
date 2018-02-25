package com.zhenhui.demo.uac.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhenhui.demo.uac.common.AuthInfo;
import com.zhenhui.demo.uac.common.SocialType;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@SuppressWarnings("unused")
public enum SecurityUtils {
    INSTANCE;

    private Algorithm algorithm;

    SecurityUtils() {
        try {
            this.algorithm = Algorithm.HMAC256("N*#.QrPW:Wk:gv~fT*3urN[^`KLQCTN$k,%fgt,gt!{ymcwKD~wbb9J+/=g6&_km");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public String createToken(AuthInfo authInfo, Date expireAt) throws JWTCreationException {

        String token = JWT.create()
                .withIssuer("top-top")
                .withIssuedAt(new Date())
                .withAudience("top-top app")
                .withSubject(getSubject(authInfo))
                .withExpiresAt(expireAt)
                .withClaim(PayloadKeys.KEY_USER_ID, authInfo.getUserId())
                .withClaim(PayloadKeys.KEY_PHONE, authInfo.getPhone())
                .withClaim(PayloadKeys.KEY_SOCIAL_TYPE, (authInfo.getType() == null ? SocialType.NONE : authInfo.getType()).name())
                .withClaim(PayloadKeys.KEY_OPEN_ID, authInfo.getOpenId())
                .withClaim(PayloadKeys.KEY_IS_ADMIN, authInfo.getIsAdmin())
                .sign(this.algorithm);
        return token;

    }

    public Token parseToken(String token) throws JWTVerificationException {

        final Token result = new Token();

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("top-top")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        result.setIssuer(jwt.getIssuer());
        result.setAudience(jwt.getAudience().get(0));
        result.setSubject(jwt.getSubject());
        result.setIssuedAt(jwt.getIssuedAt());
        result.setExpiresAt(jwt.getExpiresAt());

        AuthInfo authInfo = new AuthInfo();
        Map<String, Claim> claims = jwt.getClaims();
        claims.forEach((k, v) -> {
            switch (k) {
                case PayloadKeys.KEY_USER_ID:
                    authInfo.setUserId(v.asLong());
                    break;
                case PayloadKeys.KEY_PHONE:
                    authInfo.setPhone(v.asString());
                    break;
                case PayloadKeys.KEY_SOCIAL_TYPE:
                    authInfo.setType(SocialType.valueOf(v.as(String.class)));
                    break;
                case PayloadKeys.KEY_OPEN_ID:
                    authInfo.setOpenId(v.asLong());
                    break;
                case PayloadKeys.KEY_IS_ADMIN:
                    authInfo.setIsAdmin(v.asBoolean());
                    break;
                default:
                    break;
            }
        });
        result.setAuthInfo(authInfo);

        return result;
    }


    private String getSubject(AuthInfo authInfo) {

        if (null == authInfo.getType() || SocialType.NONE == authInfo.getType()) {
            return "PH" + authInfo.getPhone();
        }

        switch (authInfo.getType()) {
            case WEIXIN:
                return "WX" + authInfo.getOpenId();
            case QQ:
                return "QQ" + authInfo.getOpenId();
            case WEIBO:
                return "WB" + authInfo.getOpenId();
            default:
                throw new IllegalArgumentException("No implement found for " + authInfo.getType().name());
        }
    }

//    public static void main(String[] args) {
//
//        for (int i = 0; i < 20; ++i) {
//            long a = System.currentTimeMillis();
//
//            AuthInfo authInfo = new AuthInfo();
//            authInfo.setOpenId(0);
//            authInfo.setUserId(1);
//            authInfo.setType(SocialType.NONE);
//            authInfo.setOpenId(0);
//            authInfo.setPhone("1340202208" + i);
//            authInfo.setIsAdmin(false);
//
//            String token = SecurityUtils.INSTANCE.createToken(authInfo, new Date(System.currentTimeMillis() + 12232232323L));
//            //System.out.println(token);
//            long b = System.currentTimeMillis();
//            System.out.println(b - a);
//
//            Token ps = SecurityUtils.INSTANCE.parseToken(token);
//            System.out.println(System.currentTimeMillis() - b);
//        }
//    }

}


