package com.softpuzzle.angkor.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("고객센터 상세보기 객체")
public class ORGReq036 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334", hidden = true)
    private String angkorId;
    @ApiModelProperty( value = "seq", name = "seq", dataType = "Integer")
    private int seq;

}