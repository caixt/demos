package com.github.cxt.Mybatis;


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
import com.alibaba.fastjson.JSONObject;
  

@MappedTypes({JSONObject.class})  
@MappedJdbcTypes({JdbcType.LONGVARCHAR})
public class JSONObjectHandler implements TypeHandler<JSONObject> {

	@Override
	public void setParameter(PreparedStatement ps, int i, JSONObject parameter,
			JdbcType jdbcType) throws SQLException {
		String s = parameter.toJSONString();
		StringReader reader = new StringReader(s);
	    ps.setCharacterStream(i, reader, s.length());
	}

	@Override
	public JSONObject getResult(ResultSet rs, String columnName)
			throws SQLException {
		String value = "";
	    Clob clob = rs.getClob(columnName);
	    if (clob != null) {
	      int size = (int) clob.length();
	      value = clob.getSubString(1, size);
	    }
	    return JSONObject.parseObject(value);
	}

	@Override
	public JSONObject getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		String value = "";
	    Clob clob = rs.getClob(columnIndex);
	    if (clob != null) {
	      int size = (int) clob.length();
	      value = clob.getSubString(1, size);
	    }
	    return JSONObject.parseObject(value);
	}

	@Override
	public JSONObject getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String value = "";
	    Clob clob = cs.getClob(columnIndex);
	    if (clob != null) {
	      int size = (int) clob.length();
	      value = clob.getSubString(1, size);
	    }
	    return JSONObject.parseObject(value);
	}  
}  

