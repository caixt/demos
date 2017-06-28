package com.github.cxt.Mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({String[].class})  
@MappedJdbcTypes({JdbcType.VARCHAR})
public class STRING extends StringTokenizerTypeHandler<String> {
    public STRING() {
        super(String.class);
    }

    @Override
    String parseString(String value) {
        return value;
    }
}