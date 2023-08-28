package com.lvboaa.utils.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class OpenApiFilter implements Filter{
	private static Logger logger=LoggerFactory.getLogger(OpenApiFilter.class);
	private final String validateFailed = "/openapi/validateFailed";
	private final String openapiPath = "/openapi/";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println(123);
		String requestURI = ((HttpServletRequest) request).getRequestURI();

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
