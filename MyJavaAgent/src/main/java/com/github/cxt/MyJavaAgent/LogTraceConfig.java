package com.github.cxt.MyJavaAgent;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class LogTraceConfig{
	
	private static final Logger logger = Logger.getLogger(LogTraceConfig.class.getName());
	
	private static final String CONFIG_FILE_NAME = "logtrace.properties";
	private static File DEFAULT_CONFIG_FILE = new File(".logtrace/" + CONFIG_FILE_NAME);
	
	
	private boolean enableJdbcTrace = false;
	private boolean enableServletTrace = false;
	private List<String> customMethods = new ArrayList<>();
	
    public boolean isEnableJdbcTrace() {
    	return enableJdbcTrace;
    }
    
    public boolean isEnableServletTrace() {
    	return enableServletTrace;
    }
    
    public List<String> getCustomMethods() {
    	return customMethods;
    }
    
	
	private URL getURL(String path){
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			logger.fine(e.toString());
		}
		if (url == null) try {
			File file = new File(path);
			if (file != null && file.exists() && file.isFile()) {
				url = file.toURI().toURL();
			}else{
				logger.fine("file not exists: " + path);
			}
		} catch (MalformedURLException ex) {
			logger.fine(ex.toString());
		}
		return url;
	}
	
	public LogTraceConfig(String configFilePath) {
		URL configURL = null;
		
		if (configFilePath != null){
			logger.fine(String.format("try load config from %s", configFilePath));
			configURL = getURL(configFilePath);
		}
		
		//given config file path
		if (configURL == null) {
			String givenConfigFileName = System.getProperty("logtrace.config");
			if (givenConfigFileName != null) {
				logger.fine(String.format("try load config from %s", givenConfigFileName));
				configURL = getURL(givenConfigFileName);
			}
		}
		
		//work dir
		if (configURL == null) {
			logger.fine("try load config from workdir: " + CONFIG_FILE_NAME);
			configURL = getURL(CONFIG_FILE_NAME);
		}
		
		//classes root
		if (configURL == null) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl!=null){
				logger.fine("try load config from classpath: " + cl);
				configURL = Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILE_NAME);
			}
		}
		
		//home default dir
		if (configURL == null) {
			logger.fine("try load config from homedir: " + DEFAULT_CONFIG_FILE);
			if (DEFAULT_CONFIG_FILE != null && DEFAULT_CONFIG_FILE.exists()) {
				try {
					configURL = DEFAULT_CONFIG_FILE.toURI().toURL();
				} catch (MalformedURLException e) {
				}
			}
		}

		// load default
		if (configURL == null) {
			logger.fine("extract default configuration to " + DEFAULT_CONFIG_FILE.getAbsolutePath());
			try {
				extractDefaultProfile();
				configURL = DEFAULT_CONFIG_FILE.toURI().toURL();
			} catch (IOException e) {
				throw new RuntimeException("error load config file " + DEFAULT_CONFIG_FILE, e);
			}
		}	
		
		logger.info("load configuration from " + configURL.toString());
		parseProperty(configURL);
	}
	

	
	private void extractDefaultProfile() throws IOException {
		InputStream in = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
		OutputStream out = null;
		try{
		  File profileDirectory = DEFAULT_CONFIG_FILE.getParentFile();
		  if (!profileDirectory.exists()){
			profileDirectory.mkdirs();
		  }
		  out = new BufferedOutputStream(new FileOutputStream(DEFAULT_CONFIG_FILE));
		  byte[] buffer = new byte[1024];
		  for (int len = -1; (len = in.read(buffer)) != -1;){
			out.write(buffer, 0, len);
		  }
		}finally{
		  in.close();
		  out.close();
		}
	}

	
	private void parseProperty(URL url) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = url.openStream();
			properties.load(in); 
			properties.list(System.out);
			
			Properties context = new Properties(); 
			context.putAll(System.getProperties());
			context.putAll(properties);
			
			loadConfig(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (in!=null) try{
				in.close();
			}catch(IOException ioe) {}
		}
	}

	private void loadConfig(Properties properties)  {
		this.enableJdbcTrace = Boolean.parseBoolean(properties.getProperty("logtrace.enableJdbcTrace"));
		this.enableServletTrace = Boolean.parseBoolean(properties.getProperty("logtrace.enableServletTrace"));
		
		String customMethodsStr = properties.getProperty("logtrace.customNethods");
		if (customMethods != null && customMethodsStr.trim().length() > 0){
			String[] str = customMethodsStr.split(";");
			for (String s : str) {
				if(s.indexOf(".") > 0){
					customMethods.add(s);
				}
			}
		}
		
	}

}