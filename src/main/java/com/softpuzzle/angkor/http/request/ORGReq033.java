package com.softpuzzle.angkor.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("이메일 변경 객체")
public class ORGReq033 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "email", name = "이메일 주소", dataType = "String", example = "")
    private String email;
    @ApiModelProperty( value = "인증번호", name = "인증번호", dataType = "String", example = "123456")
    private String certNumber;
}