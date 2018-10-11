package com.github.cxt.MyJavaAgent.javassist;

public class Demo {
	
	
	@Override
	public String toString() {
		return "haha";
	}
	
	public void test(){
		
	}
	
	public Integer abc(String str) { 
		int a = 1;
		int b = 1;
		System.out.println(a + b);
		return str.length();
	}
	
//	private Object _$PINPOINT$_interceptor4 = null;
//	public void xx() {
//		try {
//			_$PINPOINT$_interceptor4 = com.navercorp.pinpoint.bootstrap.interceptor.registry.InterceptorRegistry
//					.getInterceptor(4);
//			((com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor) _$PINPOINT$_interceptor4).before(this,
//					$args);
//		} catch (java.lang.Throwable _$PINPOINT_EXCEPTION$_) {
//			com.navercorp.pinpoint.bootstrap.interceptor.InterceptorInvokerHelper
//					.handleException(_$PINPOINT_EXCEPTION$_);
//		}
//		
//		test();
//		
//		{
//			try {
//				((com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor) _$PINPOINT$_interceptor4).after(this,
//						$args, null, $e);
//			} catch (java.lang.Throwable _$PINPOINT_EXCEPTION$_) {
//				com.navercorp.pinpoint.bootstrap.interceptor.InterceptorInvokerHelper
//						.handleException(_$PINPOINT_EXCEPTION$_);
//			}
//			throw $e;
//		}
//		
//		
//		{
//			try {
//				((com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor) _$PINPOINT$_interceptor4).after(this,
//						$args, ($w) $_, null);
//			} catch (java.lang.Throwable _$PINPOINT_EXCEPTION$_) {
//				com.navercorp.pinpoint.bootstrap.interceptor.InterceptorInvokerHelper
//						.handleException(_$PINPOINT_EXCEPTION$_);
//			}
//		}
//		
//	}
}
