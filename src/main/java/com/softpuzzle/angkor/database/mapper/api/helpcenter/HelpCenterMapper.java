package com.softpuzzle.angkor.database.mapper.api.helpcenter;

import com.softpuzzle.angkor.http.request.Paginginfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


@Mapper
public interface HelpCenterMapper {
    List<HashMap> checkNoticeExpectDt(HashMap params) throws Exception;
    Integer updateNoticeToPublish(List<HashMap> params) throws Exception;
    Integer getNoticeCnt(HashMap params) throws Exception;
    List<HashMap> getNoticeList(Paginginfo paginginfo, HashMap params) throws Exception;
    List<HashMap> checkFaqExpectDt(HashMap params) throws Exception;
    Integer updateFaqToPublish(List<HashMap> params) throws Exception;
    List<HashMap> getFaqFirstCategoryList(HashMap params) throws Exception;
    List<HashMap> getFaqSecondCategoryList(HashMap params) throws Exception;
    Integer getFaqCnt(HashMap params) throws Exception;
    List<HashMap> getFaqList(Paginginfo paginginfo, HashMap params) throws Exception;
    HashMap getFaqDetail(HashMap params) throws Exception;
    Integer setQuestion(HashMap params) throws Exception;
    void insertQnaFile(HashMap params) throws Exception;
    Integer getQuestionCnt(HashMap params) throws Exception;
    List<HashMap> getQuestionList(Paginginfo paginginfo, HashMap params) throws Exception;
    HashMap getQuestionDetail(HashMap params) throws Exception;
    List<HashMap> getQuestionFiles(HashMap params) throws Exception;
    HashMap getQuestionAnswer(HashMap params) throws Exception;
}
