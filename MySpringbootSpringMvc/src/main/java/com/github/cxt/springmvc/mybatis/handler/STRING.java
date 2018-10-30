package com.github.cxt.springmvc.mybatis.handler;

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