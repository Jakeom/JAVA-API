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
@ApiModel("사용자 채팅방 배경 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq058 {
    @JsonIgnore
    private String angkorId;
    @ApiModelProperty( value = "채팅창 백그라운드 컬러 코드 seq \n Code [1:FFFAFF ...]", name = "채팅창 백그라운드 컬러 seq", dataType = "Integer", example = "1")
    private Integer backgroundSeq;

}

