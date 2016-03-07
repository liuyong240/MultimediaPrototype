package org.multimediaprototype.auth.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.auth.service.SiteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dx.yang on 15/11/24.
 */


@Api("权限管理接口")
@RestController
@RequestMapping("/auth/api")
public class AuthApiController {

    @Autowired
    private SiteUserService siteUserService;

    @ApiOperation("新用户注册接口")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseObject signup(
            @ApiParam(value = "用户名", required = true)
            @RequestParam("username")
            String username,
            @ApiParam(value = "密码", required = true)
            @RequestParam("password")
            String password
    ) {
        ResponseObject res = new ResponseObject();
        Integer result = null;
        try {
            result = siteUserService.insert(username, password);
            if (result == null) {
                res.setCode(1);
                res.setError("注册失败!");
            }
        } catch (Exception e) {
            res.setCode(1);
            res.setError(e.getMessage());
        }
        return res;
    }

}
