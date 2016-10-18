package com.github.cxt.MySpring.io;

import java.net.Socket;  
import java.io.*;  


public class Client {  
    public static void main(String[] args) {  
        try{  
            Socket so = new Socket("127.0.0.1",2222);  
            DataInputStream dis = new   
              DataInputStream(so.getInputStream());  
            DataOutputStream dos = new   
              DataOutputStream(so.getOutputStream());  
            InputStreamReader isr = new   
              InputStreamReader(System.in);  
            BufferedReader br = new BufferedReader(isr);  
            while(true){  
                String str = br.readLine();  
                //1 没有通知服务器  
                dos.writeUTF(str);  
                if("bye".equalsIgnoreCase(str)) break;  
                String s = dis.readUTF();  
                System.out.println(s);  
            }  
            dis.close();dos.close();  
            so.close();  
        }catch(Exception ee){  
            ee.printStackTrace();  
        }  
    }  
  
} 