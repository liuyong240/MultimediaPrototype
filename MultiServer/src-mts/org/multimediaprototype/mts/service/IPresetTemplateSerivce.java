package org.multimediaprototype.mts.service;

import com.aliyun.api.domain.AnalysisJob;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
public interface IPresetTemplateSerivce {

    // 提交模板分析作业
    HashMap queryAnalysisJobList(List<String> analysisJobIds);

    // 查询模板分析作业
    AnalysisJob submitAnalysisJob(String input, String priority, String userData, String pipelineId);

}
