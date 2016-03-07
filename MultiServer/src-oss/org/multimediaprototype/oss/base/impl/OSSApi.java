package org.multimediaprototype.oss.base.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.oss.base.IOSSAPi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

@Service("OSSApi")
public class OSSApi implements IOSSAPi {

    @Autowired
    private OSSClient ossClient;

    private static Logger log = LogManager.getLogger(OSSApi.class);

    @Override
    public Bucket ensureBucket(String bucketName) throws OSSException,
            ClientException {
        Bucket bucket = null;
        try {
            // 创建bucket
            bucket = ossClient.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKET_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 如果Bucket已经存在，则忽略
                log.info(e.getMessage());
            }
        }
        return bucket;
    }

    @Override
    public void deleteBucket(String bucketName) throws OSSException,
            ClientException {

        ObjectListing ObjectListing = ossClient.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            ossClient.deleteObject(bucketName, objectName);
        }
        ossClient.deleteBucket(bucketName);
    }

    @Override
    public Bucket creatandsetBucket(String bucketName,
            CannedAccessControlList control) throws OSSException,
            ClientException {
        // 创建bucket
        Bucket bucket = ossClient.createBucket(bucketName);
        // 设置bucket的访问权限，public-read-write权限
        ossClient.setBucketAcl(bucketName, control);
        return bucket;
    }

    @Override
    public String uploadFile(String bucketName, String key,
            MultipartFile multFile) {

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(multFile.getSize());
        // 可以在metadata中标记文件类型
        // objectMeta.setContentType("image/jpeg");
        objectMeta.setContentEncoding("UTF-8");

        PutObjectResult result = null;
        try {
            result = ossClient.putObject(bucketName, key,
                    multFile.getInputStream(), objectMeta);
        } catch (OSSException | ClientException | IOException e) {
            // TODO Auto-generated catch block
            log.error(e.getStackTrace());
            // e.printStackTrace();
            return null;
        }
        if (result.hashCode() < 0) {
        }
        return result.getETag();
    }

    @Override
    // 下载文件
    public void downloadFile(String bucketName, String key, String filename)
            throws OSSException, ClientException {
        ossClient.getObject(new GetObjectRequest(bucketName, key), new File(
                filename));
    }

    @Override
    // 删除文件
    public void deleteObject(String bucketName, String key) {
        ossClient.deleteObject(bucketName, key);
    }


    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            log.error(e.getStackTrace());
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getStackTrace());
                }
            }
        }
        return value;
    }

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("D:\\a.jpg");

        OSSClient ossClient = new OSSClient(
                "http://oss-cn-beijing.aliyuncs.com", "HYmwRNeXFrEMLXYp",
                "Aqw38Fzux52inAbDZbmyIfPMz8HZnx");
        PutObjectResult result = ossClient.putObject("yx-get", "123", file);
        File fileNew = new File("D:\\b.jpg");

        String s = getMd5ByFile(file);
        System.out.println(s);
        ossClient.getObject(new GetObjectRequest("yx-get", "123"), fileNew);

        System.out.println(result.getETag());

    }

    @Override
    public List<Bucket> listBucket() {
        // TODO Auto-generated method stub
        return ossClient.listBuckets();
    }

}
