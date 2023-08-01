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
@ApiModel("사용자 Privacy 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq046 {
    @JsonIgnore
    private String angkorId;

    @Min(1) @Max(3)
    @ApiModelProperty( value = "나의 접속 표시 범위 설정 \n [1:everybody, 2:My friends,3:Nobody]", name = "나의 접속 표시 범위 설정", dataType = "Integer", example = "1")
    private Integer inOnlineRange;

    @Min(1) @Max(3)
    @ApiModelProperty( value = "나의 프로파일 사진 범위 설정 \n [1:everybody, 2:My friends,3:Nobody]", name = "나의 프로파일 사진 범위 설정", dataType = "Integer", example = "1")
    private Integer inPhotoRange;

    @Min(1) @Max(3)
    @ApiModelProperty( value = "나에게 전화걸수 있는 범위 설정 \n [1:everybody, 2:My friends,3:Nobody]", name = "나에게 전화걸수 있는 범위 설정", dataType = "Integer", example = "1")
    private Integer inCallRange;

    @Min(1) @Max(3)
    @ApiModelProperty( value = "그룹채팅방 초대 범위 범위 설정 \n [1:everybody, 2:My friends,3:Nobody]", name = "그룹채팅방 초대 범위 범위 설정", dataType = "Integer", example = "1")
    private Integer inGroupRange;
}

