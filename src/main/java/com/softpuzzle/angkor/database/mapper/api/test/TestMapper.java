package com.softpuzzle.angkor.database.mapper.api.test;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface TestMapper {

    Integer signUp(HashMap params) throws Exception;
    Integer deleteSamePhoneUser(HashMap params) throws Exception;
    Integer deleteFavorite(HashMap params) throws Exception;
    Integer deleteFriend(HashMap params) throws Exception;
    Integer deleteBlock(HashMap params) throws Exception;
    Integer addUserIcon(HashMap params) throws Exception;
    Integer addUserAgree(HashMap params) throws Exception;
    Integer addSysVersion(HashMap params) throws Exception;
    Integer addSysNoti(HashMap params) throws Exception;
    Integer addSysSecurity(HashMap params) throws Exception;
    Integer addSysFriends(HashMap params) throws Exception;
    Integer addSysChatting(HashMap params) throws Exception;
    Integer addSysDisplay(HashMap params) throws Exception;

    HashMap getUser(HashMap params) throws Exception;
    HashMap getMyId(HashMap params) throws Exception;
    HashMap getUser2(HashMap params) throws Exception;
    HashMap getChatting(HashMap params) throws Exception;
    HashMap getDisplay(HashMap params) throws Exception;
    HashMap getFriends(HashMap params) throws Exception;
    HashMap getNoti(HashMap params) throws Exception;
    HashMap getPrivacy(HashMap params) throws Exception;
    void updateLoginDtAndPublicKey(HashMap params) throws Exception;
    List<HashMap> getQuitUserList() throws Exception;
    List<HashMap> getUserList() throws Exception;
}
