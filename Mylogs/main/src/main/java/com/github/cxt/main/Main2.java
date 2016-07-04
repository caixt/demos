package com.github.cxt.main;


public class Main2 {

	  public static void main(String[] args) throws ClassNotFoundException{
	    PackageScan cs = new PackageScan();
	    int c = cs.scanning("org.apache.log4j", true);
	    //int c = cs.scanning("com.github.cxt", false);
	    System.out.println(c);
	    System.out.println(cs.getClasses());
	    for(String key : cs.getClasses().keySet()){
	    	int count = cs.getClasses().get(key);
	    	if(count > 1){
	    		System.out.println(key + "!" + count);
	    	}
	    }
	  }

}
