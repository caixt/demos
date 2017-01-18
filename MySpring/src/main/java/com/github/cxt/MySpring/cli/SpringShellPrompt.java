package com.github.cxt.MySpring.cli;

import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Integer.MIN_VALUE)
public class SpringShellPrompt extends SpringShellProvider implements PromptProvider {

	@Override
	public String getPrompt() {
		return "myshell>";
	}

}
