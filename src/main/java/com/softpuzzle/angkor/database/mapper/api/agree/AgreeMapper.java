package com.softpuzzle.angkor.database.mapper.api.agree;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface AgreeMapper {
    List<HashMap> getAgreeList(HashMap params) throws Exception;

}
