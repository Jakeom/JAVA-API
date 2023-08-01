package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("페이징 객체")
public class Paginginfo {
	@ApiModelProperty(notes = "paging number", dataType = "integer")
	private Integer page;
	@ApiModelProperty(notes = "paging size", dataType = "integer")
	private Integer pagingSize;
	@ApiModelProperty(notes = "OFFSET", dataType = "integer")
    @JsonIgnore
    private Integer offset;
	
}
