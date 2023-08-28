package com.lvboaa.utils.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterRegistrationConfig {
	
	/**
	  * 配置过滤器
	  * @return
	  */
	 @Bean
	 public FilterRegistrationBean<Filter> registrationBean() {
	     FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
	     registration.setFilter(openApiFilter());
	     registration.addUrlPatterns("/*");
		 registration.addInitParameter("excludes",
				 "/user/*");
	     registration.setName("openApiFilter");
		 // 排除华光的接口

	     registration.setOrder(Integer.MAX_VALUE);

	     return registration;
	 }
	 @Bean(name = "openApiFilter")
	 public Filter openApiFilter() {
	     return new OpenApiFilter();
	 }
}
