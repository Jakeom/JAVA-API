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
@ApiModel("사용자 PW 변경(분실 시) 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq016 {
    @ApiModelProperty( value = "email", name = "이메일 주소", dataType = "String", example = "")
    private String email;
    @ApiModelProperty( value = "인증번호", name = "인증번호", dataType = "String", example = "123456")
    private String certNumber;
}

