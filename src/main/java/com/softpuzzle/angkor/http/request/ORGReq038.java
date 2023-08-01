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
@ApiModel("문의하기 등록 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq038 {
    @ApiModelProperty( value = "email", name = "답변받을 이메일", dataType = "String", example = "angkor@angkor.com")
    private String email;
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334")
    private String angkorId;
    @ApiModelProperty( value = "category \n [1:General, 2:Angkor Account, 3:Chat, 4:Voice Call/Video Call]", name = "category", dataType = "Integer", example = "1")
    private Integer category;
    @ApiModelProperty( value = "title", name = "문의 제목", dataType = "String")
    private String title;
    @ApiModelProperty( value = "content", name = "문의 내용", dataType = "String")
    private String content;
}