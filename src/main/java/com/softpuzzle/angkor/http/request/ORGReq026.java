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
@ApiModel("프로필 이미지 수정 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq026 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "나의 프로필 이미지 seq", name = "나의 프로필 이미지 seq", dataType = "Integer", example = "22")
    private Integer profileSeq;
    @ApiModelProperty( value = "변경하려는 상태 \n [1:Set as Profile Photo, 2:Hide,3:Delete]", name = "변경하려는 상태", dataType = "Integer", example = "1")
    private Integer status;
}