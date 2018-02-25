package com.zhenhui.demo.uac.core.repository.mybatis.type.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhenhui.demo.uac.core.dataobject.Profile;
import com.zhenhui.demo.uac.core.utils.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileTypeHandler extends BaseTypeHandler<Profile> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Profile profile, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONUtil.toJsonString(profile));
    }

    @Override
    public Profile getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSONUtil.fromJsonString(resultSet.getString(s), new TypeReference<Profile>() {
        });
    }

    @Override
    public Profile getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSONUtil.fromJsonString(resultSet.getString(i), new TypeReference<Profile>() {
        });
    }

    @Override
    public Profile getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSONUtil.fromJsonString(callableStatement.getString(i), new TypeReference<Profile>() {
        });
    }
}
