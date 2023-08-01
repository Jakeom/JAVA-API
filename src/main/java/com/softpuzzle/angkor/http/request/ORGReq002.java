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
@ApiModel("상세 코드 정보 확인 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq002 {
    @ApiModelProperty( value = "G001", name = "그룹 코드명", dataType = "String", example = "G001")
    private String code_name;

}

