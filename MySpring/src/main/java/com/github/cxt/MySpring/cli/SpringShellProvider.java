package com.github.cxt.MySpring.cli;

import org.springframework.shell.plugin.NamedProvider;

public abstract class SpringShellProvider implements NamedProvider {

	@Override
	public String getProviderName() {
		return "MyShell";
	}

}
