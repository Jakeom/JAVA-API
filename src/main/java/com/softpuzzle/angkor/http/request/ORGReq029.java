package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("비밀채팅 그룹 키 생성 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq029 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "비밀 채팅 참여 유저 리스트", name = "비밀 채팅 참여 유저 리스트", dataType = "List")
    private String[] userList;
}

