package com.github.cxt.Myredis.rdb;

import java.io.IOException;
import java.io.InputStream;


public class ByteUtils {

	public static byte[] read(InputStream in, int len){
		byte[] bytes = new byte[len];
		try{
			if(in.read(bytes) != len){
				throw new RuntimeException();
			}
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		return bytes;
	}

	public static byte read_signed_char(InputStream in){
		byte[] bytes = read(in, 1);
		return bytes[0];
	}
	
	public static int read_unsigned_char(InputStream in){
		byte[] bytes = read(in, 1);
		return bytes[0] & 0xff;
	}
	
	public static int read_signed_short(InputStream in){
		byte[] bytes = read(in, 2);
		return  ((bytes[1] & 0xff) <<  8) |
		        ((bytes[0] & 0xff)      );
	}
	
	public static int read_unsigned_short(InputStream in){
		byte[] bytes = read(in, 2);
		return  ((bytes[1] & 0xff) <<  8) |
		        ((bytes[0] & 0xff)      );
	}
	
	public static int read_signed_int(InputStream in){
		byte[] bytes = read(in, 4);
		return ((bytes[3] & 0xff)  << 24) |
		        ((bytes[2] & 0xff) << 16) |
		        ((bytes[1] & 0xff) <<  8) |
		        ((bytes[0] & 0xff)      );
	}
	
	public static long read_unsigned_int(InputStream in){
		byte[] bytes = read(in, 4);
		return (((long)bytes[3] & 0xff) << 24) |
		       (((long)bytes[2] & 0xff) << 16) |
		       (((long)bytes[1] & 0xff) <<  8) |
		       (((long)bytes[0] & 0xff)      );
	}	
	
	public static long read_unsigned_int_be(InputStream in){
		byte[] bytes = read(in, 4);
		return (((long)bytes[0] & 0xff) << 24) |
		       (((long)bytes[1] & 0xff) << 16) |
		       (((long)bytes[2] & 0xff) <<  8) |
		       (((long)bytes[3] & 0xff)      );
	}
	
	public static long read_24bit_signed_number(InputStream in){
		byte[] bytes = read(in, 3);
		bytes = new byte[]{(byte)0, bytes[0], bytes[1], bytes[2]};
		int num = ((bytes[3] & 0xff)  << 24) |
		        ((bytes[2] & 0xff) << 16) |
		        ((bytes[1] & 0xff) <<  8) |
		        ((bytes[0] & 0xff)      );
		return num >> 8;
	}
	
	public static long read_signed_long(InputStream in){
		byte[] bytes = read(in, 8);
		return (((long)bytes[7] & 0xff) << 56) |
		       (((long)bytes[6] & 0xff) << 48) |
		       (((long)bytes[5] & 0xff) << 40) |
		       (((long)bytes[4] & 0xff) << 32) |
			   (((long)bytes[3] & 0xff) << 24) |
		       (((long)bytes[2] & 0xff) << 16) |
		       (((long)bytes[1] & 0xff) <<  8) |
		       (((long)bytes[0] & 0xff)      );
	}
	
	    
	public static long read_unsigned_long(InputStream in){
		byte[] bytes = read(in, 8);
		long l = (((long)bytes[7] & 0xff) << 56) |
		       (((long)bytes[6] & 0xff) << 48) |
		       (((long)bytes[5] & 0xff) << 40) |
		       (((long)bytes[4] & 0xff) << 32) |
			   (((long)bytes[3] & 0xff) << 24) |
		       (((long)bytes[2] & 0xff) << 16) |
		       (((long)bytes[1] & 0xff) <<  8) |
		       (((long)bytes[0] & 0xff)      );
		if(l < 0){
			throw new RuntimeException();
		}
		return l;
	}

	public static long read_unsigned_long_be(InputStream in){
		byte[] bytes = read(in, 8);
		long l = (((long)bytes[0] & 0xff) << 56) |
		       (((long)bytes[1] & 0xff) << 48) |
		       (((long)bytes[2] & 0xff) << 40) |
		       (((long)bytes[3] & 0xff) << 32) |
			   (((long)bytes[4] & 0xff) << 24) |
		       (((long)bytes[5] & 0xff) << 16) |
		       (((long)bytes[6] & 0xff) <<  8) |
		       (((long)bytes[7] & 0xff)      );
		if(l < 0){
			throw new RuntimeException();
		}
		return l;
	}
	
	public static float read_binary_float(InputStream in) {
		return Float.intBitsToFloat(read_signed_int(in));
	}
	
	public static double read_binary_double(InputStream in) {
		return Double.longBitsToDouble(read_signed_long(in));
	}

}
