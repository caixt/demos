package com.github.cxt.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**
 * 只对jar下的包有效,maven的引用不正确
 * @Author
 * @Description 包扫描器
 * @CopyRight 
 */
public class PackageScan{
  private Map<String, Integer> classes	= new HashMap<String, Integer>();
  private FilenameFilter		javaClassFilter;									// 类文件过滤器,只扫描一级类
  private final String		  CLASS_FILE_SUFFIX = ".class";					   // Java字节码文件后缀
  private String				packPrefix;										 // 包路径根路劲
  private String				scanPackPrefix;										 // 包路径根路劲
  
  public PackageScan(){
    javaClassFilter = new FilenameFilter(){
      public boolean accept(File dir, String name){
        // 排除内部内
        return !name.contains("$");
      }
    };
  }

  /**
   * @Title: scanning
   * @Description 扫描指定包, Jar或本地
   * @param packagePath 包路径
   * @param recursive 是否扫描子包
   * @return Integer 类数量
   */
  public Integer scanning(String packagePath, boolean recursive){
	scanPackPrefix = packagePath;
    Enumeration<URL> dir;
    String filePackPath = packagePath.replace('.', '/');
    try{
      // 得到指定路径中所有的资源文件
      dir = Thread.currentThread().getContextClassLoader().getResources(filePackPath);
      packPrefix = Thread.currentThread().getContextClassLoader().getResource("").getPath();
      if(System.getProperty("file.separator").equals("\\")){
        packPrefix = packPrefix.substring(1);
      }
      // 遍历资源文件
      while(dir.hasMoreElements()){
        URL url = dir.nextElement();
        String protocol = url.getProtocol();
        if("file".equals(protocol)){
          File file = new File(url.getPath().substring(1));
          scan0(file, recursive);
        } 
        else if("jar".equals(protocol)){
          scanJ(url, recursive);
        }
      }
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
    return classes.size();
  }
  /**
   * @Title: scanJ
   * @Description 扫描Jar包下所有class
   * @param url jar-url路径
   * @param recursive 是否递归遍历子包
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void scanJ(URL url, boolean recursive) throws IOException, ClassNotFoundException{
    JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
    JarFile jarFile = jarURLConnection.getJarFile();
    // 遍历Jar包
    Enumeration<JarEntry> entries = jarFile.entries();
    while(entries.hasMoreElements()){
      JarEntry jarEntry = (JarEntry)entries.nextElement();
      String fileName = jarEntry.getName();
      if (jarEntry.isDirectory()) {
        if (recursive) {
        }
        continue;
      }
      // .class
      if(fileName.endsWith(CLASS_FILE_SUFFIX)){
        String className = fileName.substring(0, fileName.indexOf('.')).replace('/', '.');
        boolean b = className.startsWith(scanPackPrefix) && className.substring(scanPackPrefix.length()).split("\\.").length < 3;
        if(recursive || b){
        	add(className);
        }
      }
    }
  }
  /**
   * @Title: scan0
   * @Description 执行扫描
   * @param dir Java包文件夹
   * @throws ClassNotFoundException
   */
  private void scan0(File dir, boolean recursive) throws ClassNotFoundException{
    File[] fs = dir.listFiles(javaClassFilter);
    for(int i = 0; fs != null && i < fs.length; i++){
      File f = fs[i];
      String path = f.getAbsolutePath();
      // 跳过其他文件
      if(path.endsWith(CLASS_FILE_SUFFIX)){
        String className = StringUtil.getPackageByPath(f, packPrefix); // 获取包名
        add(className);
      }
      else if(f.isDirectory() && recursive){
    	  scan0(f, recursive);
      }
    }
  }
  /**
   * @Title: getClasses
   * @Description 获取包中所有类
   * @return Map<String,Class<?>> K:类全名, V:Class字节码
   */
  public Map<String, Integer> getClasses(){
    return classes;
  }

  
  private void add(String className){
	 Integer i = classes.get(className);
	 if(i == null){
		 i = 1;
	 }
	 else {
		 i ++;
	 }
	 classes.put(className, i);
  }
}
