package org.multimediaprototype.mts.service.impl;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.domain.AnalysisJob;
import com.aliyun.api.mts.mts20140618.request.QueryAnalysisJobListRequest;
import com.aliyun.api.mts.mts20140618.request.SubmitAnalysisJobRequest;
import com.aliyun.api.mts.mts20140618.response.QueryAnalysisJobListResponse;
import com.aliyun.api.mts.mts20140618.response.SubmitAnalysisJobResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.mts.service.IPresetTemplateSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
@Service
public class PresetTemplateService implements IPresetTemplateSerivce {

    @Autowired
    AliyunClientManager aliyunClientManager;

    private AliyunClient mtsClient;

    // log4j
    private Logger log = LogManager.getLogger(PresetTemplateService.class);

    @PostConstruct
    public void setupClient() {
        mtsClient = aliyunClientManager.getMtsClient();
    }

    /**
     * 查询预制模板分析作业列表
     *
     * @param analysisJobIds 作业Id列表
     * @return               分析作业列表和不存在的作业Id集合对象
     */
    @Override
    public HashMap queryAnalysisJobList(List<String> analysisJobIds) {

        try {
            QueryAnalysisJobListRequest request = new QueryAnalysisJobListRequest();
            
            for(int index = 0; index < analysisJobIds.size(); index++) {
                request.setAnalysisJobIds(analysisJobIds.get(index));
            }
            request.check();

            QueryAnalysisJobListResponse response = mtsClient.execute(request);
            if(!response.isSuccess()) {

                // throw new RuntimeException("QueryAnalysisJobListRequest failed");
                log.error("QueryAnalysisJobListRequest failed");
            }

            HashMap map = new HashMap();

            List<AnalysisJob> analysisJobList = response.getAnalysisJobList();
            List<String> noExistAnalysisJobIds = response.getNonExistAnalysisJobIds();

            map.put("analysisJobList", analysisJobList);
            map.put("noExistAnalysisJobIds", noExistAnalysisJobIds);

            return map;

        } catch (Exception e) {
            // throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 提交预置模板分析作业
     *
     * @param input      输入的结构花参数，JSON对象
     * @param priority   任务在管道内的转码优先级[1-10]，默认为6, 10为最高优先级
     * @param userData   用户自定义数据
     * @param pipelineId 管道ID
     * @return           系统预置模板分析作业对象
     */
    @Override
    public AnalysisJob submitAnalysisJob(String input, String priority, String userData, String pipelineId) {

        try {
            SubmitAnalysisJobRequest request = new SubmitAnalysisJobRequest();

            // 根据传入参数设置输入信息
            request.setInput(input);
            request.setPriority(priority);
            request.setUserData(userData);
            request.setPipelineId(pipelineId);
            request.check(); // 检查输入参数合法性

            SubmitAnalysisJobResponse response = mtsClient.execute(request);
            if(!response.isSuccess()) {
                //throw new RuntimeException("SubmitAnalysisJobRequest failed");
                log.error("SubmitAnalysisJobRequest failed");
            }

            return response.getAnalysisJob();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }
}
