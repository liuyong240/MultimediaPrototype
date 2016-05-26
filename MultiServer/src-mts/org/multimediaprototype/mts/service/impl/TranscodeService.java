package org.multimediaprototype.mts.service.impl;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.domain.Job;
import com.aliyun.api.domain.JobResult;
import com.aliyun.api.domain.Pipeline;
import com.aliyun.api.mts.mts20140618.request.*;
import com.aliyun.api.mts.mts20140618.response.SubmitJobsResponse;
import com.aliyun.api.mts.mts20140618.response.CancelJobResponse;
import com.aliyun.api.mts.mts20140618.response.QueryJobListResponse;
import com.aliyun.api.mts.mts20140618.response.SearchJobResponse;
import com.aliyun.api.mts.mts20140618.response.QueryJobListByPidResponse;
import com.taobao.api.ApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.multimediaprototype.admin.service.MTSService;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.base.bean.OSSFileDO;
import org.multimediaprototype.mns.service.impl.MNSService;
import org.multimediaprototype.mts.dao.impl.MTSJobHistoryService;
import org.multimediaprototype.mts.dao.model.MTSJobHistory;
import org.multimediaprototype.mts.service.ITranscodeService;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.impl.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/11/17.
 */
@Service
public class TranscodeService implements ITranscodeService {
    // common config
    @Value("#{propsUtil['aliyunConsole.bucket']}")
    private String defaultBucket;
    @Value("#{propsUtil['aliyunConsole.location']}")
    private String defaultLocation;
    @Autowired
    private MTSService mtsAdminService;
    @Autowired
    private MTSJobHistoryService mtsJobHistoryService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    AliyunClientManager aliyunClientManager;
    @Autowired
    MNSService mnsService;
    @Autowired
    private OSSService ossService;

    private AliyunClient mtsClient;
    private static Logger log = LogManager.getLogger(TranscodeService.class);

    @PostConstruct
    public void setupClient() {
        log.debug("TranscodeService setup");
        mtsClient = aliyunClientManager.getMtsClient();
    }

    private MTSJobHistory formatJobHistoryFromJobResult(String inputUrl, JobResult jobResult, Long userId) {
        Job job = jobResult.getJob();
        String outputBucket = job.getOutput().getOutputFile().getBucket();
        String outputLocation = job.getOutput().getOutputFile().getLocation();
        String outputObj = job.getOutput().getOutputFile().getObject();
        String outputUrl = new OSSFileDO(outputLocation, outputBucket, outputObj).getExternalUrl();
        MTSJobHistory mtsJob = new MTSJobHistory();
        mtsJob.setUserId(userId);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        mtsJob.setGmtCreated(now);
        mtsJob.setGmtModified(now);
        mtsJob.setJobId(job.getJobId());
        mtsJob.setPipelineId(job.getPipelineId());
        mtsJob.setOutputUrl(outputUrl);
        mtsJob.setInputUrl(inputUrl);
        mtsJob.setStatus(job.getState());
        mtsJob.setTranscodeTemplateId(job.getOutput().getTemplateId());
        if (job.getOutput().getWaterMarkList() != null) {
            mtsJob.setWatermarkCount(job.getOutput().getWaterMarkList().size());
        } else {
            mtsJob.setWatermarkCount(0);
        }
        mtsJob.setJobAction(Constants.MTS_ACTION_SUBMIT_TRANSCODE_JOB);
        return mtsJob;
    }

    @Override
    public boolean cancelTranscodeJob(String jobId) {
        // 更新管道，将管道状态置为Paused，暂停作业调度
        Pipeline pipeline = pipelineService.getDefaultPipeLine();
        if (pipeline == null) {
            log.error("get default pipeline failed");
            return false;
        }
        Pipeline updatedPipeline = pipelineService.updatePipelineState(pipeline.getId(), Constants.MTS_PIPELINE_STATUS_PAUSED);
        if (updatedPipeline.getState() != Constants.MTS_PIPELINE_STATUS_PAUSED) {
            log.error("pause pipeline failed");
            return false;
        }

        // 调用cancel job接口
        boolean cancelJobStatus = cancelJob(jobId);

        // 恢复管道状态为active，管道中的其他作业才会被继续调度执行
        Pipeline recoveredPipeline = pipelineService.updatePipelineState(pipeline.getId(), Constants.MTS_PIPELINE_STATUS_ACTIVE);
        if (recoveredPipeline.getState() != Constants.MTS_PIPELINE_STATUS_ACTIVE) {
            log.error("recover pipeline failed");
            //TODO retry if failed
        }
        return cancelJobStatus;
    }

    private boolean cancelJob(String jobId) {
        CancelJobRequest request = new CancelJobRequest();
        request.setJobId(jobId);
        try {
            request.check();
            CancelJobResponse response = mtsClient.execute(request);
            return response.isSuccess();
        } catch (ApiException e) {
            log.error(e.getStackTrace());
            return false;
        }
    }

    @Override
    public Map<String, String> queryTranscodeJobs(String jobIds) {
        QueryJobListRequest request = new QueryJobListRequest();
        request.setJobIds(jobIds);
        try {
            request.check();
            Map<String, String> stateMap = new HashMap<>();
            QueryJobListResponse response = mtsClient.execute(request);
            if (response.isSuccess()) {
                List<Job> jobs = response.getJobList();
                if (jobs != null) {
                    for (Job job : jobs) {
                        stateMap.put(job.getJobId(), job.getState());
                    }
                }
            }
            return stateMap;
        } catch (ApiException e) {
            log.error(e.getStackTrace());
            return null;
        }
    }

