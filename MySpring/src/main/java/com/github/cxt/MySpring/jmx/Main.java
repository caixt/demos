package com.github.cxt.MySpring.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class Main {
	
	//-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=10001
	public static void main(String[] args) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException {
		//-Djava.rmi.server.hostname 部分需要设置这个
		Integer port = Integer.parseInt(System.getProperty("com.sun.management.jmxremote.port", "0"));
		if(port == 0){
			return ;
		}
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("cai:xiantong=report,name=Main");
		Report report = new Report();
	    mbs.registerMBean(report, name);
	    int n = 0;
	    while(n++ < 100){
	    	report.addCount(n);
	    	Thread.sleep(1000);
	    }
	}

}
