package com.softpuzzle.angkor.http.response.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class ResSimpleDTO implements Serializable {

	@ApiModelProperty(position = 0, required = true, value = "수행 결과 Object 결과")
	private Object data;

	@ApiModelProperty(position = 1, required = true, value = "수행결과 메시지")
	private String message;

	@ApiModelProperty(position = 2, required = true, value = "수행 결과 코드 0(성공) 나머지는 에러")
	private String code;

	@ApiModelProperty(position = 3, required = true, value = "수행 시간")
	private String resTime;

	@Builder
	public ResSimpleDTO(Object data, String message, String code, String resTime) {
		super();
		this.data = data;
		this.message = message;
		this.code = code;
		this.resTime = resTime;
	}
	
	
	

}
