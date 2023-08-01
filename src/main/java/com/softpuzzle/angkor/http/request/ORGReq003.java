package com.softpuzzle.angkor.http.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ApiModel("코드 관리 객체")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq003 {

    @ApiModelProperty( value = "G003", name = "그룹코드", dataType = "String", example = "G003")
    private String group_code;
    @ApiModelProperty( value = "코드명", name = "그룹코드명[en]", dataType = "String", example = "ENG")
    private String group_code_name_en;
    @ApiModelProperty( value = "코드명", name = "그룹코드명[kher]", dataType = "String", example = "KHMER")
    private String group_code_name_khmr;
    @ApiModelProperty( value = "1", name = "그룹코드 종류[1:삭제불가, 2:삭제가능]", dataType = "String", example = "1")
    private int group_code_type;
    @ApiModelProperty( value = "설명", name = "그룹코드 설명", dataType = "String", example = "설명")
    private String remark;
    @ApiModelProperty( value = "admin", name = "생성자 ID", dataType = "String", example = "admin")
    private String create_id;
}

