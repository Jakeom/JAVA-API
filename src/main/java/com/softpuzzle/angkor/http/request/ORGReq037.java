package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("고객센터 객체")
public class ORGReq037 {
	@ApiModelProperty(value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334", hidden = true)
	private String angkorId;
	@ApiModelProperty(value = "페이징")
	private Paginginfo paging;
}
