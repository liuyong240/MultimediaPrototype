package org.multimediaprototype.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dx.yang on 15/11/20.
 */
public class MTSJobOutputWaterMarkInputFile {
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("Object")
    private String object;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
