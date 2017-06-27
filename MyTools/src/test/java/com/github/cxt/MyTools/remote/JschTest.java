package com.github.cxt.MyTools.remote;

import org.junit.Test;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JschTest {

	@Test
	public void test1() throws JSchException{
		Session session = ShellUtils.connect("root", "12345678", "10.1.50.130");
		session.disconnect();
		System.out.println("connect true");
	}
	
	@Test
	public void test2() throws Exception{
		ShellUtils.shell("echo aaaa \n lll\necho bbbb\nexit\n", "root", "12345678", "10.1.50.130");
	}
	
	@Test
	public void test3() throws Exception{
		ShellUtils.exec("cat /root/server/openAPI.yaml", "root", "12345678", "10.1.50.130");
	}
	
	@Test
	public void test4() throws Exception{
		ShellUtils.upload("pom.xml","pom.xml", "root", "12345678", "10.1.50.130");
	}
	
	@Test
	public void test5() throws Exception{
		ShellUtils.download("pom.xml", "pomtemp.xml", "root", "12345678", "10.1.50.130");
	}
	
}
