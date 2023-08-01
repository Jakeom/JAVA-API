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
@ApiModel("사용자 폰트 설정 변경 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq059 {
    @JsonIgnore
    private String angkorId;
    @ApiModelProperty( value = "폰트 사이즈", name = "폰트 사이즈", dataType = "String", example = "14")
    private String inFontsize;

}

