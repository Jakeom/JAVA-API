package com.softpuzzle.angkor.config.servletFilter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("ServletFilterBean")
public class ServletFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(ServletFilter.class);

	
	/*
	 * 필터 예외 URI
	 */
	List<String> whiteReqs = null;
	List<String> staticResources = null;

	private static final String URI_ROOT = "/";
	private static final String URI_LOGIN = "/login";
	private static final String URI_LOGOUT = "/logout";
	private static final String URI_RESOURCE = "/resources/**";

	/*
	 * 서블릿에서 Open 할경우 무조건 사용자에게 서비스 한다.
	 */

	public ServletFilter() {

		whiteReqs = new ArrayList<>();
		
		whiteReqs.add(URI_ROOT); // 파비콘 요청
		whiteReqs.add(URI_LOGIN); // 파비콘 요청
		whiteReqs.add("/index"); // 파비콘 요청
		whiteReqs.add("/dashboard"); // 파비콘 요청

		staticResources = new ArrayList<>();
		staticResources.add(URI_RESOURCE);

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		Enumeration<String> hname = req.getHeaderNames();

		while (hname.hasMoreElements()) {
			String name = (String) hname.nextElement();
			String value = req.getHeader(name);

			log.info("Header 정보 :" + name + " : " + value);			

		}
		
		log.info( req.getRequestURI());
		
		/*
		
		UserVo user = sessionContext.get().getUser();
		String uri = req.getRequestURI();
		
		//이상한 호출이 존재 해서 무조건 Return (막음)
		if (uri.contains("/favicon.ico") ) {
			log.info(">>>>>> -------------------------------------");			
			return;
		}
		*/
		IncaResponseRequestWrapper myres = new IncaResponseRequestWrapper(res);
		
		try {
			chain.doFilter(request, myres);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	
	private boolean isWhiteList(String uri) {
		boolean isWhiteReq = false;
		
		if(uri.contains("/resources/") ||  uri.contains("/api")) {
			isWhiteReq = true;
			return isWhiteReq;
		}
		for (String whiteReq : whiteReqs) {
			if (uri.startsWith(whiteReq)) {
				isWhiteReq = true;
				break;
			}
		}
		
		return isWhiteReq;
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// FilterConfig를 통한 filter 설정
		// 서블릿 컨테이너가 필터 인스턴스 생성한 후 초기화 하기 위해서 사용 전 호출하는 메소드

	}

}
