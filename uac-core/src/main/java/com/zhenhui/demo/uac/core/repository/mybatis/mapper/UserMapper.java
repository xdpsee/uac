package com.zhenhui.demo.uac.core.repository.mybatis.mapper;

import com.zhenhui.demo.uac.core.dataobject.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    int insert(User user);

    User selectById(@Param("userId") long userId);

    User selectByPhone(@Param("phone") String phone);

    int updateAvatar(@Param("userId") long userId, @Param("avatar") String avatar);

    int updateSecret(@Param("userId") long userId, @Param("secret") String secret);
}


