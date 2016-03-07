package org.multimediaprototype.admin.model;

import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;

/**
 * Created by dx.yang on 15/12/1.
 */
public class VideoDetail {
    private MediaMapping baseInfo;
    private String mediaURL;
    private String picURL;

    public MediaMapping getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(MediaMapping baseInfo) {
        this.baseInfo = baseInfo;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}
