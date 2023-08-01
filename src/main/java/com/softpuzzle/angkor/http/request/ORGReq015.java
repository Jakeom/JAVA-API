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
@ApiModel("사용자 전화번호 변경 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq015 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "전화국가코드", name = "전화국가코드", dataType = "String", example = "82")
    private String phoneCode;
    @ApiModelProperty( value = "국가코드 포함 전화번호", name = "국가코드 포함 전화번호", dataType = "String", example = "821022322232")
    private String phoneNumber;
    @ApiModelProperty( value = "인증번호", name = "certNumber", dataType = "String", example = "123456")
    private String certNumber;
    @ApiModelProperty( value = "update", name = "신규가입/로그인/휴대폰변경", dataType = "String", example = "update")
    private String grantType;
    @ApiModelProperty( value = "sms 인증 authKey", name = "sms 인증 authKey", dataType = "String", example = "akj123159jfasdfjacnaskl1k2315sadfqweqwe1231fv")
    private String authKey;
}

