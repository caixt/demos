package com.github.cxt.Myjdk8Special;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.github.cxt.Myjdk8Special.Employee.Status;


/*
 * 一、 Stream 的操作步骤
 * 
 * 1. 创建 Stream
 * 
 * 2. 中间操作
 * 
 * 3. 终止操作
 */
public class Stream1 {
	
	List<Employee> emps = Arrays.asList(
			new Employee(102, "李四", 59, 6666.66, Status.BUSY),
			new Employee(101, "张三", 18, 9999.99, Status.FREE),
			new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
			new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(105, "田七", 38, 5555.55, Status.BUSY)
	);
	
	//3. 终止操作
	/*
		allMatch——检查是否匹配所有元素
		anyMatch——检查是否至少匹配一个元素
		noneMatch——检查是否没有匹配的元素
		findFirst——返回第一个元素
		findAny——返回当前流中的任意元素
		count——返回流中元素的总个数
		max——返回流中最大值
		min——返回流中最小值
	 */
	@Test
	public void test1(){
		boolean bl = emps.stream()
			.allMatch((e) -> e.getStatus().equals(Status.BUSY));
		
		System.out.println(bl);
		
		boolean bl1 = emps.stream()
			.anyMatch((e) -> e.getStatus().equals(Status.BUSY));
		
		System.out.println(bl1);
		
		boolean bl2 = emps.stream()
			.noneMatch((e) -> e.getStatus().equals(Status.BUSY));
		
		System.out.println(bl2);
	}
	
	@Test
	public void test2(){
		List<Employee> op = emps.parallelStream()
			.filter(new Predicate<Employee>() {

				@Override
				public boolean test(Employee e) {
					return e.getStatus().equals(Status.FREE);
				}
			})
			.collect(Collectors.toList());
		System.out.println(op.size());
	}
	
	@Test
	public void test3(){
		long count = emps.stream()
						 .filter((e) -> e.getStatus().equals(Status.FREE))
						 .count();
		
		System.out.println(count);
		
		Optional<Double> op = emps.stream()
			.map(Employee::getSalary)
			.max(Double::compare);
		
		System.out.println(op.get());
		
		Optional<Employee> op2 = emps.stream()
			.min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
		
		System.out.println(op2.get());
	}
	
	@Test
	public void test4(){
		Map<Status, List<Employee>> map = emps.stream()
				.collect(Collectors.groupingBy(Employee::getStatus));
			
			System.out.println(map);
	}
	
	//分区
	@Test
	public void test5(){
		Map<Boolean, List<Employee>> map1 = emps.stream()
			.collect(Collectors.partitioningBy((e) -> e.getSalary() >= 5000));
		
		System.out.println(map1);
		
		//多级分组
		Map<Status, Map<String, List<Employee>>> map2 = emps.stream()
			.collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
				if(e.getAge() >= 60)
					return "老年";
				else if(e.getAge() >= 35)
					return "中年";
				else
					return "成年";
			})));
		
		System.out.println(map2);
	}

	
	@Test
	public void test6(){
		String str = emps.stream()
			.map(Employee::getName)
			.collect(Collectors.joining("," , "----", "----"));
		
		System.out.println(str);
	}
	
	
	@Test
	public void test7(){
		Optional<Double> sum = emps.stream()
			.map(Employee::getSalary)
			.collect(Collectors.reducing(Double::sum));
		
		System.out.println(sum.get());
	}
	
	@Test
	public void test8(){
		Optional<Integer> sum = emps.stream()
			.map(Employee::getName)
			.flatMap(Stream1::filterCharacter)
			.map((ch) -> {
				if(ch.equals('六'))
					return 1;
				else 
					return 0;
			}).reduce(Integer::sum);
		
		System.out.println(sum.get());
	}
	
	@Test
	public void test9(){
		Comparator.comparing(Employee::getName);
		
		 emps.stream()
				 .filter(distinctByKey(p -> p.getName())).forEach(System.out::println);
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	public static Stream<Character> filterCharacter(String str){
		List<Character> list = new ArrayList<>();
		
		for (Character ch : str.toCharArray()) {
			list.add(ch);
		}
		
		return list.stream();
	}
}
