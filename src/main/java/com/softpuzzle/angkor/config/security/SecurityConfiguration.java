package com.softpuzzle.angkor.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	 private static final String[] AUTH_WHITELIST = {
	            "/swagger-resources/**",
	            "/swagger-ui.html",
	            "/v2/api-docs",
	            "/webjars/**",
	            "/apis/*"
	    };

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
        .inMemoryAuthentication()
            .withUser("skyun").password("skyun").roles("USER").and()
            .withUser("admin").password("admin").roles("USER","ADMIN");
	}
	
	//리소스(URL) 접근 권한 설정
	//인증 전체 흐름에 필요한 Login, Logout 페이지 인증완료 후 페이지 인증 실패 시 이동페이지 등등 설정
	//인증 로직을 커스텀하기위한 커스텀 필터 설정
	//기타 csrf, 강제 https 호출 등등 거의 모든 스프링시큐리티의 설정

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		
		http.csrf().disable();
		//http.logout().disable();
		http.authorizeRequests()
			.antMatchers("/login**", "/resources/static/**", "/actuator/**").permitAll()
			.antMatchers("/api/**").permitAll()
			.antMatchers("swagger-ui.html").permitAll()
			.antMatchers("/v2/api-docs").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.antMatchers("/swagger-resources/**").permitAll();
		
		http.sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
	}

	//
	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().antMatchers("/resources/**", "/api/test", "/apis/**",
		// "/swagger-resources/**", "/swagger-ui","/swagger/**");

		// resources 모든 접근을 허용하는 설정을 해버리면
        // HttpSecurity 설정한 ADIM권한을 가진 사용자만 resources 접근가능한 설정을 무시해버린다.
		web.ignoring()
				.antMatchers("/resources/**")
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security","/swagger-ui.html", "/webjars/**", "/swagger/**");
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}