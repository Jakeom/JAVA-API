package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 Language 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq054 {
    @JsonIgnore
    private String angkorId;

    @ApiModelProperty( value = "선택한 언어 코드 \n [en:영어,khr:크메르어]", name = "선택한 언어 코드", dataType = "String", example = "en")
    private String language;

}

