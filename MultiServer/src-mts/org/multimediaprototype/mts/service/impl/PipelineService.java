package org.multimediaprototype.mts.service.impl;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.domain.Pipeline;
import com.aliyun.api.mts.mts20140618.request.DeletePipelineRequest;
import com.aliyun.api.mts.mts20140618.request.SearchPipelineRequest;
import com.aliyun.api.mts.mts20140618.request.UpdatePipelineRequest;
import com.aliyun.api.mts.mts20140618.response.DeletePipelineResponse;
import com.aliyun.api.mts.mts20140618.response.SearchPipelineResponse;
import com.aliyun.api.mts.mts20140618.response.UpdatePipelineResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.mts.service.IPipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
@Service
public class PipelineService implements IPipelineService {

    @Autowired
    private AliyunClientManager aliyunClientManager;

    private AliyunClient mtsClient;

    // log4j
    private Logger log = LogManager.getLogger(PipelineService.class);


    @PostConstruct
    public void setupClient() {
        mtsClient = aliyunClientManager.getMtsClient();
    }

    /**
     * 获取管道列表
     *
     * @return 返回管道列表对象
     */
    @Override
    public List<Pipeline> getPipelineList() {

         try {
             SearchPipelineRequest request = new SearchPipelineRequest();
             //QueryPipelineListRequest request = new QueryPipelineListRequest();
             //QueryPipelineListResponse response = mtsClient.execute(request);
             SearchPipelineResponse response = mtsClient.execute(request);

             if(!response.isSuccess()) {

                // throw new RuntimeException("SearchPipelineRequest failed");
                log.error("SearchPipelineRequest failed");
             }

            return response.getPipelineList();

        } catch (Exception e) {
            // throw new RuntimeException(e);
             log.error(e.getMessage());
             return null;
        }
    }

    /**
     * 获取默认管道信息
     *
     * @return 返回默认管道信息
     */
    @Override
    public Pipeline getDefaultPipeLine() {
        List<Pipeline> pipelineList = getPipelineList();
        if (pipelineList != null && pipelineList.size() > 1) {
            return pipelineList.get(0);
        }
        return null;
    }

    /**
     * 更新管道名称
     *
     * @param pipelineId          管道对应的ID
     * @param changedPipelineName 修改后的管道名称
     * @return                    更新后的管道对象
     */
    public Pipeline updatePipelineName(String pipelineId, String changedPipelineName) {
        try {
            UpdatePipelineRequest request = new UpdatePipelineRequest();

            // 根据传入参数设置请求信息
            request.setPipelineId(pipelineId);
            request.setName(changedPipelineName);
            request.check(); // 检查输入参数

            UpdatePipelineResponse response = mtsClient.execute(request);
            if(!response.isSuccess()) {

                // throw new RuntimeException("UpdatePipelineRequest failed (update pipeline name)");
                log.error("UpdatePipelineRequest failed (update pipeline name)");
            }
            return response.getPipeline();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 更新管道状态
     *
     * @param pipelineId           管道对应的ID
     * @param changedPipelineState 修改后的管道状态
     * @return                     更新后的管道对象
     */
    @Override
    public Pipeline updatePipelineState(String pipelineId, String changedPipelineState) {

        try {
            UpdatePipelineRequest request = new UpdatePipelineRequest();
            // 根据传入参数设置请求信息
            request.setPipelineId(pipelineId);
            request.setState(changedPipelineState);
            request.check();  // 检查输入参数

            UpdatePipelineResponse response = mtsClient.execute(request);

            if(!response.isSuccess()) {

                // throw new RuntimeException("UpdatePipelineRequest failed (update pipeline state)");
                log.error("UpdatePipelineRequest failed (update pipeline state)");
            }
            return response.getPipeline();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 根据状态查询对应的管道信息
     *
     * @param pipelineState 指定的管道状态
     * @return              返回对应状态的管道列表
     */
    @Override
    public List<Pipeline> searchPipelineByState(String pipelineState) {

        try {
            SearchPipelineRequest request = new SearchPipelineRequest();
            request.setState(pipelineState);
            request.check();

            SearchPipelineResponse response = mtsClient.execute(request);
            if (!response.isSuccess()) {

                // throw new RuntimeException("SearchPipelineRequest failed (search pipeline state)");
                log.error("SearchPipelineRequest failed (search pipeline state)");
            }
            return response.getPipelineList();

        } catch (Exception e) {

            // throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 删除指定管道
     *
     * @param pipelineId 指定要删除的管道ID
     * @return           返回删除状态，true表示成功，false表示失败
     */
    @Override
    public boolean deletePipeline(String pipelineId) {

        try {
            DeletePipelineRequest request = new DeletePipelineRequest();
            request.setPipelineId(pipelineId);
            request.check();

            DeletePipelineResponse response = mtsClient.execute(request);
            boolean deleteFlag = response.isSuccess();

            return deleteFlag;

        } catch (Exception e) {

            // throw new RuntimeException(e);
            log.error(e);

            return false;
        }
    }
}
