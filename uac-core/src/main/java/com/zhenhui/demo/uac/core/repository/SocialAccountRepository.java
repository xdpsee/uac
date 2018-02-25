package com.zhenhui.demo.uac.core.repository;

import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import com.zhenhui.demo.uac.core.repository.mybatis.mapper.SocialAccountMapper;
import com.zhenhui.demo.uac.core.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@CacheConfig(cacheNames = "social_accounts")
public class SocialAccountRepository {

    @Autowired
    private SocialAccountMapper socialAccountMapper;

    @Caching(put = {
            @CachePut(key = "#account.type.name() + #account.openId", unless = "#result == null ")
    }, evict = {
            @CacheEvict(key = "#account.userId")
    })
    public SocialAccount createSocialAccount(SocialAccount account) throws UserAlreadyExistsException {

        try {
            socialAccountMapper.insert(account);
            return account;
        } catch (Exception e) {
            if (ExceptionUtils.isDuplicateEntryException(e)) {
                throw new UserAlreadyExistsException();
            }

            throw e;
        }
    }

    @Cacheable(key = "#userId")
    public List<SocialAccount> getSocialAccounts(long userId) {
        return socialAccountMapper.selectByUserId(userId);
    }

    @Cacheable(key = "#type.name() + #openId")
    public SocialAccount getSocialAccount(SocialType type, long openId) {
        return socialAccountMapper.select(type, openId);
    }

    @Caching(put = {
            @CachePut(key = "#type.name() + #openId", unless = "#result == null ")
    }, evict = {
            @CacheEvict(key = "#result.userId")
    })
    public SocialAccount updateNicknameAvatar(SocialType type
            , long openId
            , String nickname
            , String avatar) {

        int rows = socialAccountMapper.updateNicknameAvatar(type, openId, nickname, avatar);
        if (rows > 0) {
            return socialAccountMapper.select(type, openId);
        }

        return null;
    }

    @Caching(put = {
            @CachePut(key = "#type.name() + #openId", unless = "#result == null ")
    }, evict = {
            @CacheEvict(key = "#userId"),
            @CacheEvict(key = "#originUserId")
    })
    public SocialAccount updateUserId(SocialType type
            , long openId
            , long originUserId
            , long userId) {
        int rows = socialAccountMapper.updateUser(type, openId, originUserId, userId);
        if (rows > 0) {
            return socialAccountMapper.select(type, openId);
        }

        return null;
    }


}

