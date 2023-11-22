package com.github.cxt.springboot2.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


@Component
@WebFilter(urlPatterns = "/*", filterName = "userFilter")
public class CustomFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	request.setAttribute(HttpServletRequestWrapper.REQUEST_USER, "test_user");
		chain.doFilter(new HttpServletRequestWrapper(req), response);
		
    }

    @Override
    public void destroy() {

    }


}
