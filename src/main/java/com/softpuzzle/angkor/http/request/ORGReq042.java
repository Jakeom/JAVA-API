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
@ApiModel("사용자 버전 변경 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq042 {
    @ApiModelProperty( value = "version", name = "앙코르챗 Version", dataType = "String", example = "1.1")
    private String version;
}

