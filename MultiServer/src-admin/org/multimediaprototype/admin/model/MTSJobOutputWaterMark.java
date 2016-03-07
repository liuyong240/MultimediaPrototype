package org.multimediaprototype.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dx.yang on 15/11/20.
 */
public class MTSJobOutputWaterMark {
    @JsonProperty("InputFile")
    private MTSJobOutputWaterMarkInputFile inputFile;
    @JsonProperty("WaterMarkTemplateId")
    private String waterMarkTemplateId;

    public MTSJobOutputWaterMarkInputFile getInputFile() {
        return inputFile;
    }

    public void setInputFile(MTSJobOutputWaterMarkInputFile inputFile) {
        this.inputFile = inputFile;
    }

    public String getWaterMarkTemplateId() {
        return waterMarkTemplateId;
    }

    public void setWaterMarkTemplateId(String waterMarkTemplateId) {
        this.waterMarkTemplateId = waterMarkTemplateId;
    }
}
