package com.softpuzzle.angkor.database.mapper.api.code;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface CodeMapper {
    List<HashMap> getCodeAllList(HashMap params) throws Exception;

    List<HashMap> getListByCode(HashMap params) throws Exception;

    List<HashMap> getInfoByGroupChild(HashMap params) throws Exception;

    Integer addGroupCode(HashMap params) throws Exception;

    Integer deleteGroupCode(HashMap params) throws Exception;
   
}
