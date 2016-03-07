package org.multimediaprototype.base.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by haihong.xiahh on 2015/11/12.
 */
public class OSSFileDO {
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getBucket() {
        return bucket;
    }

    public String getObject() {
        return object;
    }

    public String toJsonString() {
        return toJson().toJSONString();
    }

    public String getExternalUrl() {
        return String.format("http://%s.%s.aliyuncs.com/%s", bucket, location, object);
    }

    public String getInternalUrl() {
        return String.format("http://%s.%s-internal.aliyuncs.com/%s", bucket, location, object);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Bucket", bucket);
        jsonObject.put("Location", location);
        jsonObject.put("Object", object);

        return jsonObject;
    }

    public OSSFileDO() {

    }

    public OSSFileDO(String location, String bucket, String object) {
        this.location = location;
        this.bucket = bucket;
        this.object = object;
    }

    private String location;
    private String bucket;
    private String object;
}
