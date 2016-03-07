package org.multimediaprototype.sts.controller;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.auth.model.SiteUserDetail;
import org.multimediaprototype.auth.service.SiteUserService;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.sts.service.impl.STSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by haihong.xiahh on 2015/12/22.
 */
@RestController
@RequestMapping("/api/sts")
@Api(value = "sts封装")
public class STSController {
    @Autowired
    private STSService stsService;
    @Autowired
    private SiteUserService siteUserService;


    private Logger logger = LogManager.getLogger(STSController.class);

    @RequestMapping(value = "/assumeWriteRole", method = RequestMethod.GET)
    @ApiOperation("STS assumeWriteRole,将获取到的写权限路径为multimedia-input/user/用户ID(如果用户ID为1位数，左补齐0，如01)")
    public ResponseObject assumeWriteRole() {
        SiteUserDetail user = siteUserService.getCurrentUser();
        String userId = String.valueOf(user.getId());
        if (userId.length() == 1) {
            // assumeRole时，sessionName最小长度为2
            userId = String.format("0%s", userId);
        }
        logger.debug(userId);
        ResponseObject responseObject = new ResponseObject();
        try {
            AssumeRoleResponse response = stsService.assumeWriteRole(userId);
            responseObject.setCode(0);
            responseObject.setData(response);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(e.getMessage());
        }

        return responseObject;
    }

    @RequestMapping(value = "/assumeReadRole", method = RequestMethod.GET)
    @ApiOperation("STS assumeReadRole,将获取到的读权限路径为mulitmedia-input/user/用户ID(如果用户ID为1位数，左补齐0，如01)")
    public ResponseObject assumeReadRole() {
        SiteUserDetail user = siteUserService.getCurrentUser();
        String userId = String.valueOf(user.getId());
        if (userId.length() == 1) {
            // assumeRole时，sessionName最小长度为2
            userId = String.format("0%s", userId);
        }
        logger.debug(userId);
        ResponseObject responseObject = new ResponseObject();
        try {
            AssumeRoleResponse response = stsService.assumeReadRole(userId);
            responseObject.setCode(0);
            responseObject.setData(response);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(e.getMessage());
        }
        return responseObject;
    }
}
