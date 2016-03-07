package org.multimediaprototype.oss.base.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;

public  class UploadPartThread implements Runnable {
    @Autowired
    private OSSClient ossClient;
    
    private File uploadFile;
    private String bucket;
    private String object;
    private long start;
    private long size;
    private List<PartETag> eTags;
    private int partId;
    private OSSClient client;
    private String uploadId;

    UploadPartThread(String bucket, String object,
            File uploadFile,String uploadId, int partId,
            long start, long partSize, List<PartETag> eTags) {
        this.uploadFile = uploadFile;
        this.bucket = bucket;
        this.object = object;
        this.start = start;
        this.size = partSize;
        this.eTags = eTags;
        this.partId = partId;
        this.client = ossClient;
        this.uploadId = uploadId;
    }

    @Override
    public void run() {

        InputStream in = null;
        try {
            in = new FileInputStream(uploadFile);
            in.skip(start);

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucket);
            uploadPartRequest.setKey(object);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(in);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(partId);

            UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

            eTags.add(uploadPartResult.getPartETag());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) try { in.close(); } catch (Exception e) {}
        }
    }
}