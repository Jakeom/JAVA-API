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
@ApiModel("유저 공용키 조회 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq030 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "유저 앙코르챗 ID 리스트", name = "유저 앙코르챗 ID 리스트", dataType = "List")
    private String[] userList;
}