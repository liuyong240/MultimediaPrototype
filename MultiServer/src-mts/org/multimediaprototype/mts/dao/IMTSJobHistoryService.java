package org.multimediaprototype.mts.dao;

import org.multimediaprototype.mts.dao.model.MTSJobHistory;

import java.util.List;

/**
 * Created by haihong.xiahh on 2015/11/23.
 */
public interface IMTSJobHistoryService {
    // 插入MTSJob记录
    int insert(MTSJobHistory mtsJob);

    // 轮询后更新任务状态
    int updateStatus(String jobId, String Status);

    // 收到通知后更新任务状态
    int updateNotifiedStatus(String jobId, String status);

    //删除记录
    int delete(Long id);

    // 查看任务详情
    MTSJobHistory getJobHistory(Long id);
    MTSJobHistory getJobHistory(String jobId);

    /**
     * 根据pipelineId获取任务列表
     * @param pipelineId 管道id
     * @param pageNumber 当前页面，默认为1
     * @param pageSize 分页查询时设置的每页大小，默认为10，不超过100
     * @return
     */
    List<MTSJobHistory> getJobHistoryListByPipelineId(String pipelineId, Integer pageNumber, Integer pageSize);

    /**
     * 获取所有任务列表
     * @param pageNumber 当前页面，默认为1
     * @param pageSize 分页查询时设置的每页大小，默认为10，不超过100
     * @return
     */
    List<MTSJobHistory> getJobHistoryList(Integer pageNumber, Integer pageSize);

    /**
     * 获取用户所有任务列表
     * @param userId 用户ID
     * @param pageNumber 当前页面，默认为1
     * @param pageSize 分页查询时设置的每页大小，默认为10，不超过100
     * @return
     */
    List<MTSJobHistory> getJobHistoryListByUserId(Long userId, Integer pageNumber, Integer pageSize);

    /**
     * 根据JobID获取任务记录
     * @param jobId 任务对应的JobID列表
     * @return      返回任务记录列表
     */
    List<MTSJobHistory> getJobHistoryListByJobIds(List<String> jobIds);

}
