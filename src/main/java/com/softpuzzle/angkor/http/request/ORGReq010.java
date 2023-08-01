package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 로그인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq010 {
    @ApiModelProperty( value = "사용자 아이디", name = "사용자 아이디", dataType = "String", example = "userid1234")
    private String userId;
    @ApiModelProperty( value = "비밀번호", name = "비밀번호", dataType = "String", example = "15evwk3j4143343")
    private String password;
    @ApiModelProperty( value = "고유식별자", name = "고유식별자", dataType = "String", example = "ABCD-1234...")
    private String uuid;
    @ApiModelProperty( value = "개인 공유키", name = "개인 공유키", dataType = "String", example = "MFwwDQYJKoZIhvcNAQEBBQA...")
    private String publicKey;
}

