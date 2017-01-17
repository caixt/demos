package com.github.cxt.MySpring.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ApacheCliMain {

public static Option HELP_OPTION = new Option("help", false, "print help message");
	
	public static String CALLCOMMAND = System.getProperty("CALL_COMMAND", "");
	
	
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption(null, "clean", false, "clean all cache");
		options.addOption(null, "retrive", false, "search rpm and download");
		options.addOption(null, "repo", false, "create repodata for yum store");
        
		if(args.length == 0){
			printHelp(options);
			return ;
		}
		String command = args[0];
		try{
			switch (command) {
				case "retrive" :{
					Retrive.main(args);
					break;
				}
				default:
					printHelp(options);
					break;
			}
		}catch (RuntimeException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}  
	}
	
	
	private static void printHelp(Options options){
        HelpFormatter formatter = new HelpFormatter();
		formatter.setSyntaxPrefix(CALLCOMMAND + " command");
	    formatter.setLongOptPrefix("");
	    formatter.printHelp(" ", options);
	}
}
