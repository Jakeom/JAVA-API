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
@ApiModel("이메일 인증코드 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq006 {
    @ApiModelProperty( value = "email", name = "이메일 주소", dataType = "String", example = "")
    private String email;
}

