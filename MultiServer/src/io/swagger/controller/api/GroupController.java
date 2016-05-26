package io.swagger.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.common.model.ResponseObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yangdongxu on 15/11/5.
 */


@RestController
@RequestMapping("/swagger/api/group")
@Api(value = "Group接口", description = "关于用户组操作")
public class GroupController {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "根据id或name查找一个分组")
    public ResponseObject get(
            @ApiParam(value = "组ID") @RequestParam(required = false) Integer id,
            @ApiParam(value = "组名") @RequestParam(required = false) String name
    ) {
        ResponseObject res = new ResponseObject();
        res.setData(id + ":" + name);
        return res;
    }
}




