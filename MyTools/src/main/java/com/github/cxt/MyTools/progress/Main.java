package com.github.cxt.MyTools.progress;

import java.text.MessageFormat;

public class Main {
	//在eclipse环境演示不出来,可以用命令
	public static void main(String args[]) throws Exception{
        for (int x = 0 ; x <= 100; x += 1){
            String data = MessageFormat.format("\r{0} 完成{1}%",bar(x),x);
            System.out.write(data.getBytes());
            Thread.sleep(100);
        }
    }

    private static String bar(int i)
    {
        StringBuilder sb = new StringBuilder();

        int x = i / 2;
        sb.append("[");
        for (int k = 0; k < 50; k++) {
            String str = (x <= k) ? " " : "=";
            sb.append(str);
        }
        sb.append("]");

        return sb.toString();
    }
}
