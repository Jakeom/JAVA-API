package com.softpuzzle.angkor.database.mapper.api.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface UserMapper {

    Integer checkIsUserId(HashMap params) throws Exception;
    HashMap getMyId(HashMap params) throws Exception;
    Integer signUp(HashMap params) throws Exception;
    Integer deleteSamePhoneUser(HashMap params) throws Exception;
    Integer deleteFavorite(HashMap params) throws Exception;
    Integer deleteFriend(HashMap params) throws Exception;
    Integer deleteBlock(HashMap params) throws Exception;
    void updateFriendNickName(HashMap params) throws Exception;
    void updateFavoriteNickName(HashMap params) throws Exception;
    Integer addUserIcon(HashMap params) throws Exception;
    Integer addUserAgree(HashMap params) throws Exception;
    Integer addSysVersion(HashMap params) throws Exception;
    Integer addSysNoti(HashMap params) throws Exception;
    Integer addSysSecurity(HashMap params) throws Exception;
    Integer addSysFriends(HashMap params) throws Exception;
    Integer addSysChatting(HashMap params) throws Exception;
    Integer addSysDisplay(HashMap params) throws Exception;
    HashMap getUser(HashMap params) throws Exception;
    String getMainProfileUrl(HashMap params) throws Exception;
    String getRecentProfileUrl(HashMap params) throws Exception;
    Integer checkIsUsePhoneNumber(HashMap params) throws Exception;
    Integer deleteUser(HashMap params) throws Exception;
    Integer updateEmail(HashMap params) throws Exception;
    void checkAndUpdateEmailIsUsed(HashMap params) throws Exception;
    Integer updatePhone(HashMap params) throws Exception;
    Integer updatePw(HashMap params) throws Exception;
    Integer updatePwChange(HashMap params) throws Exception;
    Integer updateInfo(HashMap params) throws Exception;
    Integer updateLanguage(HashMap params) throws Exception;
    Integer checkPositionCnt(HashMap params) throws Exception;
    Integer updatePosition(HashMap params) throws Exception;
    Integer insertPosition(HashMap params) throws Exception;
    HashMap checkMyProfileImage(HashMap params) throws Exception;
    Integer updateProfile(HashMap params) throws Exception;
    Integer hideProfile(HashMap params) throws Exception;
    Integer deleteProfile(HashMap params) throws Exception;
    Integer checkDau(HashMap params) throws Exception;
    Integer insertDau(HashMap params) throws Exception;
    Integer insertUserActivation(HashMap params) throws Exception;
    
    HashMap getRegistEmailUser(HashMap params) throws Exception;
    Integer updateUserId(HashMap params) throws Exception;


    Integer insertTest(HashMap params) throws Exception;
    Integer updateUserChatSound(HashMap params) throws Exception;
    String getUserChatSound(HashMap params) throws Exception;
    
    Integer insertUserPushToken(HashMap params) throws Exception;
    
    HashMap getUserPushToken(HashMap params) throws Exception;
    
    Integer isnertMessagePayload(HashMap params) throws Exception;
    List<String> getMessagePayload(HashMap params) throws Exception;
    
    Integer updateUserOnoff(HashMap params) throws Exception;
    String getUserOnoff(HashMap params) throws Exception;
    
    List<String> getNoTokenUser(HashMap params) throws Exception;
    
}
