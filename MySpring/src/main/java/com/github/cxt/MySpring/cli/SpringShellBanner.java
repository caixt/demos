package com.github.cxt.MySpring.cli;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Order(Integer.MIN_VALUE)
public class SpringShellBanner extends SpringShellProvider implements BannerProvider {

	private static final String BANNER = "MySrpingBanner.txt";
	private static final String ASSISTANCE = "Welcome to MyShell. For assistance type help.";
	
	private final static Logger logger = Logger.getLogger(SpringShellBanner.class);

    @Value("${MySrping.version}")
    private String version;

	@Override
	public String getBanner() {
		logger.info("!!!!!!!!!");
		StringBuilder sb = new StringBuilder();
		try (InputStream in = ClassLoader.getSystemResourceAsStream(BANNER)) {
			sb.append(IOUtils.toString(in));
		} catch(IOException e) {
			sb.append("MyShell");
		}
		sb.append(System.lineSeparator());
		sb.append(getVersion());
		return sb.toString();
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getWelcomeMessage() {
		return ASSISTANCE;
	}

}
