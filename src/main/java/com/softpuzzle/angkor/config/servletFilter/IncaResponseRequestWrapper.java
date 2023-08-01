package com.softpuzzle.angkor.config.servletFilter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

//향후 Response의 내용을 수정 하기 위해서 사용
public class IncaResponseRequestWrapper extends HttpServletResponseWrapper{

	public IncaResponseRequestWrapper(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

}
