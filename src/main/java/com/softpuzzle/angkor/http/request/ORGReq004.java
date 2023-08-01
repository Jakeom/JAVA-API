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
@ApiModel("약관 동의")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq004 {
    @ApiModelProperty( value = "선택 언어", name = "선택 언어", dataType = "String", example = "en/khr")
    private String language;
}