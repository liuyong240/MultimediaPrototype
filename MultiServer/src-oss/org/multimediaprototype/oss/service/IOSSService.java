package org.multimediaprototype.oss.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;


public interface IOSSService {

      OSSFile uploadOss(MultipartFile multiFile,long userId);
      OSSFile uploadOssWithPath(MultipartFile multiFile, String path, long userId);

      List<OSSFile> getUserOssList(Long userid,Integer offset,Integer rowCount);
      long deleteOss(long id);
   	  String uploadWebOss(MultipartFile file,String key);
      String uploadWebOss(String fileName,String key);
      OSSFile upUserMeg(String key,long uid);
      OSSFile upUserMeg(String key,long uid, String bucket);
      String uploadBigFile(File uploadFile,String key);
      
      Bucket creatBucket(String bucketName);
      Bucket creatandsetBucket(String bucketName,CannedAccessControlList control);
      void deleteBucket(String bucketName);
      List<Bucket> listBucket();
      MediaMapping addMediaMap(long mediaId,long picId,String desc, String title);
      MediaMapping addMediaMap(long mediaId,long picId,String desc, String title, Long fatherMapId, Integer status);
      MediaMapping updateMediaMap(Long id, Long mediaId, Long picId, String desc, Boolean isDelete, Integer status, String title);
      long deleteMediaMap(Long id);
      Map<String, String> parseOSSUrl(String url);

      void updateMediaMapStatus(String url, String state);

}
