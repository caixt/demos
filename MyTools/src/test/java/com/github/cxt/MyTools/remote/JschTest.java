package com.github.cxt.MyTools.remote;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JschTest {

	//ssh -fCNL 3306:10.1.5.230:3306 root@10.1.50.130
	//navicat 的ssh 代理和我这个用的是同一个原理
	@Test
	public void portForwarding() throws JSchException, InterruptedException{
		Session session = ShellUtils.connect("root", "12345678", "10.1.50.130");
		int assinged_port = session.setPortForwardingL(3306, "10.1.5.230", 3306);
		System.out.println(assinged_port);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
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
		ShellUtils.upload("pom.xml", "pom.xml", "root", "12345678", "10.1.50.130");
	}
	
	@Test
	public void test5() throws Exception{
		ShellUtils.download("pom.xml", "pomtemp.xml", "root", "12345678", "10.1.50.130");
	}
	
	@Test
	public void test6() throws Exception{
		InputStream in = JschTest.class.getResourceAsStream("test1.sh");
		ShellUtils.upload(in, "cxt/test/test1.sh", "root", "12345678", "10.1.50.130");
		in.close();
		in = JschTest.class.getResourceAsStream("test2.sh");
		String str = IOUtils.toString(in);
		in.close();
		ShellUtils.shell(str, "root", "12345678", "10.1.50.130");
	}
	
}
