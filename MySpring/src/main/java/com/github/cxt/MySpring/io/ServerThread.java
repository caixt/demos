package com.github.cxt.MySpring.io;

import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.net.Socket;  
  
public class ServerThread extends Thread{  
    private Socket so;  
    public ServerThread(Socket so){  
        this.so = so;  
    }  
    public void run(){  
        try{             
            DataInputStream dis = new   
              DataInputStream(so.getInputStream());  
            DataOutputStream dos = new   
              DataOutputStream(so.getOutputStream());  
            while(true){  
                String s = dis.readUTF();  
                if("bye".equalsIgnoreCase(s)) break;  
                dos.writeUTF("from server:"+s);  
            }  
            dis.close();dos.close();  
            so.close();  
        }catch(Exception ee){  
            ee.printStackTrace();  
        }  
    }   
}  