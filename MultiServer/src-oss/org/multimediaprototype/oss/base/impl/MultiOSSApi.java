package org.multimediaprototype.oss.base.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.oss.base.IMultiOSSApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.PartETag;

@Service("MultiOSSApi")
public class MultiOSSApi implements IMultiOSSApi {

    @Autowired
    private OSSClient ossClient;
    private static final long PART_SIZE = 5 * 1024 * 1024L; // 每个Part的大小，最小为5MB
    private static final int CONCURRENCIES = 2; // 上传Part的并发线程数。

    private static Logger log = LogManager.getLogger(MultiOSSApi.class);

    // 通过Multipart的方式上传一个大文件
    // 要上传文件的大小必须大于一个Part允许的最小大小，即5MB。
    @Override
    public CompleteMultipartUploadResult uploadBigFile(String bucketName,
            String key, File uploadFile) {
        // TODO Auto-generated method stub
        int partCount = calPartCount(uploadFile);
        if (partCount <= 1) {
            log.error("要上传文件的大小必须大于一个Part的字节数：" + PART_SIZE);
        }

        String uploadId = initMultipartUpload(bucketName, key);

        ExecutorService pool = Executors.newFixedThreadPool(CONCURRENCIES);

        List<PartETag> eTags = Collections
                .synchronizedList(new ArrayList<PartETag>());

        for (int i = 0; i < partCount; i++) {
            long start = PART_SIZE * i;
            long curPartSize = PART_SIZE < uploadFile.length() - start ? PART_SIZE
                    : uploadFile.length() - start;

            pool.execute(new UploadPartThread(bucketName, key, uploadFile,
                    uploadId, i + 1, PART_SIZE * i, curPartSize, eTags));
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                log.error(e.getStackTrace());
            }
        }

        if (eTags.size() != partCount) {
            log.error("Multipart上传失败，有Part未上传成功。");
        }

        return completeMultipartUpload(bucketName, key, uploadId, eTags);
    }

    // 根据文件的大小和每个Part的大小计算需要划分的Part个数。
    public int calPartCount(File f) {
        int partCount = (int) (f.length() / PART_SIZE);
        if (f.length() % PART_SIZE != 0) {
            partCount++;
        }
        return partCount;
    }

    public String initMultipartUpload(String bucketName, String key) {
        // TODO Auto-generated method stub
        InitiateMultipartUploadRequest initUploadRequest = new InitiateMultipartUploadRequest(
                bucketName, key);
        InitiateMultipartUploadResult initResult = ossClient
                .initiateMultipartUpload(initUploadRequest);
        return initResult.getUploadId();
    }

    private CompleteMultipartUploadResult completeMultipartUpload(
            String bucketName, String key, String uploadId, List<PartETag> eTags) {
        // TODO Auto-generated method stub
        // 为part按partnumber排序
        Collections.sort(eTags, new Comparator<PartETag>() {

            public int compare(PartETag arg0, PartETag arg1) {
                PartETag part1 = arg0;
                PartETag part2 = arg1;

                return part1.getPartNumber() - part2.getPartNumber();
            }
        });

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
                bucketName, key, uploadId, eTags);

        return ossClient
                .completeMultipartUpload(completeMultipartUploadRequest);
    }
}
