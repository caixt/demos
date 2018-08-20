package com.github.cxt.MyJavaAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;

public class LogTraceAgent {

	private static ClassLoader agentClassLoader;

	public static void premain(String agentOps, Instrumentation inst) {
		LogTraceConfig config = new LogTraceConfig(agentOps);
		try {
			agentClassLoader = new AgentClassLoad(
					ClassTransformer.class.getProtectionDomain().getCodeSource().getLocation());
			Class<?> clazz = agentClassLoader.loadClass(ClassTransformer.class.getName());
			Constructor<?> constructor = clazz.getConstructor(LogTraceConfig.class);
			ClassFileTransformer classTransformer = (ClassFileTransformer) constructor.newInstance(config);
			inst.addTransformer(classTransformer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// LogTraceConfig config = new LogTraceConfig(agentOps);
		// inst.addTransformer(new ClassTransformer(config));
	}
}
