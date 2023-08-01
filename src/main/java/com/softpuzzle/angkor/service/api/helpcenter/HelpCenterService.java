package com.softpuzzle.angkor.service.api.helpcenter;

import com.softpuzzle.angkor.database.mapper.api.helpcenter.HelpCenterMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.request.Paginginfo;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.service.api.s3.S3Service;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpCenterService {
	@Autowired
	private S3Service s3Service;
	@Autowired
	private CertService certService;
	@Autowired
	private HelpCenterMapper dao;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public ResTrDTO checkNoticeExpectDt() {

		int cnt=0;

		// 1. 현재 날짜 시간 가져오기
		String queryCondition = String.format("expect_dt <= '%s'", DateUtil.formatDateToyyyymmddhhmm());

		HashMap params = new HashMap();
		params.put("queryCondition", queryCondition);

		try {
			List<HashMap> list = dao.checkNoticeExpectDt(params);
			if(list.size()>0){
				cnt = dao.updateNoticeToPublish(list);
			}
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getNoticeList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		params.put("language", request.getHeader("Language"));

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("language"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			int totalCnt = dao.getNoticeCnt(params);

			Paginginfo pagingInfo = PagingUtil.setPaging((Paginginfo) params.get("paging"));
			params.put("language", request.getHeader("Language"));
			List<HashMap> list = dao.getNoticeList(pagingInfo, params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage(), pagingInfo.getPagingSize(), pagingInfo.getPage(), totalCnt);
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO checkFaqExpectDt() {

		int cnt=0;

		// 1. 현재 날짜 시간 가져오기
		String queryCondition = String.format("expect_dt <= '%s'", DateUtil.formatDateToyyyymmddhhmm());

		HashMap params = new HashMap();
		params.put("queryCondition", queryCondition);

		try {
			List<HashMap> list = dao.checkFaqExpectDt(params);
			if(list.size()>0){
				cnt = dao.updateFaqToPublish(list);
			}
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getFaqCategoryList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		params.put("language", request.getHeader("Language"));

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("language"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			HashMap response = new HashMap();
			response.put("fCategory", dao.getFaqFirstCategoryList(params));
			response.put("sCategory", dao.getFaqSecondCategoryList(params));
			return CommonUtil.setResponseObject(response,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getFaqList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		params.put("language", request.getHeader("Language"));

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("language"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		if(!StringUtils.isBlank((String) params.get("keyword"))){
			// 특수문자 제거를 위한 정규식 패턴 설정
			String pattern = "\\p{Punct}";
			Pattern punctPattern = Pattern.compile(pattern);
			String cleanText = punctPattern.matcher((String) params.get("keyword")).replaceAll("");
			params.put("keyword", cleanText);
		}

		try {
			int totalCnt = dao.getFaqCnt(params);

			Paginginfo pagingInfo = PagingUtil.setPaging((Paginginfo) params.get("paging"));
			List<HashMap> list = dao.getFaqList(pagingInfo, params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage(), pagingInfo.getPagingSize(), pagingInfo.getPage(), totalCnt);
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getFaqDetail(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		params.put("language", request.getHeader("Language"));

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			HashMap map = dao.getFaqDetail(params);
			return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO setQuestion(HttpServletRequest request, MultipartFile[] files, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || params.get("category")==null) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("title")) || StringUtils.isBlank((String)params.get("content"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		int cnt=0;
		long totalSize = 0;

		if(files!=null){
			for (MultipartFile file : files) {
				totalSize += file.getSize();
			}
			if (totalSize > 30 * 1024 * 1024) {
				return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.FILE_SIZE_OVER.getErrorcode() + "", CommonErrorCode.FILE_SIZE_OVER.getGmessage());
			}

			// 허용된 파일 형식
			String[] allowedExtensions = { "jpg", "jpeg", "png", "mp4", "mov" };

			// 파일 형식 및 크기 확인
			for (MultipartFile file : files) {
				if (!FileUtil.isFileExtensionAllowed(file.getOriginalFilename(), allowedExtensions)) {
					return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.FILE_FORMAT_ERROR.getErrorcode() + "", CommonErrorCode.FILE_FORMAT_ERROR.getGmessage());
				}
			}
		}

		HashMap response = new HashMap();

		try {
			// 1. insert my question
			params.put("content", URLDecoder.decode((String) params.get("content"), "UTF-8"));
			cnt = dao.setQuestion(params);

			if(cnt>0 && files!=null) {
				// 2. files s3 upload
				for (MultipartFile file : files) {
					Long fileSize = file.getSize();
					String fileName = file.getOriginalFilename();
					String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

					String thumbImageUrl = null;
					MultipartFile thumbFile = null;
					if(fileExtension.equals("mp4") || fileExtension.equals("mov")){
						thumbFile = FileUtil.generateThumbnailAndUpload(file);
					}else{
						thumbFile = FileUtil.compressImage(file);
					}

					if(thumbFile!=null){
						thumbImageUrl = s3Service.save(thumbFile, "upload/question"); // question : 디렉터리명
						log.info("thumb url : " + thumbImageUrl);
					}

					String url = s3Service.save(file, "upload/question"); // question : 디렉터리명
					log.info("url : " + url); // https://angkorchat-bucket.s3.ap-southeast-1.amazonaws.com/upload/test/00bfd8f1205040d6863dabb5ccdbe739.png

					// insert ank_sys_qna_file
					HashMap iMap = new HashMap();
					iMap.put("inquiryId", params.get("inquiryId"));
					iMap.put("angkorId", params.get("angkorId"));
					iMap.put("fileName", fileName);
					iMap.put("path", url);
					iMap.put("size", fileSize);
					iMap.put("thumb", thumbImageUrl);
					dao.insertQnaFile(iMap);
				}
			}
			if(request.getHeader("Language").equals("en")){
				return CommonUtil.setResponseTrObject(response, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorKhmerCode.SUCCESS.getErrorcode() +"",CommonErrorKhmerCode.SUCCESS.getGmessage());
			}
		} catch (Exception e) {
			if(request.getHeader("Language").equals("en")){
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.QUESTION_SET_SEVER_ERROR.getErrorcode() +"",CommonErrorCode.QUESTION_SET_SEVER_ERROR.getGmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorKhmerCode.QUESTION_SET_SEVER_ERROR.getErrorcode() +"",CommonErrorKhmerCode.QUESTION_SET_SEVER_ERROR.getGmessage());
			}
		}
	}

	public ResCommDTO getQuestionList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			params.put("language", request.getHeader("Language"));
			int totalCnt = dao.getQuestionCnt(params);
			Paginginfo pagingInfo = PagingUtil.setPaging((Paginginfo) params.get("paging"));
			List<HashMap> list = dao.getQuestionList(pagingInfo, params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage(), pagingInfo.getPagingSize(), pagingInfo.getPage(), totalCnt);
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getQuestionDetail(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		HashMap response = new HashMap<>();
		try {
			// 1. get Question
			params.put("language", request.getHeader("Language"));
			HashMap map = dao.getQuestionDetail(params);
			if(map!=null) {
				// 2. get Question -> get files
				List<HashMap> files = dao.getQuestionFiles(params);
				for(HashMap file : files){

					Integer width = null;
					Integer height = null;

					String imageUrl = (String) file.get("fileUrl");
					String thumbUrl = (String) file.get("fileThumb");
					URL url = new URL(imageUrl);

					String filePath = url.getPath();
					String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
					String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

					if(!(fileExtension.equals("mp4") || fileExtension.equals("mov"))){
						HashMap wh = FileUtil.getFilePixelSize(url);
						width = (Integer) wh.get("width");
						height = (Integer) wh.get("height");
					}

					if(!StringUtils.isBlank(thumbUrl)){
						url = new URL(thumbUrl);
						HashMap wh = FileUtil.getFilePixelSize(url);
						width = (Integer) wh.get("width");
						height = (Integer) wh.get("height");
					}

					String mimeType = Files.probeContentType(Path.of(filePath));

					file.put("mimeType", mimeType);
					file.put("width", width);
					file.put("height", height);
				}

				map.put("fileList", files);
				response.put("question", map);

				// 3. check question answered
				if (map.get("status").equals("Answered") || map.get("status").equals("បានឆ្លើយ")) {
					HashMap aMap = dao.getQuestionAnswer(params);
					if (aMap != null) {
						response.put("answer", aMap);
					} else {
						response.put("answer", null);
					}
				} else {
					response.put("answer", null);
				}
			}
			return CommonUtil.setResponseObject(response,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
