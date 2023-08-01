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
@ApiModel("사용자 전화번호 확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq028 {
    @ApiModelProperty( value = "국가코드 포함 전화번호", name = "국가코드 포함 전화번호", dataType = "String", example = "821022322232")
    private String phoneNumber;
}

