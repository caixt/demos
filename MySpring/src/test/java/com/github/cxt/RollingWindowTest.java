package com.github.cxt;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RollingWindowTest {

	private static final Logger logger = LoggerFactory.getLogger(RollingWindowTest.class);

    public static final Func2<Integer, Integer, Integer> INTEGER_SUM =
            (integer, integer2) -> integer + integer2;

    public static final Func1<Observable<Integer>, Observable<Integer>> WINDOW_SUM =
            window -> window.scan(0, INTEGER_SUM).skip(3);

    public static final Func1<Observable<Integer>, Observable<Integer>> INNER_BUCKET_SUM =
            integerObservable -> integerObservable.reduce(0, INTEGER_SUM);

            
    @Test
    public void main() throws InterruptedException {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        SerializedSubject<Integer, Integer> serializedSubject = publishSubject.toSerialized();

        serializedSubject
                .window(5, TimeUnit.SECONDS) // 5秒作为一个基本块
                .flatMap(INNER_BUCKET_SUM)           // 基本块内数据求和
                .window(3, 1)              // 3个块作为一个窗口，滚动布数为1
                .flatMap(WINDOW_SUM)                 // 窗口数据求和
                .subscribe((Integer integer) ->
                        logger.info("[{}] call ...... {}", // 输出统计数据到日志
                        Thread.currentThread().getName(), integer));

        logger.info("");
        // 缓慢发送数据，观察效果
        for (int i=0; i<100; ++i) {
            if (i < 30) {
                serializedSubject.onNext(1);
            } else {
                serializedSubject.onNext(2);
            }
            Thread.sleep(1000);
        }
    }
    
    @Test
    public void test0() throws InterruptedException{
    	  PublishSubject<Integer> publishSubject = PublishSubject.create();
          SerializedSubject<Integer, Integer> serializedSubject = publishSubject.toSerialized();

          int n = 7;
          serializedSubject
                  .window(n, 1).flatMap(new Func1<Observable<Integer>, Observable<Integer>>(){

					@Override
					public Observable<Integer> call(Observable<Integer> t) {
						return t.scan(0, new Func2<Integer, Integer, Integer>(){

							@Override
							public Integer call(Integer t1, Integer t2) {
								int t = t1 + t2;
								logger.info(t1 + "!" + t2 + "!" + t);
								return t;
							}
							
							
						}).skip(n);
					}
                	  
                	  
                  }).subscribe(new Subscriber<Integer>() {
    	            @Override
    	            public void onCompleted() {

    	            }

    	            @Override
    	            public void onError(Throwable e) {

    	            }

    	            @Override
    	            public void onNext(Integer s) {
    	            	logger.info(s.toString());
    	            }
          });
          // 缓慢发送数据，观察效果
          for (int i=0; i< n * 10; ++i) {
        	  serializedSubject.onNext(i);
          }
    }
    
    
    @Test
    public void test1(){
    	List<String> list = new ArrayList<>();
    	for (int i = 0; i < 30; i++) {
    	    list.add("hello i:" + i);
    	}
    	Observable.from(list).window(3, 1).subscribe(new Subscriber<Observable<String>>() {
    	    @Override
    	    public void onCompleted() {

    	    }

    	    @Override
    	    public void onError(Throwable e) {

    	    }

    	    @Override
    	    public void onNext(Observable<String> stringObservable) {
    	        stringObservable.subscribe(new Subscriber<String>() {
    	            @Override
    	            public void onCompleted() {

    	            }

    	            @Override
    	            public void onError(Throwable e) {

    	            }

    	            @Override
    	            public void onNext(String s) {
    	            	logger.info(s);
    	            }
    	        });
    	        logger.info("next group");
    	    }
    	});
    }
    
    @Test
    public void test2(){
    	
    	Observable.just(1,2,3,4).reduce(new Func2<Integer, Integer, Integer>() {

			@Override
			public Integer call(Integer t1, Integer t2) {
				 return t1 + t2;
			}
           
        })
        .subscribe(integer -> logger.info(integer.toString()));//10
    	
    	
    	Observable.just(1,2,3,4).scan(new Func2<Integer, Integer, Integer>() {

			@Override
			public Integer call(Integer t1, Integer t2) {
				 return t1 + t2;
			}
           
        }).skip(5)
        .subscribe(integer -> logger.info(integer.toString()));//10
    }

    @Test
    public void test3() throws InterruptedException{
    	PublishSubject<Integer> publishSubject = PublishSubject.create();
        SerializedSubject<Integer, Integer> serializedSubject = publishSubject.toSerialized();
        
        Func1<Observable<Integer>, Observable<Integer>> x  = new Func1<Observable<Integer>, Observable<Integer>>() {
			
			@Override
			public Observable<Integer> call(Observable<Integer> t) {
				return t.scan(0, new Func2<Integer, Integer, Integer>(){

					@Override
					public Integer call(Integer t1, Integer t2) {
						int t = t1 + t2;
						logger.info(t1 + "!" + t2 + "!" + t);
						return t;
					}} ).skip(3);
			}
		};

        serializedSubject
                .window(5, TimeUnit.SECONDS) // 5秒作为一个基本块
                .flatMap(INNER_BUCKET_SUM)           // 基本块内数据求和
                .window(3, 1)              // 3个块作为一个窗口，滚动布数为1
                .flatMap(x)
//                .flatMap(WINDOW_SUM)                 // 窗口数据求和
                .subscribe((Integer integer) ->
                        logger.info("[{}] call ...... {}", // 输出统计数据到日志
                        Thread.currentThread().getName(), integer));

        logger.info("");
        // 缓慢发送数据，观察效果
        for (int i=0; i<100; ++i) {
            if (i < 30) {
                serializedSubject.onNext(1);
            } else {
                serializedSubject.onNext(2);
            }
            Thread.sleep(1000);
        }
    }
}

