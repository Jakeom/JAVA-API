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
@ApiModel("사용자 채팅 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq050 {
    @JsonIgnore
    private String angkorId;

    @ApiModelProperty( value = "채팅방 이름 소트 방식 \n Code [D001:By Time, D002:By Unread Chats]", name = "채팅방 이름 소트 방식", dataType = "String", example = "D001")
    private String inSortrooms;

    @ApiModelProperty( value = "이미지 해상도 \n Code [D001:original, D002:standard,D003:low]", name = "이미지 해상도", dataType = "String", example = "D001")
    private String inImageResolution;

    @ApiModelProperty( value = "동영상 해상도 \n Code [D001:original, D002:standard]", name = "동영상 해상도", dataType = "String", example = "D001")
    private String inVideos;

    @ApiModelProperty( value = "영상 전송 시 컨버팅 여부", name = "영상 전송 시 컨버팅 여부", dataType = "Boolean", example = "true")
    private Boolean inVideosConvert;

}

