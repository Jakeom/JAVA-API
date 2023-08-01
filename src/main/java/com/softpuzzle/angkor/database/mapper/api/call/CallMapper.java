package com.softpuzzle.angkor.database.mapper.api.call;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CallMapper {

    Integer checkMyAngKorId(HashMap params) throws Exception;
    HashMap getFriendInfo(HashMap params) throws Exception;
    Integer insertBlockCallLog(HashMap params) throws Exception;
    List<HashMap> getCallLog(HashMap params) throws Exception;
    Integer insertCallLog(HashMap params) throws Exception;
    Integer checkCallBlock(HashMap params) throws Exception;
    Integer insertCallLogExtra(HashMap params) throws Exception;
}
