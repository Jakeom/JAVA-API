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
@ApiModel("사용자 푸시 토큰 정보 객체")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ORGReq061 {
    @JsonIgnore
    private String angkorId;
    
    @ApiModelProperty( value = "기기 종류", name = "기기 종류", dataType = "String", example = "a or o or h")
    private String os;
    @ApiModelProperty( value = "푸시토큰", name = "푸시토큰", dataType = "String", example = "cSn1pOZWejN9W5yhc7kw2r:APA91bGiZa028ajhR5YX8DmQJo4f1742-zzG4YGwvVnIDh2eBOU4ctm9cOFp50HSSSHdCqZcfbDCzay4nMdSl5kzbkH-qt482GiZ5WOz6uvUyz6nzrCV3zOz00dymqP9H6i1MbxUOCIl")
    private String token;

}


