package com.softpuzzle.angkor.database.mapper.api.login;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface LoginMapper {

    HashMap getMyId(HashMap params) throws Exception;
    HashMap getUser(HashMap params) throws Exception;
    HashMap getChatting(HashMap params) throws Exception;
    HashMap getDisplay(HashMap params) throws Exception;
    HashMap getFriends(HashMap params) throws Exception;
    HashMap getNoti(HashMap params) throws Exception;
    HashMap getPrivacy(HashMap params) throws Exception;
    void updateLoginDtAndPublicKey(HashMap params) throws Exception;
    Integer updateUuid(HashMap params) throws Exception;
}
