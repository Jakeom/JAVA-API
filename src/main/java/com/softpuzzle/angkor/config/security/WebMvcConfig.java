package com.softpuzzle.angkor.config.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

	@Autowired
	@Qualifier(value = "httpInterceptor")
	private HandlerInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/")
			.excludePathPatterns("/login")
			.excludePathPatterns("/apis/**")
			.excludePathPatterns("/swagger-resources/**")
			.excludePathPatterns("/swagger-ui.html")
			.excludePathPatterns("/swagger/**")
			.excludePathPatterns("/resources/**");
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
        		.addResourceLocations("/views/**")
                .addResourceLocations("/WEB-INF/resources/")
                .addResourceLocations("/swagger-ui.html")
                .addResourceLocations("/swagger-resources/**")
                .addResourceLocations("/swagger/**")
                .addResourceLocations("/apis/**")
                .setCachePeriod(20);
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	

}