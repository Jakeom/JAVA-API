package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 친구추가 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq048 {
    @JsonIgnore
    private String angkorId;

    @ApiModelProperty( value = "친구 자동추가 ON/OFF", name = "친구 자동추가 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isAddAuto;

    @ApiModelProperty( value = "나의 전화번호 자동추가 허용 ON/OFF", name = "나의 전화번호 자동추가 허용 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isInAllowOthers;

    @ApiModelProperty( value = "친구 추천 자동추가 허용 ON/OFF", name = "친구 추천 자동추가 허용 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isInRecommendFriends;

}

