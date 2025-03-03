package com.template.exam.biz.common.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringToSetTypeHandler extends BaseTypeHandler<Set<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        // Set을 콤마로 구분된 문자열로 변환
        String value = String.join(",", parameter);
        ps.setString(i, value);
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToSet(rs.getString(columnName));
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToSet(rs.getString(columnIndex));
    }

    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToSet(cs.getString(columnIndex));
    }

    private Set<String> convertToSet(String value) {
        if (value == null || value.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(value.split(",")));
    }
}
