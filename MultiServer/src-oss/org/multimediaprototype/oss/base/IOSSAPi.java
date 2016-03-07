package org.multimediaprototype.oss.base;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;

public interface IOSSAPi {

    Bucket ensureBucket(String bucketName);
    void deleteBucket(String bucketName);
    Bucket creatandsetBucket(String bucketName,CannedAccessControlList control);
    List<Bucket> listBucket();

    String uploadFile(String bucketName, String key, MultipartFile multFile);
    void downloadFile(String bucketName, String key, String filename);
    void deleteObject(String bucketName,String key);

}
