package com.github.cxt.MyTestProvider;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GoodsManagerImpl extends UnicastRemoteObject implements GoodsManager{
	private int port = 0;

	private static final long serialVersionUID = 1L;

	protected GoodsManagerImpl(int port) throws RemoteException {
		super();
		this.port = port;
	}

	public String test() {
		System.out.println("!!!!!!!!!");
		//throw new RuntimeException("test3..........");
		return "result...." + port;
	}
}
