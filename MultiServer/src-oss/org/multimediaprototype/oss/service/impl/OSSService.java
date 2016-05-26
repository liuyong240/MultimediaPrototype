package org.multimediaprototype.oss.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.soap.SOAPBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.oss.base.IMultiOSSApi;
import org.multimediaprototype.oss.base.IOSSAPi;
import org.multimediaprototype.oss.dao.IOSSManage;
import org.multimediaprototype.oss.dao.ImediaMap;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.IOSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadResult;

@Service("ossService")
public class OSSService implements IOSSService {

    @Autowired
    private IOSSAPi OSSApi;

    @Autowired
    private IMultiOSSApi multiOSSApi;

    @Autowired
    private IOSSManage OSSManage;

    @Autowired
    private ImediaMap mediaMap;

    @Value("#{propsUtil['aliyunConsole.bucket']}")
    private String defaultBucket;

    @Value("#{propsUtil['aliyunConsole.ossWriteSubdir']}")
    private String defaultSubInputPath;

    @Value("#{propsUtil['aliyunConsole.location']}")
    private String defaultLocation;

    @Value("#{propsUtil['aliyunConsole.ossEndpointHz']}")
    private String ossEndpointHz;
    
    @Value("#{propsUtil['aliyunConsole.demo']}")
    private String mediademo;

    @Value("#{propsUtil['aliyunConsole.ossWriteSubdirdemo']}")
    private String ossWriteSubdirdemo;
    
    private static Logger log = LogManager.getLogger(OSSService.class);

    /**
     * 上传文件到oss
     *
     * @param userId    用户Id
     * @param multiFile 文件内容
     * @return 该文件的oss信息
     */
    @Override
    public OSSFile uploadOss(MultipartFile multiFile, long userId) {
        return uploadOssWithPath(multiFile,
                String.format("%s/%s/", defaultSubInputPath, userId), userId);
    }

    public OSSFile uploadOssWithPath(MultipartFile multiFile, String path,
                                     long userId) {
        String fileType = multiFile.getOriginalFilename().substring(
                multiFile.getOriginalFilename().lastIndexOf("."));
        if (fileType.length() > 6)
            fileType = "";

        String key = path + System.currentTimeMillis() + getMyRand() + fileType;
        OSSFile ossFile = null;
        try {
            if (OSSApi.uploadFile(defaultBucket, key, multiFile) == null) {
                return null;
            }
            ossFile = upUserMeg(key, userId);
        } catch (OSSException | ClientException e) {
            log.error(e.getMessage());
        }
        return ossFile;
    }

    public static Integer getMyRand() {
        int num = (int) (Math.random() * 1000);
        return num;
    }

    /**
     * 查询用户的oss文件列表
     *
     * @param userId   用户Id
     * @param offset   查询偏移量
     * @param rowCount 每次查询的数量
     * @return 该用户的oss信息列表
     */
    @Override
    public List<OSSFile> getUserOssList(Long userId, Integer offset,
                                        Integer rowCount) {
        return OSSManage.getOssList(userId, offset, rowCount);
    }

    /**
     * 根据id删除oss文件
     *
     * @param id oss文件Id
     * @return 删除数量
     */
    @Override
    public long deleteOss(long id) {
        OSSFile aFile = OSSManage.getOssdetail(id);
        String osskeyString = aFile.getUserid().toString() + "/"
                + aFile.getObjectName();
        OSSApi.deleteObject(aFile.getBucketName(), osskeyString);
        return OSSManage.delete(id);
    }