    @Override
    public SearchJobResponse searchTranscodeJobs(Long pageNumber, Long pageSize, String state, String startOfJobCreatedTimeRange, String endOfJobCreatedTimeRange) {
        SearchJobRequest request = new SearchJobRequest();
        try {
            request.setPageNumber(pageNumber);
            request.setPageSize(pageSize);
            request.setState(state);
            request.setStartOfJobCreatedTimeRange(startOfJobCreatedTimeRange);
            request.setEndOfJobCreatedTimeRange(endOfJobCreatedTimeRange);
            request.check();
            // TODO 返回内容根据需要再优化
            return mtsClient.execute(request);
        } catch (ApiException e) {
            log.error(e.getStackTrace());
            return null;
        }
    }

    @Override
    public QueryJobListByPidResponse queryTranscodeJobsByPid(String pipelineId, Long pageNumber, Long pageSize) {
        QueryJobListByPidRequest request = new QueryJobListByPidRequest();
        try {
            request.setPipelineId(pipelineId);
            request.setPageNumber(pageNumber);
            request.setPageSize(pageSize);
            request.check();
            // TODO 返回内容根据需要再优化
            return  mtsClient.execute(request);
        } catch (ApiException e) {
            log.error(e.getStackTrace());
            return null;
        }
    }

    /**
     * 提交转码任务
     *
     * @param userId        用户Id
     * @param inputObj      输入的oss object
     * @param inputBucket   输入的Bucket信息
     * @param inputLocation 输入的Location
     * @return 已成功提交的转码作业任务JobId列表
     */
    @Override
    public List<String> submitTranscodeJobs(Long userId, String inputObj, String inputBucket, String inputLocation) {

        try {
            List<String> jobIds = new ArrayList<>(); // JobId列表默认为空

            if (StringUtils.isEmpty(inputObj)) {
                log.error("inputObj should be specified");
                return null;
            }
            // 校验参数，如果为null，取默认值
            if (StringUtils.isEmpty(inputBucket)) {
                inputBucket = defaultBucket;
            }
            if (StringUtils.isEmpty(inputLocation)) {
                inputLocation = defaultLocation;
            }

            String inputUrl = new OSSFileDO(inputLocation, inputBucket, inputObj).getExternalUrl();

            // 提交job
            List<SubmitJobsResponse> responseList;
            try {
                responseList = mtsAdminService.submitJobs(inputLocation, inputBucket, inputObj);
            } catch (Exception e) {
                log.error(e.getStackTrace());
                return null;
            }

            // 更新sql，insert job记录
            for (SubmitJobsResponse response : responseList) {
                if (response.isSuccess()) {
                    List<JobResult> jobResultList = response.getJobResultList();
                    for (JobResult jobResult : jobResultList) {
                        // 更新job history
                        MTSJobHistory mtsJobHistory = formatJobHistoryFromJobResult(inputUrl, jobResult, userId);
                        mtsJobHistoryService.insert(mtsJobHistory);

                        // 将jobId 存储到MNS queue
                        String jobId = jobResult.getJob().getJobId();
                        jobIds.add(jobId);
                        mnsService.sendMessage(jobId); // 将jobId信息插入MNS服务提供的默认消息队列中
                    }
                }
            }

            return jobIds;

        } catch (Exception e) {
            log.error(e.getStackTrace());
            return null;
        }
    }


    /**
     * 提交转码任务，并将转码任务和截图、描述等视频信息关联*
     * @param userId 用户id
     * @param fatherMapId 父id (用于将源文件和转码后文件进行关联)
     * @param mediaFile 视频文件
     * @param picFile 截图文件
     * @param desc 描述信息
     * @param title 标题
     * @return
     */
    @Override
    public List<String> submitTranscodeJobs(Long userId, Long fatherMapId, OSSFile mediaFile, OSSFile picFile, String desc, String title) {
        try {
            Long picFileId = 0L; // TODO set default pic
            if (picFile != null) {
                picFileId = picFile.getId();
            }
            List<String> jobIds = new ArrayList<>(); // JobId列表默认为空
            String inputUrl = mediaFile.getObjectUrl();

            // 提交job
            List<SubmitJobsResponse> responseList;
            try {
                responseList = mtsAdminService.submitJobs(mediaFile.getLocation(), mediaFile.getBucketName(), mediaFile.getObjectName());
            } catch (Exception e) {
                log.error(e.getStackTrace());
                return null;
            }

            // 更新sql，insert job记录
            for (SubmitJobsResponse response : responseList) {
                if (response.isSuccess()) {
                    List<JobResult> jobResultList = response.getJobResultList();
                    for (JobResult jobResult : jobResultList) {
                        MTSJobHistory mtsJobHistory = formatJobHistoryFromJobResult(inputUrl, jobResult, userId);

                        // 更新job history 记录
                        mtsJobHistoryService.insert(mtsJobHistory);

                        // 输出文件插入到oss_file表
                        String outputUrl = mtsJobHistory.getOutputUrl();
                        Map<String, String> outputParsed = ossService.parseOSSUrl(outputUrl);
                        String objectKey = outputParsed.get("object");
                        OSSFile outputFile = ossService.upUserMeg(objectKey, userId);

                        // 更新media map 表
                        ossService.addMediaMap(outputFile.getId(), picFileId, desc, title, fatherMapId, Constants.MEDIA_STATUS_TRANSDING);

                        // 将jobId存储到mns queue
                        String jobId = jobResult.getJob().getJobId();
                        jobIds.add(jobId);
                        mnsService.sendMessage(jobId);
                    }
                }
            }

            return jobIds;

        } catch (Exception e) {
            log.error(e.getStackTrace());
            return null;
        }
    }

}
