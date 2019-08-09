package com.github.cxt.MySpring.analysis;

import java.util.concurrent.ThreadLocalRandom;
/*
DefNew---------Default New Generation
Tenured--------Old
ParNew---------Parallel New Generation
PSYoungGen-----Parallel Scavenge
ParOldGen------Parallel Old Generation


-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseSerialGC   DefNew + Tenured    
Serial(Young) + Serial Old(Old)  新生代和老年代都用串行回收收集器     新生代用复制算法    老年代用标记-整理算法

-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParNewGC   ParNew + Tenured    
ParNew(Young) + Serial Old(Old)  启用ParNew收集器,只会影响新生代不会影响老年代     新生代用复制算法    老年代用标记-整理算法

-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParallelGC   PSYoungGen + ParOldGen 
串行收集器在新生代和老年代的并行化    -XX:MaxGCPauseMillis     新生代用复制算法    老年代用标记-整理算法

-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParallelOldGC    PSYoungGen + ParOldGen 
和上面的相同  串行收集器在新生代和老年代的并行化    -XX:MaxGCPauseMillis   新生代用复制算法    老年代用标记-整理算法

-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseConcMarkSweepGC    ParNew + CMS + Serial Old（备用)
-XX:CMSFullGCsBeforeCompaction
1.初始标记 2.并发标记 3.重新标签 4.并发清除
*/
public class GCDemo {

	public static void main(String[] args) {
		System.out.println("gc demo");
		try{
			String str = "abcfefg";
			while(true){
				str += str + ThreadLocalRandom.current().nextInt(1000) + ThreadLocalRandom.current().nextInt(10000);
				str.intern();
			}
		}catch(Throwable e){
			e.printStackTrace();
		}

	}

}
