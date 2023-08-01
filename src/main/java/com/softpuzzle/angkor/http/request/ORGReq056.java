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
@ApiModel("사용자 채팅 설정 이미지 해상도 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq056 {
    @JsonIgnore
    private String angkorId;
    @ApiModelProperty( value = "이미지 해상도 \n Code [D001:original, D002:standard,D003:low]", name = "이미지 해상도", dataType = "String", example = "D001")
    private String inImageResolution;

}

