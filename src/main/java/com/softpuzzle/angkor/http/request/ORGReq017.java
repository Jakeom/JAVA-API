package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 PW 변경(알고 있을 때) 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq017 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "new password", name = "새로운 비밀번호", dataType = "String", example = "dkfajdrkjwlk23432d")
    private String newPassword;
    @ApiModelProperty( value = "old password", name = "기존 비밀번호", dataType = "String", example = "sdfsdfkjkj231sfdsf")
    private String oldPassword;
}

