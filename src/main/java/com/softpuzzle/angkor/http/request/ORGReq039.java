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
@ApiModel("그룹채팅 사용자 확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq039 {
    @JsonIgnore
    private String angkorId;
    @ApiModelProperty( value = "그룹 채팅 참여 유저 리스트", name = "그룹 채팅 참여 유저 리스트", dataType = "List")
    private String[] userList;
}

