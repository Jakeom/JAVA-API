package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("약관동의 체크 항목 객체")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq020 {
    @ApiModelProperty( value = "약관동의 항목 순서", name = "약관동의 항목 순서", dataType = "int", example = "1")
    private String termOrd;
    @ApiModelProperty( value = "약관동의 항목 체크 유무", name = "약관동의 항목 체크 유무", dataType = "boolean", example = "true")
    private boolean agreement;
}

