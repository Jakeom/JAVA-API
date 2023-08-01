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
@ApiModel("SMS 인증요청 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq008 {
    @ApiModelProperty( value = "국가코드 포함 전화번호", name = "PhoneNumber", dataType = "String", example = "821012345678")
    private String phoneNumber;
    @ApiModelProperty( value = "고유식별자", name = "고유식별자", dataType = "String", example = "ABCD-1234...")
    private String uuid;
    @ApiModelProperty( value = "signup/signin", name = "신규가입/로그인", dataType = "String", example = "signup")
    private String grantType;
}

