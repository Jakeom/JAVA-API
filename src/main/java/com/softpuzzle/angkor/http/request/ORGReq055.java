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
@ApiModel("플랫폼별 버전정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq055 {
    @ApiModelProperty( value = "ANDROID/IOS", name = "전화 OS유형", dataType = "String", example = "ANDROID")
    private String osType;
}

