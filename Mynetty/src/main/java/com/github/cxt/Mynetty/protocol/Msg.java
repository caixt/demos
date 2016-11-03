package com.github.cxt.Mynetty.protocol;

public class Msg {
	 
	private Header header = new Header();
 	private String body;
 	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
 	
}