package org.multimediaprototype.admin.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.admin.model.VideoDetail;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.multimediaprototype.auth.service.SiteUserService;
import org.multimediaprototype.oss.dao.mapper.MediaMappingMapper;
import org.multimediaprototype.oss.dao.mapper.OSSFileMapper;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.service.FileProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by dx.yang on 15/12/1.
 */

@Service("VideoService")
public class VideoService {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private MediaMappingMapper mediaMappingMapper;

    @Autowired
    private OSSFileMapper ossFileMapper;

    @Autowired
    private FileProxyService fileProxyService;

    @Autowired
    private SiteUserService siteUserService;

    /**
     *
     * @param pageNumber 页数, 从1开始
     * @param pageSize  页面大小
     * @param status 状态
     * @param statusExclude 排除字段
     * @param self 1:获取当前用户的视频列表, 0:获取所有用户的视频列表
     * @return
     */
    public Map getVideoList(Integer pageNumber, Integer pageSize, Integer status, Integer statusExclude, Integer self) {

        Map<String, Object> res = new HashMap<>();
        Long userid = null;
        if (self == 1) {
            // 获取当前用户
            SiteUserDetail user = siteUserService.getCurrentUser();
            userid = user.getId();
        }
        List list = mediaMappingMapper.paginQueryEx((pageNumber - 1) * pageSize, pageSize, status, statusExclude, userid);
        Long count = mediaMappingMapper.count(status, statusExclude, userid);

        for(Iterator<Map> i = list.iterator(); i.hasNext(); ) {
            Map item = i.next();
            // format pic url
            String location = "";
            if (item.get("pic_location") != null) {
                location = item.get("pic_location").toString();
            }
            String bucketName = "";
            if (item.get("pic_bucket_name") != null) {
                bucketName = item.get("pic_bucket_name").toString();
            }
            String objectName = "";
            if (item.get("pic_object_name") != null) {
                objectName = item.get("pic_object_name").toString();
            }
            String picUrl = fileProxyService.createProxyURL(location, bucketName, objectName);
            item.put("pic_url", "/admin/api/video/file/" + picUrl);

            // format video url
            if (item.get("video_location") != null) {
                location = item.get("video_location").toString();
            }
            if (item.get("video_bucket_name") != null) {
                bucketName = item.get("video_bucket_name").toString();
            }
            if (item.get("video_object_name") != null) {
                objectName = item.get("video_object_name").toString();
            }
            String videoUrl = fileProxyService.createProxyURL(location, bucketName, objectName);
            item.put("video_url", "/admin/api/video/file/" + videoUrl);
        }

        res.put("list", list);
        res.put("count", count);

        return res;

    }

    /**
     * 获取视频详情
     * @param id   mediamapping id
     * @return
     */
    public VideoDetail getDetailById(Long id) {
        VideoDetail videoDetail = new VideoDetail();


        MediaMapping mediaMapping = mediaMappingMapper.selectByPrimaryKey(id);
        videoDetail.setBaseInfo(mediaMapping);

        OSSFile mediaFile = ossFileMapper.selectByPrimaryKey(mediaMapping.getMediaId());
        String mediaFileUrl = fileProxyService.createProxyURL(
                mediaFile.getLocation(),
                mediaFile.getBucketName(),
                mediaFile.getObjectName()
        );

        OSSFile picFile = ossFileMapper.selectByPrimaryKey(mediaMapping.getPicId());
        String picFileUrl = fileProxyService.createProxyURL(
                picFile.getLocation(),
                picFile.getBucketName(),
                picFile.getObjectName()
        );

        videoDetail.setMediaURL("/admin/api/video/file/" + mediaFileUrl);
        videoDetail.setPicURL("/admin/api/video/file/" + picFileUrl);

        return videoDetail;

    }

}
