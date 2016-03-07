package io.swagger.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangdongxu on 15/11/5.
 */

@ApiModel(
        value = "ResponseObject",
        description = "API约定消息体"
)
@Deprecated
public class ResponseObject {
    @ApiModelProperty(value = "API调用状态, 0:成功, 1:失败")
    private Integer code = 0;
    @ApiModelProperty(value = "API调用结果")
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
