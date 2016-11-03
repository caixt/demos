package com.github.cxt.Mynetty.protocol;
 

public class Header{
 
	private byte magic; 	// 魔数
	private MsgType msgType;	// 消息类型
	private short reserve;	// 保留字
	private short sn;		// 序列号
	private int len;		// 长度
	
	public Header() {
		super();
	}
	public Header(byte magic, MsgType msgType, short reserve, short sn,
			int len) {
		super();
		this.magic = magic;
		this.msgType = msgType;
		this.reserve = reserve;
		this.sn = sn;
		this.len = len;
	}
	public byte getMagic() {
		return magic;
	}
	public void setMagic(byte magic) {
		this.magic = magic;
	}
	public MsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
	public short getReserve() {
		return reserve;
	}
	public void setReserve(short reserve) {
		this.reserve = reserve;
	}
	public short getSn() {
		return sn;
	}
	public void setSn(short sn) {
		this.sn = sn;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
	
}
