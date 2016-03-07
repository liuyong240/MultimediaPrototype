package org.multimediaprototype.mts.service;

import com.aliyun.api.domain.Pipeline;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
public interface IPipelineService {

    // 根据用户ID获取对应的管道列表
    List<Pipeline> getPipelineList();

    // 获取默认的管道
    Pipeline getDefaultPipeLine();

    // 更新指定管道名称
    Pipeline updatePipelineName(String pipelineId, String pipelineName);

    // 更新指定管道状态
    Pipeline updatePipelineState(String pipelineId, String pipelineState);

    // 根据管道状态搜索管道
    List<Pipeline> searchPipelineByState(String pipelineState);

    // 删除指定管道，true表示删除成功，false表示删除失败
    boolean deletePipeline(String pipelineId);
}
