package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq001 {
	@ApiModelProperty( value = "0", name = "Start", dataType = "Integer", example = "0")
    private int start_cnt=0;
	@ApiModelProperty( value = "10", name = "End", dataType = "Integer", example = "4")
    private int end_cnt=0;		
	@ApiModelProperty( value = "1", name = "Page", dataType = "Integer", example = "1")
    private int page_num=0;						

}

