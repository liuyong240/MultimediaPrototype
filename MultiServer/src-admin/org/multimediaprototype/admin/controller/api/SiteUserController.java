package org.multimediaprototype.admin.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.multimediaprototype.auth.model.SiteUser;
import org.multimediaprototype.auth.service.SiteUserService;
import org.multimediaprototype.common.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by dx.yang on 15/11/26.
 */

@RestController
@Api("用户管理接口")
@RequestMapping("/admin/api/user")
public class SiteUserController {

    private Logger logger = LogManager.getLogger(SiteUserController.class);

    @Autowired
    private SiteUserService siteUserService;

    @ApiOperation("获取用户列表")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseObject getUsers() {
        logger.info("123");
        List<SiteUser> list = siteUserService.get(null, null);
        ResponseObject res = new ResponseObject();
        res.setData(list);
        return res;
    }

    @ApiOperation("根据id查找用户")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseObject getUserById(
            @ApiParam(value = "用户名", required = true)
            @PathVariable
            Long id
    ) {

        ResponseObject res = new ResponseObject();

        List<SiteUser> list = siteUserService.get(id, null);
        logger.info(id);
        logger.info(list);
        if (list.size() == 0) {
            res.setCode(1);
        } else {
            res.setData(list.get(0));
        }

        return res;

    }

    @ApiParam("更新用户状态")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseObject updateUserState(
            @RequestBody
            Map<String, Object> params
    ) {
        ResponseObject res = new ResponseObject();

        Long id = new Long(params.get("id").toString());
        String username = (String)params.get("username");
        String password = (String)params.get("password");
        String authorities = (String)params.get("authorities");
        Boolean enabled = (Boolean)params.get("enabled");

        try {
            siteUserService.update(id, username, password, authorities, enabled);
        } catch (Exception e) {
            res.setCode(1);
            res.setError(e.getMessage());
        }

        return res;
    }


}
