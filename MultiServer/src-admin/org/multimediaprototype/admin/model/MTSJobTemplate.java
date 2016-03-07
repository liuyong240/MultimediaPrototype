package org.multimediaprototype.admin.model;


/**
 * Created by dx.yang on 15/11/16.
 */

/**
 * <h1>MTS转码作业预置模板</h1>
 *
 * <p>
 *     MTS转码接口SubmitJobsRequest需要提供若干参数<br>
 *     后台管理预先设置好其中的部分参数, 触发转码作业时, 可以读取预设参数, 生成SubmitJobsRequest.
 * </p>
 */
public class MTSJobTemplate {
    /**
     * 模板id
     */
    private Integer id;
    /**
     * 模板名
     */
    private String name;
    /**
     * 模板描述
     */
    private String desc;
    /**
     * outputs参数, SubmitJobsRequest的必要参数
     * 参考 {@link MTSJobOutput}
     */
    private String outputs;
    /**
     * 转码管道id, SubmitJobsRequest必要参数
     */
    private String pipelineId;
    /**
     * 转码结果输出到的bucket, SubmitJobsRequest必要参数
     */
    private String outputBucket;
    /**
     * 转码结果输出bucket所在的location, SubmitJobsRequest必要参数
     */
    private String outputLocation;
    /**
     * 操作人
     */
    private Integer adminId;
    /**
     * 更新时间
     */
    private Integer lastUpdate;
    /**
     * 是否使用, 业务接口只获取user为true的转码作业模板
     */
    private Boolean using;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOutputs() {
        return outputs;
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Integer lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getUsing() {
        return using;
    }

    public void setUsing(Boolean using) {
        this.using = using;
    }

    public String getOutputBucket() {
        return outputBucket;
    }

    public void setOutputBucket(String outputBucket) {
        this.outputBucket = outputBucket;
    }

    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }
}
