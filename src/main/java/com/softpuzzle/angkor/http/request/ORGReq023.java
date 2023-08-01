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
@ApiModel("휴대폰 주소록 친구 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq023 {
    @ApiModelProperty( value = "친구 전화번호", name = "PhoneNumber", dataType = "String", example = "01012345678")
    private String friend_phonenumber;
    @ApiModelProperty( value = "친구의 이름", name = "친구의 이름", dataType = "String", example = "Bobby")
    private String friend_name;
}