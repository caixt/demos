package com.github.cxt.MyJavaAgent.tracespan;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class Tracer{
	
	private static final Logger logger =  Logger.getLogger(Tracer.class.getName());
	
	private String traceId;

//  不用WeakHashMap的原因是这里类几用到了hashcode,有些程序写的好不,调用hashcode会死循环,但它自己的程序不会调用hashcode，如mybatis的springjar中存在
//	private Map<Object ,Object> attachment = new WeakHashMap<>();
	
	private List<WeakReference<Object>> attachmentKey = null;
	private List<Object> attachmentValue = null;
	ReferenceQueue<Object> attachmentQueue = new ReferenceQueue<>();

	private Span currentSpan = null;
	
	public Tracer(){
		this.traceId = createUUId();
		this.attachmentKey = new ArrayList<>();
		this.attachmentValue = new ArrayList<>();
	}
	
	private static String createUUId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public String getTraceId() {
		return traceId;
	}
	
	private Span addSpan(String name, String desc){
		Span span = new Span(createUUId(), name, desc, this.currentSpan);
		if(this.currentSpan != null && !name.equals(this.currentSpan.getName())){
			currentSpan.addChild(span);
		}
		return span;
	}
	
	public void begin(String name, String desc){
		Span span = addSpan(name, desc);
		span.start();
		this.currentSpan = span;
	}
	
	public void success(){
		stop(true);
	}
	
	public void fail(){
		stop(false);
	}
	
	private void stop(boolean success){
		if(this.currentSpan == null){
			return ;
		}
		this.currentSpan.stop(success);
		if(this.currentSpan.getParent() != null){
			this.currentSpan = this.currentSpan.getParent();
			return ;
		}
		logger.info(traceId + " stop.");
		logger.info(record(this.currentSpan).toString());
		this.currentSpan = null;
		tracerLocal.remove();
	}
	
	private StringBuilder record(Span span){
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(span.info());
		List<Span> childrens = span.getChildrens();
		int length = childrens.size();
		if(length > 0){
			sb.append(",children:[");
			for(int i = 0; i < length; i++){
				Span temp = childrens.get(i);
				sb.append("{").append(record(temp)).append("}");
				if(i != length -1){
					sb.append(",");
				}
			}
			sb.append("]");
		}
		sb.append("}");
		return sb;
	}
	
	
	public void addAttachment(Object key, Object value){
		if(key == null){
			throw new NullPointerException("key");
		}
		int length = attachmentKey.size();
		boolean hasKey = false;
		for(int i = 0; i < length; i++){
			if(key == attachmentKey.get(i)){
				hasKey = true;
				attachmentValue.remove(i);
				attachmentValue.add(i, value);
				break;
			};		
			
		}
		if(!hasKey){
			attachmentKey.add(new WeakReference<Object>(key, attachmentQueue));
			attachmentValue.add(value);
		}
	}
	
	public Object getAttachmentValue(Object key){
		if(key == null){
			throw new NullPointerException("key");
		}
		Object value = null;
		int length = attachmentKey.size();
		for(int i = 0; i < length; i++){
			if(key == attachmentKey.get(i).get()){
				value = attachmentValue.get(i);
				break;
			};			
		}
		expungeStale();
		return value;
	}
	
	private void expungeStale(){
		 for (Object x; (x = attachmentQueue.poll()) != null; ) {
			 int index = attachmentKey.indexOf(x);
			 attachmentKey.remove(index);
			 attachmentValue.remove(index);
		 }
	}

	
	private static ThreadLocal<Tracer> tracerLocal = new ThreadLocal<Tracer>();
	
	public static Tracer getTracer(){
		Tracer tracer = (Tracer)tracerLocal.get();
		if (tracer == null) {
			tracer = new Tracer();
			tracerLocal.set(tracer);
			logger.info(tracer.traceId + " generated.");
		}
		return tracer;
	}
}