package com.softpuzzle.angkor.database.mapper.api.privacy;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface PrivacyMapper {

    HashMap getPrivacy(HashMap params) throws Exception;

    Integer updatePrivacy(HashMap params) throws Exception;

}
