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
@ApiModel("휴대폰 주소록 친구 리스트 추가 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq025 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "나의 휴대폰 주소록 친구 리스트", name = "주소록 친구 리스트", dataType = "List", allowableValues = "'['{'phoneNumber': '01012345678','userName': 'bobby'}','{phoneNumber': '01012344321','userName': 'musk'}']'")
    private List<ORGReq023> friendList;
}