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
@ApiModel("사용자 즐겨찾기 및 차단 추가, 해제 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq018 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "차단하려는 친구 앙코르챗 id", name = "friend angkorId", dataType = "String", example = "akGdS4f123461234")
    private String friend_angkorId;
}

