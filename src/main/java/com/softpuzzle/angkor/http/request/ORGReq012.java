package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("회원가입 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq012 {
    @ApiModelProperty( value = "이메일", name = "이메일", dataType = "String", example = "")
    private String email;
    @ApiModelProperty( value = "아이디", name = "회원 아이디", dataType = "String", example = "userId1234")
    private String userId;
    @ApiModelProperty( value = "비밀번호", name = "비밀먼호(암호처리된 데이터)", dataType = "String", example = "adkfajk23435jksjd")
    private String password;
    @ApiModelProperty( value = "전화국가코드", name = "전화국가코드", dataType = "String", example = "82")
    private String phoneCode;
    @ApiModelProperty( value = "국가코드 포함 전화번호", name = "국가코드 포함 전화번호", dataType = "String", example = "821022322232")
    private String phoneNumber;
    @ApiModelProperty( value = "profile 메시지", name = "profile 메시지", dataType = "String", example = "hello!")
    private String profileMessage;
    @ApiModelProperty( value = "회원이름", name = "회원이름", dataType = "String", example = "angkor")
    private String userName;
    @ApiModelProperty( value = "고유식별자", name = "고유식별자 ", dataType = "String", example = "ABCD-1234...")
    private String uuid;
    @ApiModelProperty( value = "개인 공유키", name = "개인 공유키", dataType = "String", example = "MFwwDQYJKoZIhvcNAQEBBQA...")
    private String publicKey;
    @ApiModelProperty( value = "생년월일", name = "생년월일 ", dataType = "String", example = "2023-03-03")
    private String birth;
    @ApiModelProperty( value = "성별", name = "성별", dataType = "Integer", example = "1")
    private String gender;
    @ApiModelProperty( value = "en:영어,khr:크메르어", name = "선택 언어코드", dataType = "String", example = "en")
    private String language;
    @ApiModelProperty( value = "ANDROID/IOS", name = "전화 OS유형", dataType = "String", example = "ANDROID")
    private String osType;
    @ApiModelProperty( value = "app version", name = "앙코르챗 앱 버전", dataType = "String", example = "1.1")
    private String version;
    @ApiModelProperty( value = "약관동의 항목 리스트", name = "약관동의 항목 리스트", dataType = "List", allowableValues = "'['{'termOrd': '1','agreement': 'Y'}','{'termOrd': '2','agreement': 'N'}']'")
    private List<ORGReq020> agreeList;
}

