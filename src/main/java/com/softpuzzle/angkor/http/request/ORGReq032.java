package com.softpuzzle.angkor.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("유저 아이디 조회 객체")
public class ORGReq032 {
    @ApiModelProperty( value = "user id", name = "유저 ID", dataType = "String", example = "id1234zxc")
    private String userId;
    @ApiModelProperty( value = "signup/signin", name = "신규가입/로그인", dataType = "String", example = "signup")
    private String grantType;
}