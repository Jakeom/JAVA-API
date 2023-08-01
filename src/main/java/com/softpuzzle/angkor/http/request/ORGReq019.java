package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("사용자 정보 변경 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq019 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "profile 메시지", name = "profile 메시지", dataType = "String", example = "hello!")
    private String profileMessage;
    @ApiModelProperty( value = "profile url", name = "profile url", dataType = "String", example = "http://img.angkorChat.com/image/alice.png")
    private String profileUrl;
    @ApiModelProperty( value = "profile status", name = "profile status", dataType = "Integer", example = "1")
    private String profileStatus;
    @ApiModelProperty( value = "회원이름", name = "회원이름", dataType = "String", example = "angkor")
    private String userName;
    @ApiModelProperty( value = "생년월일", name = "생년월일 ", dataType = "String", example = "2023-03-03")
    private String birth;
    @ApiModelProperty( value = "성별", name = "성별", dataType = "Integer", example = "1")
    private String gender;
    @ApiModelProperty( value = "고유식별자", name = "고유식별자 ", dataType = "String", example = "ABCD-1234...")
    private String uuid;
    @ApiModelProperty( value = "프로필 공개/비공개", name = "프로필 공개/비공개", dataType = "Integer", example = "true")
    private Boolean privacyProfile;
}

