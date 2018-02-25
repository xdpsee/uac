package com.zhenhui.demo.uac.core.repository.mybatis.mapper;

import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.common.SocialType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SocialAccountMapper {

    int insert(SocialAccount socialAccount);

    List<SocialAccount> selectByUserId(@Param("userId") Long userId);

    SocialAccount select(@Param("type") SocialType type, @Param("openId") long openId);

    int updateNicknameAvatar(@Param("type") SocialType type
            , @Param("openId") long openId
            , @Param("nickname") String nickname
            , @Param("avatar") String avatar);

    int updateUser(@Param("type") SocialType type
            , @Param("openId") long openId
            , @Param("originUserId") long originUserId
            , @Param("userId") long userId);
}