    @Override
    public String uploadWebOss(MultipartFile multFile, String key) {
        String result = null;
        key = "/input/" + key;
        try {
            result = OSSApi.uploadFile(defaultBucket, key, multFile);

        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return result;
    }

    @Override
    public String uploadWebOss(String fileName, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * oss文件信息入库
     *
     * @param key oss文件的key
     * @param uid 用户idId
     * @return 入库数量
     */
    @Override
    public OSSFile upUserMeg(String key, long uid) {
        return upUserMeg(key, uid, defaultBucket);
    }

    @Override
    public OSSFile upUserMeg(String key, long uid, String bucket) {
        OSSFile ossFile = new OSSFile();
        ossFile.setBucketName(bucket);
        ossFile.setUserid(uid);
        ossFile.setObjectUrl("http://" + bucket + "." + defaultLocation
                + ".aliyuncs.com/" + key);
        ossFile.setIsDelete(false);
        ossFile.setGmtCreated(new Date());
        ossFile.setObjectName(key);
        ossFile.setLocation(defaultLocation);
        long i = OSSManage.addOss(ossFile);
        return ossFile;
    }

    @Override
    public String uploadBigFile(File uploadFile, String key) {
        CompleteMultipartUploadResult result = null;
        try {
            result = multiOSSApi.uploadBigFile(defaultBucket, key, uploadFile);
        } catch (OSSException | ClientException e) {
            log.error(e.getMessage());
        }
        return result.getKey();
    }

    /**
     * 根据bucket名字创建bucket
     *
     * @param bucketName oss的bucket名字
     * @return Bucket
     */
    @Override
    public Bucket creatBucket(String bucketName) {
        Bucket bucket = null;
        try {
            bucket = OSSApi.ensureBucket(bucketName);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getStackTrace());
        }
        return bucket;
    }

    /**
     * 根据bucket名字删除bucket
     *
     * @param bucketName oss的bucket名字
     * @return
     */
    @Override
    public void deleteBucket(String bucketName) {
        try {
            OSSApi.deleteBucket(bucketName);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getStackTrace());
        }
    }

