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
@ApiModel("회원가입 테스트 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq100 {
    @ApiModelProperty( value = "전화국가코드", name = "전화국가코드", dataType = "String", example = "82")
    private String phoneCode;
    @ApiModelProperty( value = "국가코드 포함 전화번호", name = "국가코드 포함 전화번호", dataType = "String", example = "821022322232")
    private String phoneNumber;
    @ApiModelProperty( value = "회원이름", name = "회원이름", dataType = "String", example = "angkor")
    private String userName;
}

