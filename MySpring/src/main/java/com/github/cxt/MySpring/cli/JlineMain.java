package com.github.cxt.MySpring.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jline.Completor;
import jline.ConsoleReader;

/**
 * 备注 在eclpse中运行没效果
 * @author caixt@broada.com
 * @Description:
 * @date 2016年9月7日
 */
public class JlineMain {

	protected static final Map<String,String> commandMap = new HashMap<String,String>();
	
	static {
        commandMap.put("connect", "host:port");
        commandMap.put("close","");
        commandMap.put("create", "[-s] [-e] path data acl");
        commandMap.put("delete","path [version]");
        commandMap.put("rmr","path");
        commandMap.put("set","path data [version]");
        commandMap.put("get","path [watch]");
        commandMap.put("ls","path [watch]");
        commandMap.put("quit","");
    }
	 
	
	public static void main(String[] args) throws IOException {
		ConsoleReader reader = new ConsoleReader();
		reader.addCompletor(new Completor() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public int complete(String buffer, int cursor, List candidates) {
				buffer = buffer.substring(0,cursor);
				for (String cmd : commandMap.keySet()) {
		            if (cmd.startsWith( buffer )) {
		                candidates.add(cmd);
		            }
		        }
				return buffer.lastIndexOf(" ")+1;
			}
		});
		String line;
        while ((line = (String)reader.readLine("caixt>")) != null) {
        	if(line.trim().equals("")){
        		continue;
        	}
        	MyCommandOptions opts = new MyCommandOptions(line);
        	System.out.println(opts);
        	if(line.startsWith("quit")){
        		 System.out.println("Quitting...");
                 System.exit(0);
        	}
        }
	}
	
	
	static class MyCommandOptions {

        private List<String> cmdArgs = null;
        private String command = null;
        
        MyCommandOptions(String cmdstring){
        	StringTokenizer cmdTokens = new StringTokenizer(cmdstring, " ");          
            String[] args = new String[cmdTokens.countTokens()];
            int tokenIndex = 0;
            while (cmdTokens.hasMoreTokens()) {
                args[tokenIndex] = cmdTokens.nextToken();
                tokenIndex++;
            }
            command = args[0];
            cmdArgs = Arrays.asList(args);
        }

		@Override
		public String toString() {
			return "MyCommandOptions [cmdArgs=" + cmdArgs + ", command="
					+ command + "]";
		}
        
	}

}
