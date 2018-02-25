package com.zhenhui.demo.uac.security;

import com.zhenhui.demo.uac.common.AuthInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Token implements Serializable {

    private static final long serialVersionUID = -123490876309752L;

    private String issuer;

    private String audience;

    private String subject;

    private Date issuedAt;

    private Date expiresAt;

    private AuthInfo authInfo = new AuthInfo();

}

