package com.github.cxt.Myjersey.jerseycore;

import java.io.IOException;
import java.net.URL;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class XmlAttackTest {

	@Test
	public void test1(){
		SAXBuilder saxBuilder = new SAXBuilder();
		URL url = Thread.currentThread().getContextClassLoader().getResource("test1.xml");
		try {
			Document doc = saxBuilder.build(url);
			String str = doc.getRootElement().getText();
			System.out.println(str);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();

		}
	}
	
	//http://www.bubuko.com/infodetail-825586.html
	@Test
	public void test2(){
		SAXBuilder saxBuilder = new SAXBuilder();
		URL url = Thread.currentThread().getContextClassLoader().getResource("test2.xml");
		try {
			Document doc = saxBuilder.build(url);
			String str = doc.getRootElement().getText();
			System.out.println(str);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();

		}
	}
	
	//http://xerces.apache.org/xerces2-j/features.html
	@Test
	public void test3(){
		SAXBuilder saxBuilder = new SAXBuilder();
		saxBuilder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//		saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//		saxBuilder.setFeature("http://xml.org/sax/features/external-general-entities", false);
//		saxBuilder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//		saxBuilder.setEntityResolver(new NoOpEntityResolver());
		URL url = Thread.currentThread().getContextClassLoader().getResource("test1.xml");
		try {
			Document doc = saxBuilder.build(url);
			String name = doc.getRootElement().getText();
			System.out.println(name);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();

		}
	}
}
