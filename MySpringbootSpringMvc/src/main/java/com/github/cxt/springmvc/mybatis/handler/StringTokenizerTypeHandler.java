package com.github.cxt.springmvc.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将逗号分隔的字符串和数组对象之间的进行类型转换的抽象类
 */
public abstract class StringTokenizerTypeHandler<T> extends BaseTypeHandler<T[]> {
    private Class<T> clazz;

    public StringTokenizerTypeHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T[] ts, JdbcType jdbcType) throws SQLException {
        StringBuffer result = new StringBuffer();
        for (T value : ts) {
            result.append(value).append(",");
        }
        result.deleteCharAt(result.length() - 1);
        ps.setString(i, result.toString());
    }

    @Override
    public T[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return toArray(resultSet.getString(columnName));
    }

    @Override
    public T[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return toArray(resultSet.getString(columnIndex));
    }

    @Override
    public T[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return toArray(callableStatement.getString(columnIndex));
    }

    T[] toArray(String columnValue) {
        if (columnValue == null) {
            return createArray(0);
        }
        String[] values = columnValue.split(",");
        T[] array = createArray(values.length);
        for (int i = 0; i < values.length; i++) {
            array[i] = parseString(values[i]);
        }
        return array;
    }

    T[] createArray(int size) {
        return (T[]) Array.newInstance(clazz, size);
    }

    abstract T parseString(String value);

}