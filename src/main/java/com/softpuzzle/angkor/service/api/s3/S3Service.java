package com.softpuzzle.angkor.service.api.s3;

import com.softpuzzle.angkor.service.api.s3.storage.AmazonS3ResourceStorage;
import com.softpuzzle.angkor.utility.NmpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

  private final AmazonS3ResourceStorage amazonS3ResourceStorage;
  public String save(MultipartFile multipartFile, String dir) {

    String fileId = NmpUtils.getUniqueName();
    String format = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") +1).toLowerCase();
    String path = NmpUtils.createPath(fileId, format, dir);
    String url = amazonS3ResourceStorage.store(path, multipartFile, dir);
    return url;
  }
}
