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
@ApiModel("사용자 Noti 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq044 {
    @JsonIgnore
    private String angkorId;
    @ApiModelProperty( value = "알람 ON/OFF", name = "알람 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isAddAuto;
    @ApiModelProperty( value = "새로운 그룹채팅 알람 ON/OFF", name = "새로운 그룹채팅 알람 ON/OFF", dataType = "Boolean", example = "false")
    private Boolean isNewGroup;
    @ApiModelProperty( value = "소리 ON/OFF", name = "소리 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isInAppSound;
    @ApiModelProperty( value = "진동 ON/OFF", name = "진동 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isInAppVibrate;
    @ApiModelProperty( value = "미리보기 ON/OFF", name = "미리보기 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isInAppPreview;
    @ApiModelProperty( value = "잠금 화면일경우 사용자명보기 ON/OFF", name = "잠금 화면일경우 사용자명보기 ON/OFF", dataType = "Boolean", example = "true")
    private Boolean isNamesOnLock;
}

