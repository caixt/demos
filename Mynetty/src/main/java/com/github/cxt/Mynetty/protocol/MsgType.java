package com.github.cxt.Mynetty.protocol;


public enum MsgType {
	EMGW_LOGIN_REQ((byte) 0x00),
	EMGW_LOGIN_RES((byte) 0x01);
	
	private byte value;

	public byte getValue() {
		return value;
	}

	private MsgType(byte value) {
		this.value = value;
	}
	
	public static MsgType valueOf(byte value){
		for(MsgType type : MsgType.values()){
			if(type.getValue() == value){
				return type;
			}
		}
		return null;
	}
}
