package com.github.cxt.Myyaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Wf {

	private static File temp = new File("tempp.yml");
	
	@Test
	public void testAction() throws IOException {
		DumperOptions options = new DumperOptions();
	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	    Yaml yaml = new Yaml(options);
	    
	    Map<String, Object> data = new HashMap<>();
	    Map<String, Object> map = new HashMap<>();
	    data.put("action", map);
	    map.put("code", "我是唯一表示的code");
	    map.put("name", "ymal文件例子操作");
	    map.put("usage", "我是操作描述:ymal文件例子其实只是一种存储格式,还有其他常见的存储格式比如\"xml\",\"json\"");
	    map.put("tags", "我是标签");
	    map.put("execHost", "agent");
	    map.put("categoryCode", "system");
	    map.put("parallel", false);
	    map.put("timeout", 0);
	    map.put("execAccount", "root");
	    
	    List<Map<String, Object>> inputParam = new ArrayList<>();
	    map.put("inputParam", inputParam);
	    Map<String, Object> input = null;
	    input = new HashMap<>();
	    input.put("name", "param1");
	    input.put("description", "我是第一个输入参数,类型为text");
	    input.put("type", "text");
	    input.put("value", "");
	    input.put("required", true);
	    inputParam.add(input);
	    input = new HashMap<>();
	    input.put("name", "param2");
	    input.put("description", "我是第二个输入参数,类型为passwd");
	    input.put("type", "passwd");
	    input.put("value", "12345678");
	    input.put("required", true);
	    inputParam.add(input);
	    
	    List<Map<String, Object>> outputParam = new ArrayList<>();
	    map.put("outputParam", outputParam);
	    Map<String, Object> output = null;
	    output = new HashMap<>();
	    output.put("name", "param1");
	    output.put("description", "我是第一个输出参数,类型为text");
	    output.put("type", "text");
	    outputParam.add(output);
	    output = new HashMap<>();
	    output.put("name", "param2");
	    output.put("description", "我是第二个输出参数,类型为host");
	    output.put("type", "host");
	    outputParam.add(output);
	    
	    Map<String, Object> script = new HashMap<>();
	    script.put("type", "Python");
	    script.put("content", "我是Python脚本内容\n具体内容此处省略1000行代码\n");
	    map.put("script", script);
	    
	    Writer fw = new OutputStreamWriter(new FileOutputStream(temp), "utf-8");
	    yaml.dump(data, fw);
	}
	
	
	@Test
	public void testWorkflow() throws IOException {
		DumperOptions options = new DumperOptions();
	    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	    Yaml yaml = new Yaml(options);
	    
	    Map<String, Object> data = new HashMap<>();
	    Map<String, Object> map = new HashMap<>();
	    data.put("workflow", map);
	    map.put("name", "ymal文件例子编排");
	    map.put("execType", 2);
	    map.put("description", "我是编排描述:ymal文件例子其实只是一种存储格式,还有其他常见的存储格式比如\"xml\",\"json\"");
	    map.put("params", "我是参数内容\n具体内容很复杂,简单的讲就是json-array格式,我这里也可以重新定义具体的字段和约束,但最后还是以json格式存储,考虑到以前是xml存储，并且只有导入和导出,所以只有用json格式包含所有\n该模块也可以列出具体的字段详情参考http://www.uyunsoft.cn/kb/pages/viewpage.action?pageId=12944013\n");
//	    List<Map<String, Object>> inputParam = new ArrayList<>();
//	    map.put("inputParam", inputParam);
//	    Map<String, Object> input = null;
//	    input = new HashMap<>();
//	    input.put("name", "param1");
//	    input.put("description", "我是第一个输入参数,类型为text");
//	    input.put("type", "text");
//	    input.put("value", "");
//	    input.put("required", true);
//	    inputParam.add(input);
//	    input = new HashMap<>();
//	    input.put("name", "param2");
//	    input.put("description", "我是第二个输入参数,类型为passwd");
//	    input.put("type", "passwd");
//	    input.put("value", "12345678");
//	    input.put("required", true);
//	    inputParam.add(input);
	    
	    List<Map<String, Object>> worknodes = new ArrayList<>();
	    map.put("worknodes", worknodes);
	    Map<String, Object> worknode = null;
	    Map<String, Object> action = null;
	    worknode = new HashMap<>();
	    worknode.put("name", "节点1");
	    worknode.put("confirm", true);
	    worknode.put("params", "我是参数内容\n具体内容很复杂,简单的讲就是json-obj格式,我这里也可以重新定义具体的字段和约束,但最后还是以json格式存储,考虑到以前是xml存储，并且只有导入和导出,所以只有用json格式包含所有\n该模块也可以列出具体的字段详情参考http://www.uyunsoft.cn/kb/pages/viewpage.action?pageId=12944013\n");
	    action = new HashMap<>();
	    action.put("code", "code1");
	    action.put("type", 2);
	    worknode.put("action", action);
	    worknodes.add(worknode);
	    
	    worknode = new HashMap<>();
	    worknode.put("name", "节点2");
	    worknode.put("confirm", true);
	    worknode.put("params", "我是参数内容\n具体内容很复杂,简单的讲就是json-obj格式,我这里也可以重新定义具体的字段和约束,但最后还是以json格式存储,考虑到以前是xml存储，并且只有导入和导出,所以只有用json格式包含所有\n该模块也可以列出具体的字段详情参考http://www.uyunsoft.cn/kb/pages/viewpage.action?pageId=12944013\n");
	    action = new HashMap<>();
	    action.put("code", "code2");
	    action.put("type", 2);
	    worknode.put("action", action);
	    worknodes.add(worknode);
	    
	    Writer fw = new OutputStreamWriter(new FileOutputStream(temp), "utf-8");
	    yaml.dump(data, fw);
	}

}
