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
@ApiModel("사용자 위치 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq027 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "취지정보 체크 [1:GPS, 2:Wifi, 3:Application]", name = "취지정보 체크", dataType = "Integer", example = "1")
    private Integer chkCd;
    @ApiModelProperty( value = "접속 위도", name = "접속 위도", dataType = "String", example = "37.5015983")
    private String latitude;
    @ApiModelProperty( value = "접속 경도", name = "접속 경도", dataType = "String", example = "126.9903933")
    private String longitude;
}