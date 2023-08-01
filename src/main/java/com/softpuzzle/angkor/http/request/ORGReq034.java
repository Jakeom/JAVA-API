package com.softpuzzle.angkor.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("아이디 추가 객체")
public class ORGReq034 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "user id", name = "유저 ID", dataType = "String", example = "id1234zxc")
    private String userId;
    @ApiModelProperty( value = "개인 공유키", name = "개인 공유키", dataType = "String", example = "MFwwDQYJKoZIhvcNAQEBBQA...")
    private String publicKey;
}