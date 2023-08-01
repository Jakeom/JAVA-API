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
@ApiModel("SMS 인증확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq009 {
    @ApiModelProperty( value = "signup/signin/update", name = "신규가입/로그인/휴대폰변경", dataType = "String", example = "signup")
    private String grantType;
    @ApiModelProperty( value = "전화번호", name = "phoneNumber", dataType = "String", example = "821012345678")
    private String phoneNumber;
    @ApiModelProperty( value = "인증번호", name = "certNumber", dataType = "String", example = "123456")
    private String certNumber;
    @ApiModelProperty( value = "고유식별자", name = "고유식별자", dataType = "String", example = "ABCD-1234...")
    private String uuid;
    @ApiModelProperty( value = "개인 공유키", name = "개인 공유키", dataType = "String", example = "MFwwDQYJKoZIhvcNAQEBBQA...")
    private String publicKey;
}

