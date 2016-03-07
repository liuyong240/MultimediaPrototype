package org.multimediaprototype.oss.base;

import java.io.File;

import com.aliyun.oss.model.CompleteMultipartUploadResult;

/**
 *  支持大文件分片上传
 */
public interface IMultiOSSApi {
    CompleteMultipartUploadResult uploadBigFile(String bucketName, String key, File uploadFile);
}
