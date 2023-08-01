package com.softpuzzle.angkor.database.mapper.api.chatting;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface ChattingMapper {

    HashMap getChatting(HashMap params) throws Exception;
    Integer updateChatting(HashMap params) throws Exception;
    Integer updateChattingToImageQuality(HashMap params) throws Exception;
    Integer updateChattingToVideoQuality(HashMap params) throws Exception;
    Integer checkMyAngKorId(HashMap params) throws Exception;
    List<HashMap> getParentEmoticonList(HashMap params) throws Exception;
    List<HashMap> getChildEmoticonList(HashMap params) throws Exception;
    HashMap getPublicKeyAndInfoOfUser(String angkorId) throws Exception;
    String getPublicKeyOfUser(String angkorId) throws Exception;
    HashMap getPrivacySettingUser(HashMap params) throws Exception;
    HashMap getParentEmoticon(HashMap params) throws Exception;


}
