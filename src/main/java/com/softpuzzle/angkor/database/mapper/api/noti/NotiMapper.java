package com.softpuzzle.angkor.database.mapper.api.noti;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface NotiMapper {

    HashMap getNoti(HashMap params) throws Exception;

    Integer updateNoti(HashMap params) throws Exception;
}
