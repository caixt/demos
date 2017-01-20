package com.github.cxt.Myyaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;



public class Main {
	
	private static File temp = new File("tempp.yml");
	
	@Test
	public void test1() throws FileNotFoundException {
		Yaml yaml = new Yaml();
		Reader reader =  new InputStreamReader(new FileInputStream(temp), Charset.forName("utf-8"));
		Map map = yaml.loadAs(reader, Map.class);
		System.out.println(map.keySet().size());
	}
	
	@Test
	public void test2() throws IOException {
		DumperOptions options = new DumperOptions();
	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	    Yaml yaml = new Yaml(options);
	    
	    YamlData data = new YamlData();
	    data.setNum_integer(1);
	    data.setNum_float(2.0f);
	    data.setNum_long(3l);
	    data.setNum_double(4.0);
	    data.setMessage("message\nmessage\n\n\n");
	    Map<String, Object> map = new HashMap<>();
	    map.put("k1", 1);
	    map.put("k2", "1abcd");
	    map.put("k3", "12345678");
	    data.setMap(map);
	    List<Object> list = new ArrayList<>();
	    list.addAll(map.values());
	    data.setList(list);
	    data.setSubData(new YamlSubData("中文名称", "code11111"));
	    
	    Writer fw = new OutputStreamWriter(new FileOutputStream(temp), "utf-8");
	    yaml.dump(data, fw);
	}
	
	
	@Test
	public void test3() throws IOException {
		Yaml yaml = new Yaml();
		Reader reader =  new InputStreamReader(new FileInputStream(temp), Charset.forName("utf-8"));
		YamlData data = yaml.loadAs(reader, YamlData.class);
		reader.close();
		String s = data.getMessage();
		System.out.println(s);
		System.out.println(data);
	}
}