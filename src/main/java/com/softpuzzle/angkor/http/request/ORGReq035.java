package com.softpuzzle.angkor.http.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("FAQ 가져오기 객체")
public class ORGReq035 {
    @ApiModelProperty( value = "angkor id", name = "앙코르챗 ID", dataType = "String", example = "akadsf1231344334", hidden = true)
    private String angkorId;
    @ApiModelProperty( value = "first category", name = "1차 카테고리", dataType = "Integer", example = "1")
    private Integer fCategory;
    @ApiModelProperty( value = "second category", name = "2차 카테고리", dataType = "Integer", example = "1")
    private Integer sCategory;
    @ApiModelProperty( value = "keyword", name = "keyword", dataType = "String")
    private String keyword;
    @ApiModelProperty(value = "페이징")
    private Paginginfo paging;

}