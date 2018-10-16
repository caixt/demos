package com.github.cxt.MyJavaAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;




public class ClassTransformer implements ClassFileTransformer {
	
	private ClassAssemble classAssemble = null;
	
	public ClassTransformer(LogTraceConfig config){
		classAssemble = new ClassAssemble(config);
	}
	
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
    	return classAssemble.assembleClass(loader, className, classfileBuffer);
    }
}