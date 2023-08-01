package com.softpuzzle.angkor.database.mapper.api.display;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface DisplayMapper {

    HashMap getDisplay(HashMap params) throws Exception;
    Integer updateDisplay(HashMap params) throws Exception;
    List<HashMap> getBackgroundColor(HashMap params) throws Exception;
    Integer setBackgroundColor(HashMap params) throws Exception;
    Integer setFontSize(HashMap params) throws Exception;

}
