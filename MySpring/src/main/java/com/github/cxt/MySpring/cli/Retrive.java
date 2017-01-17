package com.github.cxt.MySpring.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Retrive {
	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption(ApacheCliMain.HELP_OPTION);
		
		options.addOption(Option.builder("c").longOpt("config").argName("file")
                .hasArg()
                .desc("Alternate path for the user yum-store.xml file" )
                .build());
		
		options.addOption(Option.builder("o").longOpt("output").argName("directory")
                .hasArg()
                .desc("output path for zip directory" )
                .build());
		
		options.addOption(Option.builder("v").longOpt("version")
                .hasArg()
                .desc("search rpm version")
                .build());
		
		try {
		    CommandLine line = parser.parse( options, args );
		    List<String> arglist = line.getArgList();
		    if(line.hasOption(ApacheCliMain.HELP_OPTION.getOpt()) || arglist.size() < 5){
		    	printHelp(options);
		    	return ;
		    }
		    StringBuilder sb = new StringBuilder();
		    sb.append("os:" + arglist.get(1))
		    .append(",releasever:" + arglist.get(2))
		    .append(",basearch:" + arglist.get(3))
		    .append(",rpmName:" + arglist.get(4))
		    .append(",version:" + line.getOptionValue("version"))
		    .append(",conf:" + line.getOptionValue("config", "conf/yum-store.xml"))
		    .append(",output:" + line.getOptionValue("output"));
		    System.out.println(sb.toString());
		}
		catch( ParseException exp ) {
			printHelp(options);
		}
	}
	
	public static void printHelp(Options options){
		HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp(ApacheCliMain.CALLCOMMAND + " retrive [options] os releasever basearch rpmName", 
	    		"example: " + ApacheCliMain.CALLCOMMAND + " retrive centos 7 x86_64 unzip"  , options , "");
	}
}
