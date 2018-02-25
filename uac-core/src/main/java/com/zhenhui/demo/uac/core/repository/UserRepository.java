package com.zhenhui.demo.uac.core.repository;

import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import com.zhenhui.demo.uac.core.repository.mybatis.mapper.UserMapper;
import com.zhenhui.demo.uac.core.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@CacheConfig(cacheNames = "users")
public class UserRepository {

    @Autowired
    private UserMapper userMapper;

    @Cacheable(key = "#userId")
    public User queryUser(long userId) {
        return userMapper.selectById(userId);
    }

    @Cacheable(key = "#phone")
    public User queryUser(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Caching(put = {
            @CachePut(key = "#result.id", unless = "#result == null"),
            @CachePut(key = "#result.phone", unless = "#result == null")
    })
    public User updateAvatar(long userId, String avatar) {

        int rows = userMapper.updateAvatar(userId, avatar);
        if (rows > 0) {
            return userMapper.selectById(userId);
        }

        return null;
    }

    @Caching(put = {
            @CachePut(key = "#result.id", unless = "#result == null"),
            @CachePut(key = "#result.phone", unless = "#result == null")
    })
    public User updateSecret(long userId, String secret) {
        int rows = userMapper.updateSecret(userId, secret);
        if (rows > 0) {
            return userMapper.selectById(userId);
        }

        return null;
    }

    @Caching(put = {
            @CachePut(key = "#result.id", unless = "#result == null"),
            @CachePut(key = "#result.phone", unless = "#result == null")
    })
    public User createUser(String phone, String secret) throws UserAlreadyExistsException{

        User user = new User();
        user.setPhone(phone);
        user.setSecret(secret);
        user.setNickname(phone);

        try {
            userMapper.insert(user);
            return user;
        } catch (Exception e) {
            if (!ExceptionUtils.isDuplicateEntryException(e)) {
                throw e;
            }

            throw new UserAlreadyExistsException();
        }
    }
}
