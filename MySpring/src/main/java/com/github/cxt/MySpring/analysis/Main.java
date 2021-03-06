package com.github.cxt.MySpring.analysis;

import java.util.concurrent.ThreadLocalRandom;

/*
 *  java -XshowSettings:vm -version
 *	java -XshowSettings:properties -version
 *	java -XshowSettings:locale -version
 * 初始默认值             java -XX:+PrintFlagsInitial
 * 查看修改更新        java -XX:+PrintFlagsFinal -version
 * 默认参数                 java -XX:+PrintCommandLineFlags -version
 * 查看进程的参数    jinfo -flags PID
 * 整机                           top 
 *           uptime
 * cpu       vmstat -n 2 3
 *           mpstat -P ALL 2
 *           pidstat -u 1 -p PID
 * 内存                         free -m
 *           pidstat -p PID -r 2
 * 硬盘                         df -h
 * 磁盘io     iostat -xdk 2 3
 *           pidstat -d 2 -p PID
 * 网络io     ifstat 1
 * 
 * 1.查询占cpu的线程id   ps -mp PID -o THREAD,tid,time
 * 2.耗线程的tid       printf "%x\n" tid
 * 3 jstack PID |grep tid(16进制线程id小写英文) -A60
 * 
 * 
 * 内存信息	 pmap -x PID | sort -n -k3
 * 
 * java相关命令
 * 查看gc		 jstat -gc PID 1000
 * 查看内存	 jmap -heap pid
 * dump		 jmap -dump:live,format=b,file=PID.hprof PID
 * 
 */

public class Main {

	public static void main(String[] args) {
		while(true){
			System.out.println(ThreadLocalRandom.current().nextInt(10000));
		}
	}

}
