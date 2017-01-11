package com.github.cxt.MySpring.mail;

import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author cxt
 * @date:2011-11-5 下午04:39:54
 * 邮件发送
 */
public class MailTest {
	
	public static String EMAILFROM = null;
	private Session session = null;
	
	@Before
	public void befor(){
		Properties props = new Properties();
		// 建立邮箱服务器
		String from = "xiantong0216@outlook.com";
		final String password = "CAIxiantong";
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp-mail.outlook.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587"); 
		
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.host", "smtp.sina.com");
//		props.put("mail.smtp.port", "25"); 
		
		EMAILFROM = from;
		session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAILFROM, password);
			}
		});
	}
	
	@Test
	public void test1() throws AddressException, MessagingException {
		String title = Thread.currentThread().getStackTrace()[1].getMethodName();
		String content = "测试内容";
		String to = EMAILFROM;
		// 定义message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(EMAILFROM));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				to));
		message.setSubject(title);
		String body = content;
		message.setText(body);
		// 发送message
		Transport.send(message);
	}
	
	@Test
	public void test2() throws AddressException, MessagingException {
		String title = Thread.currentThread().getStackTrace()[1].getMethodName();
		String to = EMAILFROM;
		// 定义message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(EMAILFROM));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				to));
		message.setSubject(title);
		message.setContent("<span style='color:red'>中文呵呵呵</span>", "text/html;charset=utf-8");
		// 发送message
		Transport.send(message);
	}
	
	@Test
	public void test3() throws AddressException, MessagingException, IOException {
		String to = EMAILFROM;
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("\"" + MimeUtility.encodeText("牛逼人物") + "\" <" + EMAILFROM + ">"));
		msg.setSubject("送给牛逼的自己");		
		msg.setReplyTo(new Address[]{new InternetAddress(to)});
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MimeUtility.encodeText("超级牛逼人物") + " <" + to + ">"));
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		msg.setContent(msgMultipart);

		MimeBodyPart attch1 = new MimeBodyPart();		
		MimeBodyPart attch2 = new MimeBodyPart();		
		MimeBodyPart content = new MimeBodyPart();
		msgMultipart.addBodyPart(attch1);		
		msgMultipart.addBodyPart(attch2);		
		msgMultipart.addBodyPart(content);

		DataSource ds1 = new FileDataSource("README.md");
		DataHandler dh1 = new DataHandler(ds1);
		attch1.setDataHandler(dh1);
		attch1.setFileName(MimeUtility.encodeText("java培训.txt"));
		
		DataSource ds2 = new FileDataSource("schema.png");
		DataHandler dh2 = new DataHandler(ds2);
		attch2.setDataHandler(dh2);		
		attch2.setFileName("schema.png");
		
		MimeMultipart bodyMultipart = new MimeMultipart("related");
		content.setContent(bodyMultipart);
		MimeBodyPart htmlPart = new MimeBodyPart();		
		MimeBodyPart gifPart = new MimeBodyPart();		
		bodyMultipart.addBodyPart(htmlPart);
		bodyMultipart.addBodyPart(gifPart);		

		DataSource gifds = new FileDataSource(
				"mylogo.jpg"	
			);
		DataHandler gifdh = new DataHandler(gifds);		
		gifPart.setDataHandler(gifdh);
		gifPart.setHeader("Content-ID", "<IMG1>");
		
		htmlPart.setContent("这段文本内容很nb,谁能看懂他!<img src='cid:IMG1'>"
					, "text/html;charset=utf-8");
		Transport.send(msg);
//		msg.saveChanges();
//		OutputStream ips = new FileOutputStream("test.eml");
//		msg.writeTo(ips);
//		ips.close();
//		Message msg = new MimeMessage(session,new FileInputStream("test.eml"));
//		Transport.send(msg);
	}
}
