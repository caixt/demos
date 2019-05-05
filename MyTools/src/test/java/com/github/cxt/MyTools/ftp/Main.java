package com.github.cxt.MyTools.ftp;

import java.util.List;
import org.junit.Test;

public class Main {
	/**
	 * yum install vsftpd -y
	 * useradd ftpuser
	 * passwd ftpuser
	 * systemctl restart vsftpd
	 * 配置文件 /etc/vsftpd/vsftpd.conf
	 * 主动配置(改成NO测试时,还是能用主动模式的,如果客户端有防火墙之类的则不可用)
	 * connect_from_port_20=YES
	 * 被动
	 * pasv_enable=YES
	 */
	@Test
	public void test(){
		FtpScan ftpScan = new FtpScan("10.1.51.83", 21, "ftpuser", "ftpuser", null, null, "/", null);
		List<FtpFile> files = ftpScan.scan();
		System.out.println(files);
	}
	
}
