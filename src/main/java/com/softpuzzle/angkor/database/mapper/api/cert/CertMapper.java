package com.softpuzzle.angkor.database.mapper.api.cert;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface CertMapper {

    Integer checkMyAngKorId(HashMap params) throws Exception;
    Integer isValidUuid(HashMap params) throws Exception;
    Integer updateUuid(HashMap params) throws Exception;
    void updatePhoneByUserId(HashMap params) throws Exception;
    HashMap getUser(HashMap params) throws Exception;
    HashMap getUserById(HashMap params) throws Exception;
    Integer insertEmailHist(HashMap params) throws Exception;
    Integer updateEmailHist(HashMap params) throws Exception;
    Integer getEmailHist(HashMap params) throws Exception;
    Integer checkIsUsePhoneNumber(HashMap params) throws Exception;
    Integer updateCntEmailHist(HashMap params) throws Exception;
    Integer cleanCntEmailHist(HashMap params) throws Exception;
    Integer updateLoginDt(HashMap params) throws Exception;
    Integer updateLoginDtAndPublicKey(HashMap params) throws Exception;
    Integer chkEmail24Hours(HashMap params) throws Exception;
    Integer chkEmailTimeOver(HashMap params) throws Exception;
    Integer chkSmsTenOver(HashMap params) throws Exception;
    void updateMyEmail(HashMap params) throws Exception;

}
