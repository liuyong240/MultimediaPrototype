package org.multimediaprototype.mts.service;


import com.aliyun.api.mts.mts20140618.response.QueryJobListByPidResponse;
import com.aliyun.api.mts.mts20140618.response.SearchJobResponse;
import org.multimediaprototype.oss.dao.model.OSSFile;

import java.util.List;
import java.util.Map;


/**
 * Created by haihong.xiahh on 2015/11/13.
 */
public interface ITranscodeService {

    // 取消转码任务
    boolean cancelTranscodeJob(String jobId);

    // 查询转码任务 TODO 返回内容根据需要再优化
    Map<String, String> queryTranscodeJobs(String jobIds);

    // 搜索转码作业 TODO 返回内容根据需要优化
    SearchJobResponse searchTranscodeJobs(Long pageNumber, Long pageSize, String state, String startOfJobCreatedTimeRange, String endOfJobCreatedTimeRange);

    // 查询管道转码作业 TODO 返回内容根据需要优化
    QueryJobListByPidResponse queryTranscodeJobsByPid(String pipelineId, Long pageNumber, Long pageSize);

    // 获取已成功提交的转码任务JobId列表
    List<String> submitTranscodeJobs(Long userId, String inputObj, String inputBucket, String inputLocation);

    // 提交转码任务，并将转码任务和截图、描述等视频信息关联
    List<String> submitTranscodeJobs(Long userId, Long fatherMapId, OSSFile mediaFile, OSSFile picFile, String desc, String title);

}
