package com.softpuzzle.angkor.database.mapper.api.friends;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;


@Mapper
public interface FriendsMapper {

    HashMap getFriends(HashMap params) throws Exception;

    Integer updateFriends(HashMap params) throws Exception;

}
