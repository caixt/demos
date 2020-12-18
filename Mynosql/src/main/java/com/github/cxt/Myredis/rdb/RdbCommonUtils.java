package com.github.cxt.Myredis.rdb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class RdbCommonUtils {

	public static final int REDIS_STRING = 0; 			//String
	public static final int REDIS_LIST = 1;
	public static final int REDIS_SET = 2;              //Set
	public static final int REDIS_ZSET = 3;
	public static final int REDIS_HASH = 4;
	public static final int REDIS_ZSET_2 = 5;
	public static final int	REDIS_MODULE = 6;
	public static final int	REDIS_MODULE_2 = 7;
	
	public static final int REDIS_HASH_ZIPMAP = 9;
	public static final int REDIS_LIST_ZIPLIST = 10;
	public static final int REDIS_SET_INTSET = 11;
	public static final int REDIS_ZSET_ZIPLIST = 12;     //Hash
	public static final int REDIS_HASH_ZIPLIST = 13;     //SortedSet
	public static final int REDIS_LIST_QUICKLIST = 14;   //List
	public static final int	REDIS_STREAM_LISTPACKS = 15;

	/*
	 * 整型数字的编码方式
	 */
	public static final int REDIS_RDB_ENC_INT8 = 0; /* 8 bit signed integer */
	public static final int REDIS_RDB_ENC_INT16 = 1; /* 16 bit signed integer */
	public static final int REDIS_RDB_ENC_INT32 = 2; /* 32 bit signed integer */
	public static final int REDIS_RDB_ENC_LZF = 3;
	/*
	 * 表示长度的方式
	 */
	public static final int REDIS_RDB_6BITLEN = 0;
	public static final int REDIS_RDB_14BITLEN = 1;
	public static final int REDIS_RDB_32BITLEN = 0x80;
	public static final int REDIS_RDB_64BITLEN = 0x81;
	public static final int REDIS_RDB_ENCVAL = 3;

	
	public static final int REDIS_RDB_OPCODE_MODULE_AUX = 247;
	public static final int	REDIS_RDB_OPCODE_IDLE = 248;
	public static final int REDIS_RDB_OPCODE_FREQ = 249;
	public static final int REDIS_RDB_OPCODE_AUX = 250;
	public static final int REDIS_RDB_OPCODE_RESIZEDB = 251;
	
	public static final int REDIS_EXPIRETIME_MILLISECOND = 252; /* 毫秒级过期时间,占用8个字节 */
	public static final int REDIS_EXPIRETIME_SECOND = 253; /* 秒级过期时间 ,占用8个字节 */
	public static final int REDIS_SELECTDB = 254; /* 数据库 (后面紧接着的就是数据库编号) */
	public static final int REDIS_EOF = 255; /* 结束符 */

	
	public static int format(long number){
		if(number > Integer.MAX_VALUE || number < 0){
			throw new RuntimeException();
		}
		return (int) number;
	}
	
	public static Object[] read_length_with_encoding(InputStream in) {
		long length = 0;
		boolean is_encoded = false;
		int byte1 = ByteUtils.read_unsigned_char(in);
		int type = (byte1 & 0x00C0) >> 6;
		if (type == REDIS_RDB_6BITLEN) {
			length = byte1 & 0x003F;
		} else if (type == REDIS_RDB_ENCVAL) {
			is_encoded = true;
			length = byte1 & 0x003F;
		} else if (type == REDIS_RDB_14BITLEN) {
			byte byte2 = ByteUtils.read_signed_char(in);
			length = ((byte1 & 0x003F) << 8) | (byte2 & 0x00ff);
		} else if (byte1 == REDIS_RDB_32BITLEN) {
            length = ByteUtils.read_unsigned_int_be(in);
		} else if (byte1 == REDIS_RDB_64BITLEN) {
            length = ByteUtils.read_unsigned_long_be(in);
		}
		else {
			throw new RuntimeException();
		}
		return new Object[] { length, is_encoded };
	}
	
	public static long read_length(InputStream in){
		return (long) read_length_with_encoding(in)[0];
	}
	
	public static byte[] read_bytes(InputStream in) {
		Object[] lengthAndEncoded = read_length_with_encoding(in);
		long len = (Long) lengthAndEncoded[0];
		boolean encoded = (Boolean) lengthAndEncoded[1];
		if (encoded) {
			switch ((int) len) {
			case REDIS_RDB_ENC_INT8: {
				long l = ByteUtils.read_signed_char(in);
				return String.valueOf(l).getBytes();
			}
			case REDIS_RDB_ENC_INT16:{
				long l = ByteUtils.read_signed_short(in);
				return String.valueOf(l).getBytes();
			}
			case REDIS_RDB_ENC_INT32: {
				long l = ByteUtils.read_signed_int(in);
				return String.valueOf(l).getBytes();
			}
			case REDIS_RDB_ENC_LZF:{
				int clen = format(read_length(in));
				int l = format(read_length(in));
				byte[] c = ByteUtils.read(in, clen);
				byte[] s = new byte[l];
				LZFCompress.expand(c, 0, clen, s, 0, l);
				return s;
			}
			default:
				return null;
			}
		} else {
			return ByteUtils.read(in, format(len));
		}
	}
	
	public static InputStream read_bytesIO(InputStream in) {
		return new ByteArrayInputStream(read_bytes(in));
	}
	
	
	public static String read_string(InputStream in) {
		return new String(read_bytes(in), RdbParser.defaultCharset);
	}
	
}
