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
@ApiModel("이메일 인증코드 확인 객체")
public class ORGReq007 {
    @ApiModelProperty( value = "email", name = "이메일 주소", dataType = "String", example = "")
    private String email;
    @ApiModelProperty( value = "인증번호", name = "인증번호", dataType = "String", example = "123456")
    private String certNumber;
}

