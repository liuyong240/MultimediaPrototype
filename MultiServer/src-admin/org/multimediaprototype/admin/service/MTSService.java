package org.multimediaprototype.admin.service;

import com.aliyun.api.domain.Template;
import com.aliyun.api.domain.WaterMarkTemplate;
import com.aliyun.api.mts.mts20140618.request.SubmitJobsRequest;
import com.aliyun.api.mts.mts20140618.response.*;
import org.multimediaprototype.admin.model.MTSJobTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dx.yang on 15/11/11.
 */

/*
*   调用MTS服务的接口
*   封装MTS SDK
* */
public interface MTSService {

    // 初始化SDK客户端
    void setupClient();

    // -[ job template ]------------------------------
    List<MTSJobTemplate> getJobTemplates(long id, String name, long adminId, Boolean using);

    Integer addAndUpdateJobTemplate(Map<String, Object> params);
    Integer deleteJobTemplate(Integer id);

    List<SubmitJobsRequest> createSubmitJobsRequest(String inputFileLocation, String inputFileBucket, String inputFile) throws Exception;

    List<SubmitJobsResponse> submitJobs(String inputLocation, String inputBucket, String inputFile) throws Exception;


    // -[ template ]------------------------------
    HashMap<String, Object> SearchTemplate(long pageNumber, long pageSize, String state);

    List<Template> QueryTemplateList(String ids);

    AddTemplateResponse AddTemplate(Map<String, String> template);

    UpdateTemplateResponse UpdateTemplate(Map<String, String> template);

    DeleteTemplateResponse DeleteTemplate(String id);

    // -[ watermark ]------------------------------
    Map<String, Object> SearchWaterMarkTemplate(long pageNumber, long pageSize, String state);

    List<WaterMarkTemplate> QueryWaterMarkTemplateList(String ids);

    AddWaterMarkTemplateResponse AddWaterMarkTemplate(Map<String, String> template);

    UpdateWaterMarkTemplateResponse UpdateWaterMarkTemplate(Map<String, String> template);

    DeleteWaterMarkTemplateResponse DeleteWaterMarkTemplate(String id);

}
