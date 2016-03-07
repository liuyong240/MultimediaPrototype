package org.multimediaprototype.mns.controller;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.mns.model.Message;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.multimediaprototype.mns.model.notify.handler.MNSHandler;
import org.multimediaprototype.mns.service.impl.MNSService;
import org.multimediaprototype.mts.dao.impl.MTSJobHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/12/1.
 */
@RestController
//@RequestMapping("api/mns")
/**
 * MTS视频转码完成后的回调接口实现，参考文档https://help.aliyun.com/knowledge_detail/6675119.html
 */
public class NotifyController {

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(HttpRequestHandler.class);
    @Autowired
    private MNSService mnsService;
    @Autowired
    private MTSJobHistoryService mtsJobHistoryService;

    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    @ApiOperation("转码任务成功后的回调服务")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws MethodNotSupportedException, IOException {
        // 参考http://help.aliyun.com/knowledge_detail/6675119.html
        // 1. 签名校验,check headers中的签名
        // 2. 内容解析 取出消息内容
        // 3. 更新JobHistory
        MNSHandler mnsHandler = new MNSHandler();

        //check signature
        String method = request.getMethod().toUpperCase(Locale.ENGLISH);
        logger.debug("NotifyController step 1:\t" );

        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            logger.error(method + " method not supported");
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;

        }

        logger.debug("NotifyController step 2:\t" );


        Map<String, String> hm;
        hm = mnsHandler.getHeadersInfo(request);

        String target = request.getRequestURI();

        Element notify = mnsHandler.parserBodyContent(request);
        if (notify == null) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        logger.debug("NotifyController step 3:\t" );

        String cert = mnsHandler.safeGetElementContent(notify, "SigningCertURL");
        logger.debug("SigningCertURL:\t" + cert);
        logger.debug("NotifyController step 4:\t" );

        if (!mnsHandler.authenticate(method, target, hm, cert)) {
            logger.debug("authenticate fail");
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return;
        }

        logger.debug("authenticate:\t" + "authenticate is success");
        //get message
        String msg = mnsHandler.safeGetElementContent(notify, "Message");
        logger.debug("Message:\t" + new String(org.apache.commons.codec.binary.Base64.decodeBase64(msg.getBytes())));

        JSONObject jasonObject = JSONObject.parseObject(msg);
        String jobid = (String) ((Map) jasonObject).get("jobId");
        String status = (String) ((Map) jasonObject).get("state");

        //update JobHistory
        mtsJobHistoryService.updateNotifiedStatus(jobid, status);

        response.setStatus(HttpStatus.SC_NO_CONTENT);
        logger.debug("NotifyController step 5:\t" );

    }

    @RequestMapping(value = "/receive", method = RequestMethod.GET)
    @ApiOperation("receive message")
    public Message receiveMessage() {
        return mnsService.receiveMessage(null);
    }
}
