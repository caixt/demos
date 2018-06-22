package com.github.cxt.MySpring.transaction.custom;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;


public class CustomManagementConfigurationSelector extends AdviceModeImportSelector<EnableCustomManagement> {

	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
		case PROXY:
			return new String[] { AutoProxyRegistrar.class.getName(), ProxyCustomManagementConfiguration.class.getName() };
		case ASPECTJ:
			return null;
		default:
			return null;
		}
	}

}
