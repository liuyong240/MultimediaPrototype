package org.multimediaprototype.oss.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.multimediaprototype.auth.service.SiteUserService;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.oss.base.IOSSAPi;
import org.multimediaprototype.oss.dao.impl.OSSManage;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.IOSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@Api(value = "OSS对外接口")
public class OSSController {

    @Autowired
    private IOSSService ossService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private TranscodeService transcodeService;

    private static Logger log = LogManager.getLogger(OSSController.class);

    /**
     * 上传文件
     */
    @RequestMapping(value = "/putObject", method = RequestMethod.POST)
    @ApiOperation("上传文件接口")
    public ResponseObject putTransObject(
            @ApiParam("文件") @RequestParam("multiFile") MultipartFile mulitFile,
            @ApiParam("是否转码") @RequestParam(required = false) Boolean isTranscode)
            throws Exception {

        ResponseObject res = new ResponseObject();
        SiteUserDetail user = siteUserService.getCurrentUser();
        OSSFile ossFile = null;
        try {
            ossFile = ossService.uploadOss(mulitFile, user.getId());
            if (isTranscode) {
                transcodeService.submitTranscodeJobs(user.getId(),
                        ossFile.getObjectName(), ossFile.getBucketName(),
                        ossFile.getLocation());
            }
        } catch (Exception e) {
            // TODO: handle exception
            res.setCode(-1);
            log.error(e.getStackTrace());
            return res;
        }
        res.setData(ossFile);
        return res;
    }
     

    /**
     * 上传文件
     */
    @RequestMapping(value = "/putMultiAndPic", method = RequestMethod.POST)
    @ApiOperation("上传视频和图片文件")
    public ResponseObject putMultiAndPic(
            @ApiParam("视频文件") @RequestParam(required = true) MultipartFile mediaFile,
            @ApiParam("图片文件") @RequestParam(required = true) MultipartFile picFile,
            @ApiParam("对象描述") @RequestParam(required = false, defaultValue = "默认描述") String desc,
            @ApiParam("对象标题") @RequestParam(required = false, defaultValue = "默认标题") String title,
            @ApiParam("是否转码") @RequestParam(required = false, defaultValue = "false") Boolean isTranscode)
            throws Exception {

        ResponseObject res = new ResponseObject();
        SiteUserDetail user = siteUserService.getCurrentUser();
        MediaMapping mediaMapping = null;
        try {
            OSSFile ossMediaFile = ossService.uploadOss(mediaFile, user.getId());
            OSSFile ossPicFile = ossService.uploadOss(picFile, user.getId());
            Integer status = isTranscode ? Constants.MEDIA_STATUS_TRANSCODE_PENDING : Constants.MEDIA_STATUS_CHECK_PENDING;
            mediaMapping = ossService.addMediaMap(ossMediaFile.getId(),
                    ossPicFile.getId(), desc, title, 0L, status);
            if (isTranscode) {
                transcodeService.submitTranscodeJobs(user.getId(), mediaMapping.getId(), ossMediaFile, ossPicFile, desc, title);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getStackTrace());
            res.setCode(-1);
            res.setError(e.getMessage());
            return res;
        }
        res.setData(mediaMapping);
        return res;
    }

    /**
     * @param multiFile
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/putUsersReq", method = RequestMethod.POST)
    public ResponseObject putUsersReq(
            @ApiParam("文件key") @RequestParam("key") String key)
            throws Exception {
        SiteUserDetail user = siteUserService.getCurrentUser();
        OSSFile ossFile = ossService.upUserMeg(key, user.getId());
        ResponseObject res = new ResponseObject();
        res.setData(ossFile);
        return res;

    }

    public static Integer getMyRand() {
        int num = (int) (Math.random() * 1000);
        return num;
    }

    /**
     * 查询用户oss列表文件
     *
     * @param offset   偏移量
     * @param rowCount 每次返回个数
     * @return 返回一个集合，包括用户oss对象
     */
    @RequestMapping(value = "/getUserOssList", method = RequestMethod.GET)
    @ApiOperation("查询用户oss文件列表")
    public ResponseObject getUserOssList(
            @ApiParam("偏移量") @RequestParam(required = false, defaultValue = "1") Integer offset,
            @ApiParam("行数") @RequestParam(required = false, defaultValue = "8") Integer rowCount) {
        SiteUserDetail user = siteUserService.getCurrentUser();
        ResponseObject res = new ResponseObject();
        List<OSSFile> ossFiles = ossService.getUserOssList(user.getId(), offset, rowCount);
        res.setData(ossFiles);
        return res;
    }

    /**
     * 删除oss文件
     *
     * @param id 用户oss文件id
     * @return 删除成功返回oss的id
     */
    @RequestMapping(value = "/deleteOss", method = RequestMethod.GET)
    @ApiOperation("删除用户oss文件")
    public ResponseObject deleteOss(
            @ApiParam("id") @RequestParam(required = true) long id) {
        long i = ossService.deleteOss(id);
        ResponseObject res = new ResponseObject();
        if (i > 0) {
            res.setCode(0);
        } else {
            res.setCode(1);
        }
        res.setData(i);
        return res;
    }

    /**
     * 删除oss视频文件
     *
     * @param id 用户oss文件id
     * @return 删除成功返回oss的id
     */
    @RequestMapping(value = "/deleteMedia", method = RequestMethod.GET)
    @ApiOperation("删除用户oss文件")
    public ResponseObject deleteMedia(
            @ApiParam("id") @RequestParam(required = true) long id) {
        long i = ossService.deleteMediaMap(id);
        ResponseObject res = new ResponseObject();
        if (i > 0) {
            res.setCode(0);
        } else {
            res.setCode(1);
        }
        res.setData(i);
        return res;
    }

}
