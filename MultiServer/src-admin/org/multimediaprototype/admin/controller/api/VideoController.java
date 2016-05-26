package org.multimediaprototype.admin.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.admin.model.VideoDetail;
import org.multimediaprototype.admin.service.VideoService;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.multimediaprototype.auth.service.SiteUserService;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.impl.OSSService;
import org.multimediaprototype.service.FileProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by dx.yang on 15/12/1.
 */


@RestController
@Api("后台视频管理接口")
@RequestMapping("/admin/api/video")
public class VideoController {

    private Logger logger = LogManager.getLogger(this.getClass());


    @Autowired
    private VideoService videoService;

    @Autowired
    private FileProxyService fileProxyService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private OSSService ossService;

    @Autowired
    private TranscodeService transcodeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("获取所有视频列表")
    public ResponseObject videoList(
            @RequestParam(defaultValue = "1", required = false)
            Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false)
            Integer pageSize,
            @RequestParam(required = false)
            Integer status
    ) {

        logger.debug(status);

        ResponseObject res = new ResponseObject();
        Map map = videoService.getVideoList(pageNumber, pageSize, status, null, 0);
        res.setData(map);
        return res;
    }

    @RequestMapping(value = "/listMyVideo", method = RequestMethod.GET)
    @ApiOperation("获取我的视频列表")
    public ResponseObject listMyVideo(
            @RequestParam(defaultValue = "1", required = false)
            Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false)
            Integer pageSize)
    {

        ResponseObject res = new ResponseObject();
        Map map = videoService.getVideoList(pageNumber, pageSize, null, Constants.MEDIA_STATUS_TRANSCODE_PENDING, 1);
        res.setData(map);
        return res;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("获取视频详细信息")
    public ResponseObject getVideoDetail(
            @PathVariable
            Long id
    ) {
        ResponseObject res = new ResponseObject();

        VideoDetail videoDetail = videoService.getDetailById(id);

        res.setData(videoDetail);

        return res;
    }

    @RequestMapping(value = "/file/**", method = RequestMethod.GET)
    @ApiOperation("代理方式下载oss文件")
    public ResponseEntity getFile(
            HttpServletRequest request
    ) {
        String path = request.getServletPath();
        path = StringUtils.replace(path, "/admin/api/video/file/", "");

        ResponseEntity res = null;

        try {
            res = fileProxyService.getByProxyURL(path, request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (res == null) {
            res = new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return res;

    }

    @ApiOperation("更新媒体文件和图片文件")
    @RequestMapping(value = "/updateMediaAndPicStatus", method = RequestMethod.POST)
    public ResponseObject updateMediaAndPic(
            @ApiParam("mediamapping的id")
            @RequestParam(value = "id", required = false)
            Long id,
            @ApiParam("视频文件")
            @RequestParam(required = false)
            MultipartFile mediaFile,
            @ApiParam("截图文件")
            @RequestParam(required = false)
            MultipartFile picFile,
            @ApiParam("描述")
            @RequestParam(required = false)
            String desc,
            @ApiParam("标题")
            @RequestParam(required = false)
            String title,
            @ApiParam("是否删除")
            @RequestParam(required = false)
            Boolean isDelete,
            @ApiParam("状态")
            @RequestParam(required = false)
            Integer status,
            @ApiParam("是否转码")
            @RequestParam(required = false)
            Boolean isTranscode
    ) throws Exception {
        ResponseObject res = new ResponseObject();
        SiteUserDetail user = siteUserService.getCurrentUser();
        Long userId = user.getId();

        Long mediaId = null;
        Long picId = null;

        OSSFile mediaOSSFile = null;
        OSSFile picOSSFile = null;

        if (mediaFile != null) {
            mediaOSSFile = ossService.uploadOss(mediaFile, userId);
            mediaId = mediaOSSFile.getId();
        }

        if (picFile != null) {
            picOSSFile = ossService.uploadOssWithPath(picFile, "snapshot/", userId);
            picId = picOSSFile.getId();
        }

        if (id == null) {
            // id不存在, add, 设置初始状态
            Integer initStatus = isTranscode ? Constants.MEDIA_STATUS_TRANSCODE_PENDING : Constants.MEDIA_STATUS_CHECK_PENDING;
            MediaMapping mediaMapping = ossService.addMediaMap(mediaId, picId, desc, title, 0L, initStatus);
            id = mediaMapping.getId();
            res.setData(mediaMapping);
        } else {
            // id已经存在, update, 直接更新状态
            MediaMapping mediaMapping = ossService.updateMediaMap(id, mediaId, picId, desc, isDelete, status, title);
            res.setData(mediaMapping);
        }

        if (isTranscode && mediaOSSFile != null && picOSSFile != null) {
            // 需要转码, 且文件不为空, 则触发转码
            List<String> jobIds = transcodeService.submitTranscodeJobs(userId, id, mediaOSSFile, picOSSFile, desc, title);
            if (jobIds == null) {
                res.setCode(1);
                res.setError("转码触发失败");
            }
        }

        return res;
    }

}