    /**
     * 列出用户下的所有bucket
     *
     * @param
     * @return bucketlist
     */
    @Override
    public List<Bucket> listBucket() {
        List<Bucket> buckets = null;
        try {
            buckets = OSSApi.listBucket();
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getStackTrace());
        }
        return buckets;
    }

    /**
     * 创建bucket并设置权限
     *
     * @param bucketName oss的bucket名字
     * @param control    bucket的权限
     * @return Bucket
     */
    @Override
    public Bucket creatandsetBucket(String bucketName,
                                    CannedAccessControlList control) {
        Bucket bucket = null;
        try {
            bucket = OSSApi.creatandsetBucket(bucketName, control);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getStackTrace());
        }
        return bucket;
    }

    /**
     * 添加oss文件对于关系
     *
     * @param mediaId oss视频文件的的id
     * @param picId   oss图片文件的的id
     * @param desc    文件描述
     * @return MediaMapping 返回视频对应关系
     */
    @Override
    public MediaMapping addMediaMap(long mediaId, long picId, String desc, String title, Long fatherMapId, Integer status) {
        // TODO Auto-generated method stub
        MediaMapping mediaMapping = new MediaMapping();
        mediaMapping.setMediaId(mediaId);
        mediaMapping.setPicId(picId);
        mediaMapping.setDescription(desc);
        mediaMapping.setGmtCreated(new Date());
        mediaMapping.setIsDelete(false);
        mediaMapping.setTitle(title);
        mediaMapping.setFather(fatherMapId);
        mediaMapping.setStatus(status);
        Date now = new Date();
        mediaMapping.setGmtCreated(now);
        mediaMapping.setGmtModified(now);
        mediaMap.addMediaMap(mediaMapping);
        return mediaMapping;
    }

    /**
     * 添加oss文件对于关系
     *
     * @param mediaId oss视频文件的的id
     * @param picId   oss图片文件的的id
     * @param desc    文件描述
     * @return MediaMapping 返回视频对应关系
     */
    @Override
    public MediaMapping addMediaMap(long mediaId, long picId, String desc, String title) {
        MediaMapping mediaMapping = new MediaMapping();
        mediaMapping.setMediaId(mediaId);
        mediaMapping.setPicId(picId);
        mediaMapping.setDescription(desc);
        mediaMapping.setGmtCreated(new Date());
        mediaMapping.setIsDelete(false);
        mediaMapping.setTitle(title);
        Date now = new Date();
        mediaMapping.setGmtCreated(now);
        mediaMapping.setGmtModified(now);
        mediaMap.addMediaMap(mediaMapping);
        return mediaMapping;
    }

    /**
     * 更新视频文件对于关系
     *
     * @param id 记录id
     * @param mediaId   视频文件存储id
     * @param picId   视频图片存储id
     * @param desc   视频文件描述
     * @param isDelete   视频文件是否需要删除
     * @param status   视频文件的状态
     * @param title    视频文件的title
     * @return MediaMapping 返回视频对应关系
     */
    @Override
    public MediaMapping updateMediaMap(Long id, Long mediaId, Long picId,
                                       String desc, Boolean isDelete, Integer status, String title) {
        MediaMapping mediaMapping = new MediaMapping();
        mediaMapping.setId(id);
        mediaMapping.setMediaId(mediaId);
        mediaMapping.setPicId(picId);
        mediaMapping.setDescription(desc);
        mediaMapping.setIsDelete(isDelete);
        mediaMapping.setStatus(status);
        mediaMapping.setTitle(title);
        mediaMapping.setGmtModified(new Date());
        mediaMap.updataMediaMap(mediaMapping);
        return mediaMap.getMediaMap(id);
    }

    /**
     * 解析oss url
     *
     * @param url
     * @return
     */
    @Override
    public Map<String, String> parseOSSUrl(String url) {

        url = url.replace("^(https://|http://)", "");
        url = url.replaceAll("^(http://|https://)", "");
        List<String> urlSplited = new ArrayList<>(Arrays.asList(StringUtils
                .split(url, "/")));

        String[] bucketAndEndpoint = StringUtils.split(urlSplited.get(0), ".");
        String bucket = bucketAndEndpoint[0];
        String endpoint = bucketAndEndpoint[1];

        urlSplited.remove(0);
        String filename = StringUtils.join(urlSplited, "/");

        Map<String, String> res = new HashMap<>();
        res.put("endpoint", endpoint);
        res.put("bucket", bucket);
        res.put("object", filename);

        return res;

    }

    /**
     * 更新视频文件状态
     *
     * @param url 视频文件url
     * @param state   视频文件的状态
     * @return  
     */
    @Override
    public void updateMediaMapStatus(String url, String state) {
        OSSFile file = OSSManage.getByUrl(url);
        Long mediaId = file.getId();
        MediaMapping mediaMapping = mediaMap.getMediaMapByMediaId(mediaId);
        Integer status;
        if (state.equals("TranscodeSuccess")) {
            status = Constants.MEDIA_STATUS_CHECK_PENDING;
        } else {
            status = Constants.MEDIA_STATUS_TRANSCODE_FAILED;
        }
        mediaMapping.setStatus(status);
        mediaMapping.setGmtModified(new Date());
        mediaMap.updataMediaMap(mediaMapping);
    }

    /**
     * 删除视频文件
     *
     * @param id 视频文件记录id
     * @return  删除条数
     */
    @Override
    public long deleteMediaMap(Long id) {
        MediaMapping media = mediaMap.getMediaMap(id);
        deleteOss(media.getMediaId());
        deleteOss(media.getPicId());
        return mediaMap.deleteMudiaMap(id);
    }

 
    /**
     * 删除某一个对象下的所有文件
     *
     * @param bucketName ossbucket名字
     * @param prefix 前缀
     * @return 
     */  
    @Override
    public void deleteObjects(String bucketName, String prefix) {
        // TODO Auto-generated method stub
        OSSApi.deleteObjects(bucketName, prefix);
    }

}
