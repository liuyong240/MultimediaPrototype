package org.multimediaprototype.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by dx.yang on 15/11/20.
 */
public class MTSJobOutput {
    @JsonProperty("OutputObject")
    private String outputObject;
    @JsonProperty("TemplateId")
    private String templateId;
    @JsonProperty("UserData")
    private String userData;
    @JsonProperty("WaterMarks")
    private List<MTSJobOutputWaterMark> waterMarks;

    public String getOutputObject() {
        return outputObject;
    }

    public void setOutputObject(String outputObject) {
        this.outputObject = outputObject;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public List<MTSJobOutputWaterMark> getWaterMarks() {
        return waterMarks;
    }

    public void setWaterMarks(List<MTSJobOutputWaterMark> waterMarks) {
        this.waterMarks = waterMarks;
    }
}
