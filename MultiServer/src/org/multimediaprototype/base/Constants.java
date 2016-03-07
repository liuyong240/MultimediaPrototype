package org.multimediaprototype.base;

import java.net.Inet4Address;

/**
 * Created by haihong.xiahh on 2015/11/12.
 */
public class Constants {
    public static final String STS_REGION_HANGZHOU = "cn-hangzhou";
    public static final String REGION_HANGZHOU = "oss-cn-hangzhou";
    public static final String REGION_BEIJING = "oss-cn-beijing";
    public static final String REGION_QINGDAO = "oss-cn-qingdao";
    public static final String REGION_SHENZHEN = "oss-cn-shenzhen";
    public static final String REGION_SHANGHAI = "oss-cn-shanghai";
    public static final String REGION_USWEST = "oss-us-west-1";
    public static final String REGION_SIGAPORE = "oss-ap-southeast-1";
    public static final String OSS_BUCKET_NAME = "mns_test";

    public static final String MTS_JOB_STATE_ALL = "all";
    public static final String MTS_JOB_STATE_SUBMITTED = "Submitted";
    public static final String MTS_JOB_STATE_TRANSCODING = "Transcoding";
    public static final String MTS_JOB_STATE_TRANSCODE_SUCCESS = "TranscodeSuccess";
    public static final String MTS_JOB_STATE_TRANSCODE_FAIL = "TranscodeFail";
    public static final String MTS_JOB_STATE_TRANSCODE_CANCELLED = "TranscodeCancelled";

    public static final String MTS_ACTION_SUBMIT_TRANSCODE_JOB = "SubmitJobs";

    public static final String MTS_PIPELINE_STATUS_ACTIVE = "Active";
    public static final String MTS_PIPELINE_STATUS_PAUSED = "Paused";
    public static final Integer MEDIA_STATUS_CHECK_PENDING = 0; // 待审核（源文件，不需要转码或者已经转码成功）
    public static final Integer MEDIA_STATUS_CHECK_SUCCESS = 1; // 审核通过
    public static final Integer MEDIA_STATUS_CHECK_FAILED = 2; // 审核失败
    public static final Integer MEDIA_STATUS_TRANSDING = 3; // 转码中
    public static final Integer MEDIA_STATUS_TRANSCODE_FAILED = 4; // 转码失败
    public static final Integer MEDIA_STATUS_TRANSCODE_PENDING = 5; // 源文件，需要转码
}

