package com.github.cxt.MyTools.remote;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;  
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;  
import com.jcraft.jsch.JSchException;  
import com.jcraft.jsch.Session;

  
  
//shell - ChannelShell 
//exec - ChannelExec 
//direct-tcpip - ChannelDirectTCPIP 
//sftp - ChannelSftp 
//subsystem - ChannelSubsystem  
public class ShellUtils {  
  
      
  
    public static Session connect(String user, String passwd, String host) throws JSchException {  
    	JSch jsch = new JSch();  
    	Session session = jsch.getSession(user, host, 22);  
        session.setPassword(passwd);  
          
        java.util.Properties config = new java.util.Properties();  
        config.put("StrictHostKeyChecking", "no");  
        session.setConfig(config);  
          
        session.connect();  
        return session;
    }
    
    
    public static void test(String user, String passwd, String host) throws Exception{
    	Session session = connect(user, passwd, host);  
    	Channel channel = null;  
    	try {
    		//创建sftp通信通道
    		channel = (Channel) session.openChannel("shell");

    		channel.setInputStream(System.in);
    		channel.setExtOutputStream(System.err);
    		channel.setOutputStream(System.out);
    		channel.connect();
    		
    		 while(true){
                 if(channel.isClosed() || !(session.isConnected())){
                     break;
                 }
                 Thread.sleep(1000);
    		}
		} finally {
			session.disconnect();
			channel.disconnect();
		}
    	
    }
    
    public static void shell(String command, String user, String passwd, String host) throws Exception {  
    	Session session = connect(user, passwd, host);  
          
    	ChannelShell channel = null;  
  
        try {  
        	channel = (ChannelShell) session.openChannel("shell");  
        	
            channel.setPty(false);
            channel.setAgentForwarding(false);
        	
            InputStream in = new ByteArrayInputStream(command.getBytes("utf-8"));
            channel.setInputStream(in);  
            OutputStream out = new ByteArrayOutputStream();
            channel.setOutputStream(out);
            channel.setExtOutputStream(out);
            channel.connect();
            while (!channel.isClosed()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {
                }
            }
            System.out.println(((ByteArrayOutputStream) out).toString("utf-8"));
        } finally {    
            channel.disconnect();  
            session.disconnect();  
        }  
    } 
    
    
    public static void exec(String command, String user, String passwd, String host) throws Exception {  
    	Session session = connect(user, passwd, host);  
          
        BufferedReader reader = null;  
        ChannelExec channel = null;  
  
        try {  
            channel = (ChannelExec) session.openChannel("exec");  
            channel.setCommand(command);  
                  
            channel.setInputStream(null);  
            channel.setErrStream(System.err);  
            channel.connect();  
            InputStream in = channel.getInputStream();  
            reader = new BufferedReader(new InputStreamReader(in));  
            String buf = null;  
            while ((buf = reader.readLine()) != null) {  
                System.out.println(buf);  
            }  
        } finally {  
            try {  
                reader.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            channel.disconnect();  
            session.disconnect();  
        }  
    }
    
    public static void upload(String src, String dst, String user, String passwd, String host) throws Exception {
    	Session session = connect(user, passwd, host);  
    	ChannelSftp  channelSftp = null;
    	try{
    		channelSftp = (ChannelSftp)session.openChannel("sftp"); 
	        channelSftp.connect(); 
	        channelSftp.put(src, dst, ChannelSftp.OVERWRITE); 
	        channelSftp.quit(); 
    	}finally{
    		if(channelSftp != null){
    			channelSftp.quit(); 
    		}
    		session.disconnect();
    	}
    } 
    
    public static void download(String src, String dst, String user, String passwd, String host) throws Exception {
    	Session session = connect(user, passwd, host);  
    	ChannelSftp  channelSftp = null;
    	try{
    		channelSftp = (ChannelSftp)session.openChannel("sftp"); 
	        channelSftp.connect(); 
	        channelSftp.get(src, dst);
	        channelSftp.quit(); 
    	}finally{
    		if(channelSftp != null){
    			channelSftp.quit(); 
    		}
    		session.disconnect();
    	}
    }
}  