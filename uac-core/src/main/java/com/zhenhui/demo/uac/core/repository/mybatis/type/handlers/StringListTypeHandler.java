package com.zhenhui.demo.uac.core.repository.mybatis.type.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhenhui.demo.uac.common.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONUtil.toJsonString(strings));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {

        String value = resultSet.getString(s);

        return JSONUtil.fromJsonString(value, new TypeReference<List<String>>() {
        });
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);

        return JSONUtil.fromJsonString(value, new TypeReference<List<String>>() {
        });
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);

        return JSONUtil.fromJsonString(value, new TypeReference<List<String>>() {
        });
    }
}
