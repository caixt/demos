package com.github.cxt.MyTools.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FtpScan {
	private Charset CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");
	private Charset CHARSET_UTF_8 = Charset.forName("UTF-8");
	private Charset CHARSET_GB2312 = Charset.forName("GB2312");
	private String ip;
	private int port;
	private String userName;
	private String password;
	private String workDir;
	private Charset charset;
	//true,主动模式. false:被动.null为自适应
	private Boolean activeMode; 
	private PathMatcher matcher;
	private FTPClient client;
	
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param userName
	 * @param password
	 * @param activeMode true,主动模式. false:被动.null为自适应（先被动再主动）
	 * @param charset 不填就自动识别,但只能处理utf-8,和gb2312的
	 * @param matchRole 不填或填*表示任意
	 * @param workDir
	 */
	public FtpScan(String ip, int port, String userName, String password, Boolean activeMode, Charset charset, String workDir, String matchRole) {
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.activeMode = activeMode;
		this.charset = charset;
		this.workDir = workDir;
		if(matchRole != null && matchRole.trim().length() > 0 && !matchRole.trim().equals("*")){
			matcher = FileSystems.getDefault().getPathMatcher("glob:" + matchRole);
		}
	}
	
	
	public List<FtpFile> scan() {
		try{
			boolean success = login();
			if(!success){
				return null;
			}
			autoCharset();
			success = cdWorkDir();
			if(!success){
				return new ArrayList<>();
			}
			List<FtpFile> files = new ArrayList<>();
			loopScan("", files);
			return files;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}finally{
			close();
		}
	}
	
	private void loopScan(String remoteDir, List<FtpFile> files) throws IOException {
		org.apache.commons.net.ftp.FTPFile[] ftpFiles = client.listFiles();
		FtpFile ftpFile = null;
		for(org.apache.commons.net.ftp.FTPFile file : ftpFiles){
			String name = file.getName();
			String path = remoteDir + formatName(file.getName());
			if(file.isFile()){
				if(matcher!= null && !matcher.matches(Paths.get(path))){
					continue;
				}
				ftpFile = new FtpFile(path, file.getTimestamp().getTime().getTime(), file.getSize());
				files.add(ftpFile);
			}
			else if(file.isDirectory()){
				if(name.equals(".") || name.equals("..")){
					continue;
				}
				if(!client.changeWorkingDirectory(name)){
					//可能没权限,不做处理
					continue;
				}
				loopScan(path + "/", files);
				if(!client.changeToParentDirectory()){
					throw new IOException("test");
				}
			}
		}
	}


	private boolean cdWorkDir() throws IOException {
		if(workDir == null || workDir.trim().equals("")){
			return true;
		}
		return client.changeWorkingDirectory(new String(workDir.getBytes(charset), CHARSET_ISO_8859_1));
	}
	
	
	private boolean login() throws IOException {
		client = new FTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		client.connect(ip, port);
		client.setConnectTimeout(30000);
		client.setDataTimeout(60000);
		boolean success = client.login(userName, password);
		if(!success){
			return false;
		}
		if(activeMode == null || !activeMode){
			client.enterLocalPassiveMode();
		}
		client.listFiles("test.info");//文件不管是否存在,只为验证返回码
		if(FTPReply.isPositiveCompletion(client.getReplyCode())){
			return true;
		}
		else if(activeMode != null){
			return false;
		}
		//自适应,再切回主动模式
		client.enterLocalActiveMode();
		client.listFiles("test.info");//文件不管是否存在,只为验证返回码
		if(FTPReply.isPositiveCompletion(client.getReplyCode())){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	private void autoCharset() throws IOException{
		if(charset == null){
			if(FTPReply.isPositiveCompletion(client.sendCommand("OPTS UTF8","ON"))){
				charset = CHARSET_UTF_8;
			}
			else {
				charset = CHARSET_GB2312;
			}
		}
	}
	
	private void close() {
		if(client != null){
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String formatName(String name){
		byte[] bytes = name.getBytes(CHARSET_ISO_8859_1);
		return new String(bytes, charset);
	}
}
