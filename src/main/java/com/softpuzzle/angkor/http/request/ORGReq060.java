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
@ApiModel("사용자 Chat 소리 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq060 {
    @JsonIgnore
    private String angkorId;
    
    @ApiModelProperty( value = "채널명", name = "채널명", dataType = "String", example = "sendbird_group_channel_400766254_f16ec474beed9563d33ad07b3e2d4eb3fb6a895f")
    private String channelUrl;
    @ApiModelProperty( value = "사운드명", name = "사운드명", dataType = "String", example = "default")
    private String soundNm;

}


