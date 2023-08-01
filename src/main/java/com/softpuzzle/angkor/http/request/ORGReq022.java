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
@ApiModel("프로필 검색 허용 상태 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq022 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "프로필 검색 허용 체크 유무", name = "프로필 검색 허용 체크 유무", dataType = "boolean", example = "true")
    private Boolean showProfileYn;
}