package com.github.cxt.socketio.handler;

/**
 * Created by Kosinov_KV
 * on 22.10.2015. netty-socketio-spring.
 */
public class ChatObject {

    private String userName;
    private String message;
    private String toUser;

    public ChatObject() {
    }

    public ChatObject(String userName, String message) {
        super();
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

}
