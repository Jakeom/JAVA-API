package com.softpuzzle.angkor.http.response.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ResTrDTO {

	@ApiModelProperty(position = 0, required = true, value = "수행 결과 Object 결과")
	private Object data;

	@ApiModelProperty(position = 1, required = true, value = "수행 결과 코드 0(성공) 나머지는 에러")
	private String code;

	@ApiModelProperty(position = 2, required = true, value = "수행결과 메시지")
	private String message;

	@ApiModelProperty(position = 3, required = true, value = "TR 갯수")
	private int trcnt;

	@ApiModelProperty(position = 4, required = true, value = "수행 시간")
	private String trdate;

	@Builder
	public ResTrDTO(Object data, String code, String message, int trcnt, String trdate) {
		super();
		this.data = data;
		this.code = code;
		this.message = message;
		this.trcnt = trcnt;
		this.trdate = trdate;
	}

	
	
}
