package org.multimediaprototype.oss.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.impl.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by haihong.xiahh on 2015/12/22.
 */
@RestController
@RequestMapping("api/oss")
/**
 * OSS 文件上传完成之后的回调接口，参考文档 https://help.aliyun.com/document_detail/oss/api-reference/object/Callback.html?spm=5176.docoss/practice/app_server/callback_server.2.5.DV9Zrn
 */
public class OSSCallbackController {
    private Logger log = LogManager.getLogger(OSSCallbackController.class);

    @Autowired
    private OSSService ossService;
    @Autowired
    private TranscodeService transcodeService;

    @Value("#{propsUtil['aliyunConsole.location']}")
    private String defaultLocation;


    public static class ResponseInfo {
        int status;
        String info;

        public ResponseInfo(int status, String info) {
            this.status = status;
            this.info = info;
        }
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ApiOperation("OSS上传完之后的回调")
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        log.debug("callback received");
        OSSCallbackHandler handler = new OSSCallbackHandler();

        // 签名校验，解析body信息
        ResponseInfo responseObject = handler.doCheck(request);
        if (responseObject.status != 200) {
            sendResponse(response, responseObject);
            return;
        }
        // do something,记录到数据库, 触发转码
        log.debug("trigger transcode");
        responseObject = triggerTranscode(handler.getObject(), handler.getBucket(), handler.getUserId(), handler.isTranscode());
        if (responseObject.status != 200) {
            sendResponse(response, responseObject);
            return;
        }

        // response to OSS
        log.info("response success");
        sendResponse(response, new ResponseInfo(200, "{}"));
    }
    public ResponseInfo triggerTranscode(String object, String bucket, Long userId, boolean isTranscode) {
        OSSFile ossFile = ossService.upUserMeg(object, userId, bucket);
        log.debug("update oss info success");
        if (isTranscode) {
            List<String> jobIds = transcodeService.submitTranscodeJobs(userId, ossFile.getId(), ossFile, null, "from user desc", "from user title");
            if (jobIds != null) {
                int successCount = jobIds.size();
                log.debug(successCount);
                if (successCount < 1) {
                    log.error("trigger transcode failed");
                    return new ResponseInfo(500, "trigger transcode failed");
                }
            }
            log.debug("trigger transcode success");
        }
        return new ResponseInfo(200, "");
    }

    private void sendResponse(HttpServletResponse response, ResponseInfo responseObject) {
        response.addHeader("Content-Length", String.valueOf(responseObject.info.length()));
        try {
            response.getWriter().println(responseObject.info);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        response.setStatus(responseObject.status);
    }



}
