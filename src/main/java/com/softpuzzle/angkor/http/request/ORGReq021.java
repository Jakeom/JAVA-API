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
@ApiModel("이메일 확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq021 {
    @ApiModelProperty( value = "이메일 주소", name = "email", dataType = "String", example = "")
    private String email;
    @ApiModelProperty( value = "join(회원가입/수정에서 사용), check(등록된 이메일인지 확인에서 사용)", name = "funcType", dataType = "String", example = "join/check")
    private String funcType;
}

