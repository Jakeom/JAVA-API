package com.softpuzzle.angkor.http.response.common;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResCommDTO implements Serializable {

	private static final long serialVersionUID = 3782250480608038900L;

	@ApiModelProperty(position = 0, required = true, value = "수행 결과 Object 결과")
	private Object data;

	@ApiModelProperty(position = 1, required = true, value = "요청 페이지")
	private int current;

	@ApiModelProperty(position = 2, required = true, value = "요청 페이지 사이즈")
	private int pageSize;

	@ApiModelProperty(position = 3, required = true, value = "수행 결과 레코드 카운트")
	private int total;

	@ApiModelProperty(position = 4, required = true, value = "수행결과 메시지")
	private String message;

	@ApiModelProperty(position = 5, required = true, value = "수행 시간")
	private String resTime;

	@ApiModelProperty(position = 6, required = true, value = "수행 결과 코드 0(성공) 나머지는 에러")
	private String code;

	@Builder
	public ResCommDTO(Object data, int current, int pageSize, int total, String message, String resTime, String code) {
		super();
		this.data = data;
		this.current = current;
		this.pageSize = pageSize;
		this.total = total;
		this.message = message;
		this.resTime = resTime;
		this.code = code;
	}

	
}
