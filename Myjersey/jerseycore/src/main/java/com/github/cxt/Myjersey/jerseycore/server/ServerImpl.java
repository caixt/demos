package com.github.cxt.Myjersey.jerseycore.server;

import org.springframework.stereotype.Service;

@Service
public class ServerImpl implements Server{

	public String returnContent() {
		return "abcd";
	}

	
}
