package com.github.cxt.MyTestProvider;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GoodsManager extends Remote{

	String test() throws RemoteException; 
}
