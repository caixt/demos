package com.github.cxt.springmvc.mybatis.handler;

import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import com.alibaba.fastjson.JSONArray;

@MappedTypes({ JSONArray.class })
@MappedJdbcTypes({ JdbcType.LONGVARCHAR })
public class JSONArrayHandler implements TypeHandler<JSONArray> {

	@Override
	public void setParameter(PreparedStatement ps, int i, JSONArray parameter,
			JdbcType jdbcType) throws SQLException {
		if (parameter != null) {
			String s = parameter.toJSONString();
			StringReader reader = new StringReader(s);
			ps.setCharacterStream(i, reader, s.length());
		} else {
			ps.setCharacterStream(i, null, 0);
		}
	}

	@Override
	public JSONArray getResult(ResultSet rs, String columnName)
			throws SQLException {
		String value = "";
		Clob clob = rs.getClob(columnName);
		if (clob != null) {
			int size = (int) clob.length();
			value = clob.getSubString(1, size);
		}
		return JSONArray.parseArray(value);
	}

	@Override
	public JSONArray getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		String value = "";
		Clob clob = rs.getClob(columnIndex);
		if (clob != null) {
			int size = (int) clob.length();
			value = clob.getSubString(1, size);
		}
		return JSONArray.parseArray(value);
	}

	@Override
	public JSONArray getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String value = "";
		Clob clob = cs.getClob(columnIndex);
		if (clob != null) {
			int size = (int) clob.length();
			value = clob.getSubString(1, size);
		}
		return JSONArray.parseArray(value);
	}
}
