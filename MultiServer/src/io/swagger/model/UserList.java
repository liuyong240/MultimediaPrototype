package io.swagger.model;

//import com.wordnik.swagger.annotations.ApiModel;
//import com.wordnik.swagger.annotations.ApiModelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangdongxu on 15/11/5.
 */
@ApiModel(value = "UserList")
public class UserList extends ResponseObject {
    @ApiModelProperty(value = "用户列表")
    private User[] data;

    @Override
    public User[] getData() {
        return data;
    }

    public void setData(User[] data) {
        this.data = data;
    }
}
