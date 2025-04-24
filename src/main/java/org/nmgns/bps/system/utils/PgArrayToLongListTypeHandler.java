package org.nmgns.bps.system.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PgArrayToLongListTypeHandler extends BaseTypeHandler<List<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        Array array = ps.getConnection().createArrayOf("bigint", parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertArrayToList(rs.getArray(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertArrayToList(rs.getArray(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertArrayToList(cs.getArray(columnIndex));
    }

    private List<Long> convertArrayToList(Array array) throws SQLException {
        if (array == null) {
            return Collections.emptyList();
        }

        Object javaArray = array.getArray();
        List<Long> result = new ArrayList<>();

        if (javaArray instanceof Long[]) {
            for (Long item : (Long[]) javaArray) {
                result.add(item);
            }
        } else if (javaArray instanceof Object[]) {
            for (Object item : (Object[]) javaArray) {
                if (item != null) {
                    result.add(((Number) item).longValue());
                }
            }
        }

        return result;
    }
}