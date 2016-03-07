package io.swagger.controller.api;

//import com.wordnik.swagger.annotations.*;
import io.swagger.model.User;
import io.swagger.model.UserList;
import io.swagger.annotations.*;
import org.multimediaprototype.common.model.ResponseObject;
import org.springframework.web.bind.annotation.*;

/**
 * Created by yangdongxu on 15/11/1.
 */

// Swagger 增删改查示例
@RestController
@RequestMapping("/swagger/api/user")
@Api(value = "User接口", description = "用户CRUD操作")
public class UserController {

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", response = ResponseObject.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Invalid ID supplied"),
                    @ApiResponse(code = 404, message = "list not found")
            }
    )
    public ResponseObject add(@ApiParam(value = "用户信息") @RequestBody User u) {
        ResponseObject res = new ResponseObject();
        res.setData("添加成功" + u.getName());
        return res;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", response = ResponseObject.class)
    public ResponseObject get(@ApiParam(value = "用户ID", required = true) @PathVariable Integer id) {
        ResponseObject res = new ResponseObject();
        User u = new User();
        u.setId(id);
        u.setName("mns_test");
        res.setData(u);
        return res;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据id删除用户")
    public ResponseObject delete(@ApiParam(value = "用户ID") @PathVariable Integer id) {

        ResponseObject res = new ResponseObject();

        res.setData("删除成功" + id);
        return res;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "更新用户信息")
    public ResponseObject update(
            @ApiParam(value = "用户ID") @PathVariable Integer id,
            @ApiParam(value = "用户数据") @RequestBody User u
    ) {
        ResponseObject res = new ResponseObject();
        res.setData("更新成功" + id + ":" + u.getName());
        return res;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(
            value = "获取用户列表",
            response = UserList.class,
            notes = "获取用户列表<br>notes可以加入<strong><i>html标签</i></strong>"
    )
    public UserList getAll() {
        UserList res = new UserList();
        int count = 10;
        User list[] = new User[count];
        for (int i = 0; i < count; i++) {
            User newOne = new User();
            newOne.setId(i);
            newOne.setName("User_" + i);
            list[i] = newOne;
        }
        res.setData(list);
        return res;
    }
}
