package com.github.cxt.Mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes({String.class})  
@MappedJdbcTypes({JdbcType.BINARY})
public class UUIDTypeHandler implements TypeHandler<String> {
	/**
	 * 生成UUID
	 * @return 输出格式：c9c50468c5da4b06965a1a1aec76e7e2
	 */
	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	@Override
	public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
		byte[] value = convert(s);
		preparedStatement.setBytes(i, value);
	}

	/**
	 * 将一个UUID字符串转换为16字节数组
	 * @param str
	 * @return
	 */
	public static byte[] convert(String str) {
		if (str.length() == 36)
			str = str.replaceAll("-", "");
		if (str.length() != 32)
			throw new IllegalArgumentException("uuid string length must be 32, invalid uuid:" + str);
		byte[] bytes = new byte[16];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, (i + 1) * 2), 16));
		}
		return bytes;
	}

	/**
	 * 将一个16字节数组转换为UUID字符串
	 * @param bytes
	 * @return
	 */
	public static String convert(byte[] bytes) {
		if (bytes == null)
			return null;
		else if (bytes.length != 16)
			throw new IllegalArgumentException("uuid bytes length must be 16");
		else {
			StringBuilder sb = new StringBuilder(32);
			for (byte b : bytes) {
				String t = Integer.toHexString(0xff & b);
				if (t.length() == 1)
					sb.append("0");
				sb.append(t);
			}
			return sb.toString();
		}
	}

	@Override
	public String getResult(ResultSet resultSet, String s) throws SQLException {
		return convert(resultSet.getBytes(s));
	}

	@Override
	public String getResult(ResultSet resultSet, int i) throws SQLException {
		return convert(resultSet.getBytes(i));
	}

	@Override
	public String getResult(CallableStatement callableStatement, int i) throws SQLException {
		return convert(callableStatement.getBytes(i));
	}
}