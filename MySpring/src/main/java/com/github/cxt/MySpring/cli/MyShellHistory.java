package com.github.cxt.MySpring.cli;

import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.HistoryFileNameProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MIN_VALUE)
public class MyShellHistory extends SpringShellProvider implements HistoryFileNameProvider {

	@Override
	public String getHistoryFileName() {
		return "myshell-cli.history";
	}

}
