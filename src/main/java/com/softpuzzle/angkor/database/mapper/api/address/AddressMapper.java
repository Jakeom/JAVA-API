package com.softpuzzle.angkor.database.mapper.api.address;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface AddressMapper {

    List<HashMap> getAllAddresList(HashMap params) throws Exception;
    List<HashMap> getFavoriteAddresList(HashMap params) throws Exception;
    HashMap getMyProfileInfo(HashMap params) throws Exception;
    String getAngkorIdByPhoneAndPhoneCode(HashMap params) throws Exception;
    HashMap getAnotherUserProfileInfo(HashMap params) throws Exception;
    HashMap checkBlock(HashMap params) throws Exception;
    Integer insertBlock(HashMap params) throws Exception;
    Integer deleteBlock(HashMap params) throws Exception;
    HashMap checkFavorite(HashMap params) throws Exception;
    HashMap getFriendInfo(HashMap params) throws Exception;
    Integer insertFavorite(HashMap params) throws Exception;
    Integer deleteFavorite(HashMap params) throws Exception;
    Integer updateShareProfile(HashMap params) throws Exception;
    Integer checkFriendAngKorId(String friendAngkorId) throws Exception;
    Integer checkMyAngKorId(HashMap params) throws Exception;
    Integer checkRecommendYn(HashMap params) throws Exception;
    HashMap getMyId(HashMap params) throws Exception;
    Integer insertMyQrCode(HashMap params) throws Exception;
    Integer insertFriend(HashMap params) throws Exception;
    Integer checkBlockByFriendPhoneNumber(HashMap params) throws Exception;
    Integer insertAllMyPhoneAddrFriends(HashMap params) throws Exception;
    List<String> getMyFriendPhoneNumberList(HashMap params) throws Exception;
    String[] getAngkorIdByPhone(String friend_phonenumber) throws Exception;
    String selectOneAngkorIdByPhone(HashMap params) throws Exception;
    void updateFriendNameToBasic(String friend_angkorId, String angkorId) throws Exception;
    Integer deleteFriend(HashMap params) throws Exception;
    List<HashMap> getProfilesImage(HashMap params) throws Exception;
    Integer getFriendPrivacySetting(HashMap params) throws Exception;
    List<HashMap> getFriendProfilesImage(HashMap params) throws Exception;
    HashMap getMyPosition(HashMap params) throws Exception;
    HashMap getBlockFriend(HashMap params) throws Exception;
    HashMap getMyFriend(HashMap params) throws Exception;
    List<HashMap> getAddressListNearBy(HashMap params) throws Exception;
    Integer searchUserByPhoneNumber(HashMap params) throws Exception;
    Integer checkAllowOthers(HashMap params) throws Exception;
    HashMap getAddressInfoByPhone(HashMap params) throws Exception;
    List<HashMap> getMyFriendList(HashMap params) throws Exception;
    List<HashMap> getBlockFriendList(HashMap params) throws Exception;
    List<HashMap> getAddedMeFriendList(HashMap params) throws Exception;
    List<HashMap> getBlockAddressList(HashMap params) throws Exception;
    String[] checkUserAddressList(HashMap params) throws Exception;
    HashMap getFriendYnByUserId(HashMap params, String friendUserId) throws Exception;
}
