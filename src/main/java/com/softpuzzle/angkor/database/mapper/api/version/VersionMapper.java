package com.softpuzzle.angkor.database.mapper.api.version;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface VersionMapper {

    HashMap getVersion(HashMap params) throws Exception;
    Integer updateVersion(HashMap params) throws Exception;
    HashMap getRecentAppVersion(HashMap params) throws Exception;
    String checkAppUpdate(HashMap params) throws Exception;
}
