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
@ApiModel("사용자 푸시 토큰 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq062 {
    @JsonIgnore
    private String angkorId;
    
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private Boolean isVideoCall;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String endedBy;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String callerNm;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String callerId;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String callerPf;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String calleeId;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String calleeNm;
    @ApiModelProperty( value = "", name = "", dataType = "String", example = "")
    private String calleePf;

}


