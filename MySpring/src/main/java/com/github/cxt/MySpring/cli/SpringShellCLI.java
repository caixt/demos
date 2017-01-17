package com.github.cxt.MySpring.cli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Component
public class SpringShellCLI implements CommandMarker {

    private static final String CURRENTLY = "You are CURRENTLY running MyShell version: ";

    @Value("${MySrping.version}")
    private String version;

    /**
     * This global param holds the state of the CLI, if flows need to run in ASYNC or in SYNC manner.
     */
    private Boolean triggerAsync = false;

    @CliCommand(value = "run", help = "triggers a MyShell cmd")
    public String run(
            @CliOption(key = {"", "f", "file"}, mandatory = true, help = "Path to filename. e.g. myshell run --f C:\\myshell\\shell.cmd") final File file,
            @CliOption(key = {"cp", "classpath"}, mandatory = false, help = "Classpath , a directory comma separated list to flow dependencies, by default it will take flow file dir") final List<String> classPath,
            @CliOption(key = {"i", "inputs"}, mandatory = false, help = "inputs in a key=value comma separated list") final Map<String,? extends Serializable> inputs,
            @CliOption(key = {"if", "input-file"}, mandatory = false, help = "comma separated list of input file locations") final List<String> inputFiles,
            @CliOption(key = {"", "q", "quiet"}, mandatory = false, help = "quiet", specifiedDefaultValue = "true",unspecifiedDefaultValue = "false") final Boolean quiet,
            @CliOption(key = {"", "d", "debug"}, mandatory = false, help = "print each task outputs", specifiedDefaultValue = "true",unspecifiedDefaultValue = "false") final Boolean debug,
            @CliOption(key = {"spf", "system-property-file"}, mandatory = false, help = "comma separated list of system property file locations") final List<String> systemPropertyFiles) throws IOException {
    	System.err.println("!!!!!!!!!!!!!!!");
        return "haha";
    }

    @CliCommand(value = "env", help = "Set environment var relevant to the CLI")
    public String setEnvVar(
            @CliOption(key = "setAsync", mandatory = true, help = "set the async") final boolean switchAsync) throws IOException {
    	triggerAsync = switchAsync;
    	return setEnvMessage(triggerAsync);
    }

    @CliCommand(value = "myshell --version", help = "Prints the MyShell version used")
    public String version() {
        return CURRENTLY + version;
    }
    
    public static String setEnvMessage(boolean triggerAsync) {
        return "flow execution ASYNC execution was changed to : " + triggerAsync;
    }
}
