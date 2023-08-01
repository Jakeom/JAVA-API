package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 디스플레이 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq052 {
    @JsonIgnore
    private String angkorId;

    @ApiModelProperty( value = "채팅창 백그라운드 컬러 코드 seq \n Code [1:FFFAFF ...]", name = "채팅창 백그라운드 컬러 seq", dataType = "Integer", example = "1")
    private Integer backgroundSeq;

    @ApiModelProperty( value = "폰트 사이즈", name = "폰트 사이즈", dataType = "String", example = "Font Size")
    private String inFontsize;

    @ApiModelProperty( value = "나이트 모드 \n Code [0:밤,1:낮]", name = "나이트 모드", dataType = "Integer", example = "0")
    private Integer inNightmode;

    @ApiModelProperty( value = "테마 컬러 코드 \n Code [D001:FFFAFF ...]", name = "테마 컬러 코드", dataType = "String", example = "D001")
    private String inThemes;

}

