package com.github.cxt.MyTools.sftp;

import java.util.List;

import org.junit.Test;
import com.github.cxt.MyTools.ftp.FtpFile;

public class Main {

	@Test
	public void test(){
		SFtpScan ftpScan = new SFtpScan("10.1.50.130", 22, "root", "12345678", "/root/cxt/temp", null);
		List<FtpFile> files = ftpScan.scan();
		System.out.println(files);
	}
	
}
