package com.zhenhui.demo.uac.core.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Entity implements Serializable {

    private static final long serialVersionUID = -12980864316641L;

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

}

