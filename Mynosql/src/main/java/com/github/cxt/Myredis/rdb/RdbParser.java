package com.github.cxt.Myredis.rdb;

import static com.github.cxt.Myredis.rdb.RdbCommonUtils.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;



public class RdbParser {
	public static Charset defaultCharset = Charset.forName("UTF-8");
	private String magicNumber = null;
	private int version = 0;

	private static void ERROR(String msg, Object... args) {
		throw new RuntimeException(String.format(msg, args));
	}

	public void rdbParse(File file, CallBackHandler handler) {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			rdbParseHeader(inputStream);
			rdbParseBody(inputStream, handler);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void rdbParseBody(InputStream in, CallBackHandler handler) {
		int type;
		long expiretime = 0, dbid = -1;
		String key;
		while (true) {
			type = ByteUtils.read_unsigned_char(in);
			if (type == REDIS_EXPIRETIME_SECOND) {
				expiretime = ByteUtils.read_unsigned_int(in) * 1000;
				type = ByteUtils.read_unsigned_char(in);
			} else if (type == REDIS_EXPIRETIME_MILLISECOND) {
				expiretime = ByteUtils.read_signed_long(in);
				type = ByteUtils.read_unsigned_char(in);
			}
			
			if(type == REDIS_RDB_OPCODE_IDLE){
				long idle = (long) read_length_with_encoding(in)[0];
				//System.out.println("idle!" + idle);
				type = ByteUtils.read_unsigned_char(in);
			}
			if(type == REDIS_RDB_OPCODE_FREQ){
				int freq = ByteUtils.read_unsigned_char(in);
				//System.out.println("freq!" + freq);
				type = ByteUtils.read_unsigned_char(in);
			}
			
			if (type == REDIS_SELECTDB) {
				dbid = read_length(in);
				continue;
			}
			if(type == REDIS_RDB_OPCODE_AUX){
				String aux_key = read_string(in);
				String aux_val = read_string(in);
				//System.out.println(aux_key + "!" + aux_val);
				continue;
			}
			if(type == REDIS_RDB_OPCODE_RESIZEDB){
				long db_size = read_length(in);
				long expire_size = read_length(in);
				//System.out.println(db_size + "!" + expire_size);
				continue;
			}
			if(type == REDIS_RDB_OPCODE_MODULE_AUX){
				throw new RuntimeException("TODO");
			}
			
			if (type == REDIS_EOF) {
				if(version >= 5){
					//TODO 校验码
					ByteUtils.read(in, 8);
				}
				break;
			}
			key = read_string(in);
			rdbLoadValueObject(in, type, dbid, key, expiretime, handler);
		}
	}

	private void rdbParseHeader(InputStream in) {
		this.magicNumber = new String(ByteUtils.read(in, 5));
		this.version = Integer.parseInt(new String(ByteUtils.read(in, 4)));
		if (this.version < 1 || this.version > 9) {
			ERROR("Unknown RDB format version: %d\n", this.version);
		}
	}

	public void rdbLoadValueObject(InputStream in, int type, long dbid, String key, long expiretime, CallBackHandler handler) {
		Object value = null;
		String typeCode = null;
		switch (type) {
		case REDIS_STRING : {
			value = read_string(in);
			typeCode = "String";
			break;
		}
		case REDIS_LIST: {
			long length = read_length(in);
			List<String> listValues = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				String val = read_string(in);
				listValues.add(val);
			}
			value = listValues;
			typeCode = "linkedlist";
			break;
		}
		case REDIS_SET : {
			long length = read_length(in);
			List<String> setValues = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				String val = read_string(in);
				setValues.add(val);
			}
			value = setValues;
			typeCode = "hashtable";
			break;
		}
		case REDIS_ZSET :
		case REDIS_ZSET_2 :{
			long length = read_length(in);
			TreeMap<Double, String> zsetValues = new TreeMap<Double, String>();
			for (int i = 0; i < length; i++) {
				String val = read_string(in);
				double score = (type == REDIS_ZSET_2) ? score = ByteUtils.read_binary_double(in) : read_float(in);
				zsetValues.put(score, val);
			}
			value = zsetValues;
			typeCode = "skiplist";
			break;
		}
		case REDIS_HASH : {
			long length = read_length(in);
			TreeMap<String, String> mapValues = new TreeMap<String, String>();
			for (int i = 0; i < length; i++) {
				String mapKey = read_string(in);
				String mapValue = read_string(in);
				mapValues.put(mapKey, mapValue);
			}
			value = mapValues;
			typeCode = "hashtable";
			break;
		}
		case REDIS_HASH_ZIPMAP : {
			InputStream buff = read_bytesIO(in);
			int num_entries = ByteUtils.read_unsigned_char(buff);
			TreeMap<String, String> mapValues = new TreeMap<String, String>();
			long next_length;
			while(true){
				next_length = read_zipmap_next_length(buff);
				if(next_length == -1){
					break;
				}
				String mapKey = new String(ByteUtils.read(buff, format(next_length)));
				next_length = read_zipmap_next_length(buff);
				int free = ByteUtils.read_unsigned_char(buff);
				String mapValue = new String(ByteUtils.read(buff, format(next_length)));
				mapValues.put(mapKey, mapValue);
				ByteUtils.read(buff, free);
			}
			value = mapValues;
			typeCode = "zipmap";
			break;
		}
		case REDIS_LIST_ZIPLIST : {
			InputStream buff = read_bytesIO(in);
			long zlbytes = ByteUtils.read_unsigned_int(buff);
			long tail_offset = ByteUtils.read_unsigned_int(buff);
			int num_entries = ByteUtils.read_unsigned_short(buff);
			List<Object> lists = new ArrayList<Object>();
			for(int i = 0; i < num_entries; i ++){
				lists.add(read_ziplist(buff));
			}
			checkEnd(buff);
			value = lists;
			typeCode = "ziplist";
			break;
		}
		case REDIS_SET_INTSET : {
			InputStream buff = read_bytesIO(in);
			long encoding = ByteUtils.read_unsigned_int(buff);
			long num_entries = ByteUtils.read_unsigned_int(buff);
			List<Object> values = new ArrayList<>();
			for(int i = 0; i < num_entries; i ++){
				if(encoding == 8){
					values.add(ByteUtils.read_signed_long(buff));
				}
				else if(encoding == 4){
					values.add(ByteUtils.read_signed_int(buff));
				}
				else if(encoding == 2){
					values.add(ByteUtils.read_signed_short(buff));
				}
	            else {
	            	throw new RuntimeException();
	            }
			}
			value = values;
			typeCode = "intset";
			break;
			
		} 
		case REDIS_ZSET_ZIPLIST : {
			InputStream buff = read_bytesIO(in);
			long zlbytes = ByteUtils.read_unsigned_int(buff);
			long tail_offset = ByteUtils.read_unsigned_int(buff);
			int num_entries = ByteUtils.read_unsigned_short(buff);
			int count = num_entries / 2;
			HashMap<Object, Object> hashValues = new HashMap<Object, Object>();
			for (int i = 0; i < count; i++) {
				Object hashKey = read_ziplist(buff);
				Object hashValue = read_ziplist(buff);
				if(hashValue instanceof byte[]){
					hashValue = new String((byte[])hashValue);
				}
				hashValues.put(hashKey, hashValue);
			}
			checkEnd(buff);
			value = hashValues;
			typeCode = "read_hash_from_ziplist";
			break;
		}
		case REDIS_HASH_ZIPLIST : {
			InputStream buff = read_bytesIO(in);
			long zlbytes = ByteUtils.read_unsigned_int(buff);
			long tail_offset = ByteUtils.read_unsigned_int(buff);
			int num_entries = ByteUtils.read_unsigned_short(buff);
			int count = num_entries / 2;
			HashMap<Object, Object> hashValues = new HashMap<Object, Object>();
			for (int i = 0; i < count; i++) {
				Object hashKey = read_ziplist(buff);
				Object hashValue = read_ziplist(buff);
				hashValues.put(hashKey, hashValue);
			}
			checkEnd(buff);
			value = hashValues;
			typeCode = "read_hash_from_ziplist";
			break;
		}
		case REDIS_LIST_QUICKLIST: {
			long length = read_length(in);
			List<Object> listValues = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				InputStream buff = read_bytesIO(in);
	            long zlbytes = ByteUtils.read_unsigned_int(buff);
	            long tail_offset = ByteUtils.read_unsigned_int(buff);
	            long num_entries = ByteUtils.read_unsigned_short(buff);
				for(int j = 0; j < num_entries; j++){
					listValues.add(read_ziplist(buff));
				}
				checkEnd(buff);
			}
			value = listValues;
			typeCode = "quicklist";
			break;
		}
		default :{
			throw new RuntimeException();
		}
		}
		handler.printlnHandler(dbid, typeCode, key, value, expiretime);
	}
	
	public long read_zipmap_next_length(InputStream in) {
		long num = ByteUtils.read_unsigned_char(in);
		if (num < 254) {
			return num;
		} else if (num == 254) {
			return ByteUtils.read_unsigned_int(in);
		} else {
			return -1;
		}
	}
	
	public void checkEnd(InputStream in){
		int xx = ByteUtils.read_unsigned_char(in);
		if(xx != 255){
			throw new RuntimeException();
		}
	}
	
	public static Double read_float(InputStream in) {
		int length = ByteUtils.read_unsigned_char(in);
		switch (length) {
		case 255:
			return Double.NEGATIVE_INFINITY;
		case 254:
			return Double.POSITIVE_INFINITY;
		case 253:
			return Double.NaN;
		default:
			byte[] bytes = ByteUtils.read(in, length);
			return Double.parseDouble(new String(bytes));
		}
	}
	
	public Object read_ziplist(InputStream in) {
		Object value = null;
		long prev_length = ByteUtils.read_unsigned_char(in);
		if(prev_length == 254){
			prev_length = ByteUtils.read_unsigned_int(in);
		}
		int entry_header = ByteUtils.read_unsigned_char(in);
		if ((entry_header >> 6) == 0){
            int length = entry_header & 0x3F;
            value = new String(ByteUtils.read(in, length), defaultCharset);
		}
		else if ((entry_header >> 6) == 1){
			int length = ((entry_header & 0x3F) << 8) | ByteUtils.read_unsigned_char(in);
			 value = new String(ByteUtils.read(in, length), defaultCharset);
		}
		else if ((entry_header >> 6) == 2){
//            long length = read_unsigned_int_be(file);
//            value = new byte[length];
//            if(file.read(new byte[length]) != length){
            	throw new RuntimeException();
//            }
		}
		else if  ((entry_header >> 4) == 12) {
            value = ByteUtils.read_signed_short(in);
		}
        else if ((entry_header >> 4) == 13) {
            value = ByteUtils.read_signed_int(in);
        }
        else if ((entry_header >> 4) == 14) {
            value = ByteUtils.read_signed_long(in);
        }
        else if  (entry_header == 240) {
            value = ByteUtils.read_24bit_signed_number(in);
        }
		else if (entry_header == 254){
            value = ByteUtils.read_signed_char(in);
		}
		else if ((entry_header >= 241 && entry_header <= 253)){
            value = entry_header - 241;
		}
        else {
            throw new RuntimeException();
        }
        return value;
		
	}
}
