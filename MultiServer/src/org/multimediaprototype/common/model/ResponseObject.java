package org.multimediaprototype.common.model;

import com.aliyun.api.AliyunResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dx.yang on 15/11/12.
 */

/**
 * 后台管理界面RESTful接口统一数据格式
 *
 * 数据包含三个字段<br>
 * code:表示操作是否成功. 0为成功, 非0为失败. 默认值为0<br>
 * data:如果操作成功, data值为成功获取到的数据 <br>
 * error:如果操作失败, error值为错误描述
 */
@ApiModel(
        value = "ResponseObject",
        description = "API约定消息体"
)
public class ResponseObject {
    @ApiModelProperty(value = "API调用状态, 0:成功, 其他:失败")
    private Object code = 0;
    @ApiModelProperty(value = "API调用结果")
    private Object data;
    @ApiModelProperty(value = "API错误信息")
    private Object error;

    public ResponseObject() {}

    public ResponseObject(AliyunResponse res) {
        if (res.isSuccess()) {
            this.data = "ok";
        } else {
            this.code = res.getErrorCode();
            this.data = res.getMessage();
        }
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}

