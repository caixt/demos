package com.github.cxt.MySpring.io.nio;

import java.io.IOException;
import java.io.InputStream;


public class Tools {
	

	private static final byte LF = (byte) 10; 
	private static final byte CR = (byte) 13;
	private static final String SPLIT = new String(new byte[]{CR, LF});
	
	public final static void printlt(InputStream in) throws IOException {
		byte[] bytes = new byte[1024 * 1024 * 2];
		int size = 0;
		int index = 3;
		int len = in.read(bytes, 0, bytes.length);
		while(true){
			size += len;
			boolean success = false;
			for(; index < size; index ++){
				if(
					bytes[index - 3] == bytes[index - 1] &&
					bytes[index - 1] == CR &&
					bytes[index - 2] == bytes[index - 0] &&	
					bytes[index - 0] == LF
				){
					success = true;
					break;
				}
			}
			if(success){
				break;
			}
			len = in.read(bytes, size, bytes.length - size);
		}
		String head = new String(bytes, 0, index, "utf-8");
		System.out.print("head:" + SPLIT + head);
		int contentLength = -1;
		boolean transferEncoding = false;
		for(String line : head.split(SPLIT)){
			if(line.equalsIgnoreCase("Transfer-Encoding: chunked")){
				transferEncoding = true;
			}
			else if(line.split(": ")[0].equalsIgnoreCase("content-length")){
				contentLength = Integer.parseInt(line.split(": ")[1]); 
			}
		}
		if(transferEncoding == false){
			if(contentLength >= 0){
				while(index + contentLength >= size){
					len = in.read(bytes, size, bytes.length - size);
					size += len;
				}
				String bodyStr = new String(bytes, index + 1, contentLength, "UTF-8");
				System.out.println("body:" + SPLIT + bodyStr);
			}
			else {
				while(true){
					len = in.read(bytes, size, bytes.length - size);
					if(len == -1){
						break;
					}
					size += len;
				}
				String bodyStr = new String(bytes, index + 1, size, "UTF-8");
				System.out.println("body:" + SPLIT + bodyStr);
			}
		}
		else {
			byte[] body = new byte[1024 * 1024 * 2];
			int bodySize = 0;
			while(true){
				int mark = index;
				index ++;
				while(true){
					boolean success = false;
					for(; index < size; index ++){
						if(
							bytes[index - 1] == CR &&
							bytes[index - 0] == LF
						){
							success = true;
							break;
						}
					}
					if(success){
						break;
					}
					len = in.read(bytes, size, bytes.length - size);
					size += len;
				}
				
				String str = new String(bytes, mark + 1, index - mark - 2);
				if(str.equals("")){
					continue;
				}
				int num = Integer.parseInt(str, 16);
				if(num == 0){
					break;
				}
				while(index + num>= size){
					len = in.read(bytes, size, bytes.length - size);
					size += len;
				}
				System.arraycopy(bytes, index + 1, body, bodySize, num);
				bodySize += num;
				index += num;
			}
			String bodyStr = new String(body, 0, bodySize, "UTF-8");
			System.out.println("body:" + SPLIT + bodyStr);
		}
		System.out.println("success");
	}
}
