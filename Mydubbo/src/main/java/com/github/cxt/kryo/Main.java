package com.github.cxt.kryo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.alibaba.dubbo.common.serialize.kryo.utils.KryoUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File file = new File("test.out");
		Kryo kryo = KryoUtils.get();
		FileOutputStream outputStream = new FileOutputStream(file);
		Demo1 demo = new Demo1();
		demo.setA("aacd");
		demo.setB(123);
		Output out = new Output(outputStream);
		kryo.writeClassAndObject(out, demo);

		out.flush();
		outputStream.close();
		
		FileInputStream input = new FileInputStream(file);
		System.out.println(kryo.readClassAndObject(new Input(input)));
		
		input.close();
		KryoUtils.release(kryo);
	}
}


