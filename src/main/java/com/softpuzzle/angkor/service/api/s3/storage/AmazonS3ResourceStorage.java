package com.softpuzzle.angkor.service.api.s3.storage;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.utility.NmpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class AmazonS3ResourceStorage {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  private final AmazonS3Client amazonS3Client;

  public String store(String fullPath, MultipartFile multipartFile, String dir) {

    String fileUname = NmpUtils.getUniqueName() + "." + NmpUtils.getFormat(multipartFile.getContentType());
    String urlPath = NmpUtils.FILE_SEPARATOR + dir + NmpUtils.FILE_SEPARATOR + NmpUtils.getTodayString();
    String uploadPath = NmpUtils.UPLOAD_PATH + urlPath;
    String fileSavePath = uploadPath + NmpUtils.FILE_SEPARATOR + fileUname;
    File file	= new File(uploadPath);
    log.info("file : " + file);
    try {
      if(!file.exists()) {
        file.mkdirs();
      }

      file	= new File(fileSavePath);
      multipartFile.transferTo(file);

      amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file).withCannedAcl(CannedAccessControlList.PublicRead));

      return amazonS3Client.getUrl(bucket,fullPath).toString();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException();

    } finally {
      // 로컬파일 삭제
      if (file.exists()) {
        file.delete();
      }
    }
  }
}