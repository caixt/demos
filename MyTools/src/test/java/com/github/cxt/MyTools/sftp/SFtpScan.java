package com.github.cxt.MyTools.sftp;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.github.cxt.MyTools.ftp.FtpFile;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class SFtpScan {
	private String ip;
	private int port;
	private String userName;
	private String password;
	private String workDir;
	private PathMatcher matcher;
	private Session session;
	private ChannelSftp sftp;
	
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param userName
	 * @param password
	 * @param matchRole 不填或填*表示任意
	 * @param workDir
	 */
	public SFtpScan(String ip, int port, String userName, String password, String workDir, String matchRole) {
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
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
			success = cdWorkDir();
			if(!success){
				return new ArrayList<>();
			}
			List<FtpFile> files = new ArrayList<>();
			loopScan("", files);
			return files;
		}catch(JSchException | SftpException e){
			e.printStackTrace();
			return null;
		}finally{
			close();
		}
	}
	
	private void loopScan(String remoteDir, List<FtpFile> files) throws SftpException {
		Vector v = sftp.ls("*");
		FtpFile ftpFile = null;
		for(Object obj : v){
			LsEntry entry = (LsEntry) obj;
			String name = entry.getFilename();
			String path = remoteDir + name;
            if (entry.getAttrs().isReg())
            {
            	if(matcher!= null && !matcher.matches(Paths.get(path))){
					continue;
				}
				ftpFile = new FtpFile(path, (long)entry.getAttrs().getMTime() * 1000, entry.getAttrs().getSize());
				files.add(ftpFile);
            }
            else if (entry.getAttrs().isDir())
            {
            	if(name.equals(".") || name.equals("..")){
					continue;
				}
				sftp.cd(name);
				loopScan(path + "/", files);
				sftp.cd("../");
            }
            
		}
	}


	private boolean cdWorkDir() throws SftpException  {
		if(workDir == null || workDir.trim().equals("")){
			return true;
		}
		sftp.cd(workDir);
		return true;
	}
	
	
	private boolean login() throws JSchException  {
		JSch jsch = new JSch();  
    	Session session = jsch.getSession(userName, ip, port);  
        session.setPassword(password);  
          
        java.util.Properties config = new java.util.Properties();  
        config.put("StrictHostKeyChecking", "no");  
        session.setConfig(config);  
          
        session.connect();  
        
        session.setTimeout(60000);
        Channel channel = (Channel) session.openChannel("sftp");
        channel.connect();
        
        sftp = (ChannelSftp) channel;
        return true;
	}
	
	
	private void close() {
		if(sftp != null){
			sftp.disconnect();
		}
		if(session != null){
			session.disconnect();
		}
	}
}
