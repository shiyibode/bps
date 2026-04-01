package org.nmgns.bps.system.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LongListToPgArrayTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<Long> parameter, JdbcType jdbcType) throws SQLException {
        // 将List<Long>转换为Long数组
        Long[] array = parameter.toArray(new Long[0]);

        // 创建PostgreSQL BIGINT数组
        Connection conn = ps.getConnection();
        Array pgArray = conn.createArrayOf("bigint", array);
        ps.setArray(i, pgArray);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getArray(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getArray(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getArray(columnIndex));
    }

    private List<Long> toList(Array array) throws SQLException {
        if (array == null) {
            return null;
        }

        // 处理PostgreSQL返回的不同类型
        Object result = array.getArray();
        if (result instanceof Long[]) {
            return Arrays.asList((Long[]) result);
        } else if (result instanceof Integer[]) {
            // 处理可能的整数转换
            return Arrays.stream((Integer[]) result)
                    .map(Integer::longValue)
                    .collect(Collectors.toList());
        } else if (result instanceof Number[]) {
            // 处理其他数字类型
            return Arrays.stream((Number[]) result)
                    .map(Number::longValue)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
