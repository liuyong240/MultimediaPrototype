package org.multimediaprototype.mns.service;

import com.aliyun.mns.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.mns.service.impl.MNSService;
import org.multimediaprototype.mts.dao.impl.MTSJobHistoryService;
import org.multimediaprototype.mts.dao.model.MTSJobHistory;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.oss.service.impl.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/12/1.
 */

/**
 * 轮询service，定期从MTS queue里取消息，查看job状态
 */
@Component
public class ScheduleService {
    private static Logger log = LogManager.getLogger(ScheduleService.class);

    @Autowired
    private MNSService mnsService;
    @Autowired
    private TranscodeService transcodeService;
    @Autowired
    private MTSJobHistoryService mtsJobHistoryService;
    @Autowired
    private OSSService ossService;

    /**
     * 由于MNS的消息默认InActive时间为10秒，修改定时任务轮询时间为15秒
     */
    @Scheduled(fixedDelay = 15000)
    public void schedule() {

        // Step 1: 从队列获取未处理的消息
        Message msg = mnsService.receiveMessage();

        if (msg == null) {
            log.debug("MNS Queue is empty...");
            return;
        }

        String recepitHandle = msg.getReceiptHandle();

        // Step 2: 获取jobId，根据jobId查询转码作业状态
        String jobId = msg.getMessageBodyAsString();
        Map<String, String> jobStates = transcodeService.queryTranscodeJobs(jobId);

        // Step 3. 如果转码作业状态改变，更新mns_job_history记录
        if (jobStates != null) {
            for (String jobIdRet : jobStates.keySet()) {
                String jobState = jobStates.get(jobIdRet);

                // 成功、失败或取消转码，更新数据库，删除已更新的jobId
                if (jobState.contentEquals(Constants.MTS_JOB_STATE_TRANSCODE_SUCCESS) ||
                        jobState.contentEquals(Constants.MTS_JOB_STATE_TRANSCODE_FAIL) ||
                        jobState.contentEquals(Constants.MTS_JOB_STATE_TRANSCODE_CANCELLED)) {
                    int updateCount = mtsJobHistoryService.updateStatus(jobId, jobState);
                    // 转码结束后， 更新mediamapping
                    try {
                        MTSJobHistory mtsJobHistory = mtsJobHistoryService.getJobHistory(jobId);
                        String outputUrl = mtsJobHistory.getOutputUrl();
                        ossService.updateMediaMapStatus(outputUrl, jobState);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.debug(">>>Update counts: " + updateCount);
                    //boolean isDeleted = mnsService.deleteMessage(recepitHandle);
                    boolean isDeleted = mnsService.deleteMessageByRecepitHandle(null, recepitHandle);
                    if (isDeleted) {
                        log.debug(">>>JobID: [" + jobId + "] deleted!");
                    } else {
                        log.debug(">>>JobID: [" + jobId + "] not deleted!");
                    }
                }
                // 转码中，消息不从队列中删除
                if (jobState.contentEquals(Constants.MTS_JOB_STATE_TRANSCODING)) {
                    mtsJobHistoryService.updateStatus(jobId, jobState);
                    mnsService.receiveMessage();
                }
            }
        }
    }
}
