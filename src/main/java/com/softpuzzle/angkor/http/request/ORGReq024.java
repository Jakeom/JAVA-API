package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("휴대폰 주소록 친구의 정보 확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq024 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "친구 앙코르챗 id", name = "friend angkorId", dataType = "String", example = "akGdS4f123461234")
    private String friend_angkorId;
    @ApiModelProperty( value = "친구 전화번호", name = "PhoneNumber", dataType = "String", example = "01012345678")
    private String friend_phonenumber;
    @ApiModelProperty( value = "친구의 이름", name = "친구의 이름", dataType = "String", example = "Bobby")
    private String friend_name;
}