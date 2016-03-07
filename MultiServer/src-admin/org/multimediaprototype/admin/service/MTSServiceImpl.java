package org.multimediaprototype.admin.service;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.domain.Template;
import com.aliyun.api.domain.WaterMarkTemplate;
import com.aliyun.api.mts.mts20140618.request.*;
import com.aliyun.api.mts.mts20140618.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.taobao.api.ApiException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.admin.dao.MTSJobTemplateMapper;
import org.multimediaprototype.admin.model.MTSJobInput;
import org.multimediaprototype.admin.model.MTSJobOutput;
import org.multimediaprototype.admin.model.MTSJobTemplate;
import org.multimediaprototype.base.AliyunClientManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dx.yang on 15/11/11.
 */

@Service
@Scope("singleton")
public class MTSServiceImpl implements MTSService {

    // log4j
    static Logger logger = LogManager.getLogger(MTSServiceImpl.class.getName());

    @Autowired
    private MTSJobTemplateMapper mtsJobTemplateMapper;

    @Autowired
    private AliyunClientManagerImpl aliyunClientManager;

    private AliyunClient client;

    @PostConstruct
    @Override
    public void setupClient() {
        client = aliyunClientManager.getMtsClient();
    }

    /**
     * <h2>获取转码作业模板</h2>
     *
     * @param id      模板id
     * @param adminId 模板最后操作人
     * @param name    模板名称
     */
    @Override
    public List<MTSJobTemplate> getJobTemplates(long id, String name, long adminId, Boolean using) {
        return mtsJobTemplateMapper.getJobTemplate(id, name, adminId, using);
    }

    /**
     * <h2>创建或更新转码作业模板</h2>
     *
     * @param params 转码作业模板参数
     * 参数params数据结构为Map&lt;String, Object&gt; <br>
     * 具体字段可参考{@link org.multimediaprototype.admin.model.MTSJobTemplate}
     *
     * @return SQL执行insert或update返回的int值
     */
    @Override
    public Integer addAndUpdateJobTemplate(Map<String, Object> params) {

        Integer id = (Integer) params.get("id");

        //name, desc, pipelineId, adminId, lastUpdate, using
        String name = (String) params.get("name");
        String desc = (String) params.get("desc");
        String pipelineId = (String) params.get("pipelineId");
        String outputBucket = (String) params.get("outputBucket");
        String outputLocation = (String) params.get("outputLocation");
        String outputs = (String) params.get("outputs");
        Integer adminId = (Integer) params.get("adminId");
        Integer lastUpdate = (Integer) params.get("lastUpdate");
        Boolean using = (Boolean) params.get("using");

        Integer result = null;
        if (id != null) {
            result = mtsJobTemplateMapper.updateJobTemplate(
                    id, name, desc, outputs, outputBucket, outputLocation, pipelineId, adminId, lastUpdate, using
            );
        } else {
            result = mtsJobTemplateMapper.insertJobTemplate(
                    name, desc, outputs, outputBucket, outputLocation, pipelineId, adminId, lastUpdate, using
            );
        }

        return result;
    }

    /**
     * <h2>根据ID删除转码作业模板</h2>
     * @param id 模板id
     * @return sql执行返回的int值
     */
    @Override
    public Integer deleteJobTemplate(Integer id) {
        return mtsJobTemplateMapper.deleteJobTemplate(id);
    }

    /**
     * <h2>生成SubmitJobsRequest的方法</h2>
     * <p>
     * 官方SDK提供了SubmitJobsRequest类, 直接调用该类需要输入很多参数.<br>
     * 其中部分参数是后台提前配置好的.<br>
     * 调用此方法, 只需输入待转码文件的路径, 即可根据后台配置, 自动生成SubmitJobsRequest的实例, 提供给SDK调用.<br>
     * </p>
     * @param inputFileLocation 待转码文件所在OSS节点
     * @param inputFileBucket   待转码文件所在的bucket
     * @param inputFile         待转码文件名(包含路径) eg: example.flv 或 /folder/sample.mp4
     * @return SubmitJobsRequest的list
     * @throws Exception 没有可用的转码作业模板时, 抛出异常
     *
     * example:
     * <pre class="brush:java">
     * // bucket所在的节点
     * String inputFileLocation = "oss-cn-hangzhou";
     *
     * // 待转码文件所在bucket
     * String inputFileBucket = "bucketname";
     *
     * // 文件名(包含所在目录)
     * // 预置转码模板中, 会设置output输出的文件名, 其中的{%fielname%}标记会被替换为inputfile的文件名
     * // 假设intputFile参数为: path/example.flv
     * // 预置模板的输出文件名设置为: output_path/{%filename%}.mp4
     * // 最后生成的output完整文件路径为: output_path/example.mp4
     * String inputFile = "path/filename.mp4";
     *
     * // 生成submitJobsRequest实例
     * // 由于预置的作业模板可能有多个, 所以返回的SubmitJobsRequest也是多个
     * try {
     *     List<SubmitJobsRequest> reqs = mtsService.createSubmitJobsRequest(
     *         inputFileLocation,
     *         inputFileBucket,
     *         inputFile);
     *
     *     // 遍历调用SDK的接口, 提交转码作业
     *     for (SubmitJobsRequest jobReq : reqs) {
     *         SubmitJobsResponse jobResponse = aliyunClient.execute(jobReq);
     *     }
     * } catch (Exception e) {
     *     //如果数据库中没有预置的转码作业模板, 这里会抛出异常
     *     e.printStackTrace();
     * }
     * </pre>
     *
     */
    @Override
    public List<SubmitJobsRequest> createSubmitJobsRequest(String inputFileLocation, String inputFileBucket, String inputFile) throws Exception {

        // 获取正在使用的转码模板
        List<MTSJobTemplate> list = getJobTemplates(-1, null, -1, true);

        // 数据库里没有预置转码作业模板, 抛出异常
        if (list.size() == 0) {
            throw new Exception("没有可用的转码作业模板");
        }

        ObjectMapper mapper = new ObjectMapper();
        List<SubmitJobsRequest> reqList = new ArrayList<>();
        for (MTSJobTemplate jobTemplate : list) {

            // 将预置模板的outputs字段(字符串)转为MtsOutputs对象
            List<MTSJobOutput> jobOutputs = mapper.readValue(jobTemplate.getOutputs(),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, MTSJobOutput.class));

            // 根据输入的文件名和预置模板设置的目标文件后缀, 生成完整的输出文件名
            for (MTSJobOutput output : jobOutputs) {
                String inputFilename = FilenameUtils.getBaseName(inputFile);
                String presetFilename = output.getOutputObject();
                inputFilename = presetFilename.replace("{%filename%}", inputFilename);
                output.setOutputObject(inputFilename);
            }

            // outputs序列化
            String jobOutputsJSONString = mapper.writeValueAsString(jobOutputs);

            // inputfile序列化
            MTSJobInput jobInput = new MTSJobInput();
            jobInput.setLocation(inputFileLocation);
            jobInput.setBucket(inputFileBucket);
            jobInput.setObject(inputFile);
            String jobInputJSONString = mapper.writeValueAsString(jobInput);

            // 初始化一个SubmitJobsRequest对象
            SubmitJobsRequest req = new SubmitJobsRequest();
            req.setInput(jobInputJSONString);
            req.setOutputLocation(jobTemplate.getOutputLocation());
            req.setOutputBucket(jobTemplate.getOutputBucket());
            req.setOutputs(jobOutputsJSONString);
            req.setPipelineId(jobTemplate.getPipelineId());

            reqList.add(req);

        }

        return reqList;
    }

    /**
     * <h2>调用预设转码作业模板 执行转码作业</h2>
     * <p>包装createSubmitJobsRequest方法, 直接执行转码作业模板生成的request实例.</p>
     * @param inputLocation 待转码文件所在节点
     * @param inputBucket 待转码文件所在bucket
     * @param inputFile 待转码文件路径名称
     * @return SubmitJobsResponse的list
     * @throws Exception
     *
     * example:
     * <pre class="brush:java">
     * // bucket所在的节点
     * String inputFileLocation = "oss-cn-hangzhou";
     *
     * // 待转码文件所在bucket
     * String inputFileBucket = "bucketname";
     *
     * // 文件名(包含所在目录)
     * // 预置转码模板中, 会设置output输出的文件名, 其中的{%fielname%}标记会被替换为inputfile的文件名
     * // 假设intputFile参数为: path/example.flv
     * // 预置模板的输出文件名设置为: output_path/{%filename%}.mp4
     * // 最后生成的output完整文件路径为: output_path/example.mp4
     * String inputFile = "path/filename.mp4";
     *
     * // 执行转码作业
     * try {
     *     List<SubmitJobsResponse> responses = mtsService.submitJobs(
     *         inputFileLocation,
     *         inputFileBucket,
     *         inputFile);
     * } catch (Exception e) {
     *     //如果数据库中没有预置的转码作业模板, 这里会抛出异常
     *     e.printStackTrace();
     * }
     * </pre>
     *
     */
    @Override
    public List<SubmitJobsResponse> submitJobs(String inputLocation, String inputBucket, String inputFile) throws Exception {
        List<SubmitJobsResponse> responseList = new ArrayList<>();
        List<SubmitJobsRequest> reqs = createSubmitJobsRequest(inputLocation, inputBucket, inputFile);
        for (SubmitJobsRequest req : reqs) {
            SubmitJobsResponse response = client.execute(req);
            responseList.add(response);
        }
        return responseList;
    }


    /**
     * <h2>获取MTS自定义转码模板列表</h2>
     * <p>封装MTS API中的SearchTemplate方法</p>
     *
     * @param pageNumber 第几页
     * @param pageSize 每页多少条
     * @param state 模板状态
     *
     * @return 返回值为Map, {count:一共返回多少条记录, list:模板列表}
     */
    @Override
    public HashMap<String, Object> SearchTemplate(long pageNumber, long pageSize, String state) {
        SearchTemplateRequest req = new SearchTemplateRequest();
        req.setPageNumber(pageNumber);
        req.setPageSize(pageSize);
        req.setState(state);
        SearchTemplateResponse resp = null;
        try {
            resp = client.execute(req);
            if (resp.isSuccess()) {
                HashMap<String, Object> hash = new HashMap<>();
                Long count = resp.getTotalCount();
                List<Template> list = resp.getTemplateList();
                hash.put("count", count);
                hash.put("list", list);
                return hash;
            }
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * <h2>根据id搜索MTS转码模板</h2>
     * <p>封装MTS API中的QueryTemplateList方法</p>
     * @param ids 模板ID, 多个ID之间用逗号分隔
     * @return 模板列表
     */
    @Override
    public List<Template> QueryTemplateList(String ids) {

        String[] idsSplited = StringUtils.split(ids, ",");

        int size = idsSplited.length;
        if (size > 10 || size == 0) {
            return null;
        }

        QueryTemplateListRequest req = new QueryTemplateListRequest();
        req.setTemplateIds(ids);

        try {
            QueryTemplateListResponse res = client.execute(req);
            if (res.isSuccess()) {
                return res.getTemplateList();
            }
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public AddTemplateResponse AddTemplate(Map<String, String> template) {
        AddTemplateRequest req = new AddTemplateRequest();

        req.setName(template.get("Name"));
        req.setContainer(template.get("Container"));
        req.setVideo(template.get("Video"));
        req.setAudio(template.get("Audio"));
        req.setTransConfig(template.get("TransConfig"));
        req.setMuxConfig(template.get("MuxConfig"));

        AddTemplateResponse res = null;
        try {
            res = client.execute(req);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }

        return res;
    }

    @Override
    public UpdateTemplateResponse UpdateTemplate(Map<String, String> template) {

        UpdateTemplateRequest req = new UpdateTemplateRequest();

        req.setTemplateId(template.get("Id"));
        req.setName(template.get("Name"));
        req.setContainer(template.get("Container"));
        req.setVideo(template.get("Video"));
        req.setAudio(template.get("Audio"));
        req.setTransConfig(template.get("TransConfig"));
        req.setMuxConfig(template.get("MuxConfig"));

        UpdateTemplateResponse res = null;
        try {
            res = client.execute(req);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }

        return res;

    }

    @Override
    public DeleteTemplateResponse DeleteTemplate(String id) {
        DeleteTemplateRequest req = new DeleteTemplateRequest();
        req.setTemplateId(id);
        DeleteTemplateResponse res = null;
        try {
            res = client.execute(req);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    @Override
    public Map<String, Object> SearchWaterMarkTemplate(long pageNumber, long pageSize, String state) {
        SearchWaterMarkTemplateRequest req = new SearchWaterMarkTemplateRequest();
        req.setPageNumber(pageNumber);
        req.setPageSize(pageSize);
        req.setState(state);
        Map<String, Object> result = null;
        try {
            SearchWaterMarkTemplateResponse res = client.execute(req);
            if (res.isSuccess()) {
                result = new HashMap<>();
                result.put("count", res.getTotalCount());
                result.put("list", res.getWaterMarkTemplateList());
            }
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public List<WaterMarkTemplate> QueryWaterMarkTemplateList(String ids) {
        String[] idsSplited = StringUtils.split(ids, ",");

        int size = idsSplited.length;
        if (size > 10 || size == 0) {
            return null;
        }

        QueryWaterMarkTemplateListRequest req = new QueryWaterMarkTemplateListRequest();
        req.setWaterMarkTemplateIds(ids);

        try {
            QueryWaterMarkTemplateListResponse res = client.execute(req);
            if (res.isSuccess()) {
                return res.getWaterMarkTemplateList();
            }
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    @Override
    public AddWaterMarkTemplateResponse AddWaterMarkTemplate(Map<String, String> template) {
        AddWaterMarkTemplateRequest req = new AddWaterMarkTemplateRequest();
        req.setName(template.get("Name"));
        req.setConfig(template.get("Config"));
        try {
            AddWaterMarkTemplateResponse res = client.execute(req);
            return res;
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


    @Override
    public UpdateWaterMarkTemplateResponse UpdateWaterMarkTemplate(Map<String, String> template) {
        UpdateWaterMarkTemplateRequest req = new UpdateWaterMarkTemplateRequest();
        req.setWaterMarkTemplateId(template.get("Id"));
        req.setName(template.get("Name"));
        req.setConfig(template.get("Config"));
        try {
            UpdateWaterMarkTemplateResponse res = client.execute(req);
            return res;
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public DeleteWaterMarkTemplateResponse DeleteWaterMarkTemplate(String id) {
        DeleteWaterMarkTemplateRequest req = new DeleteWaterMarkTemplateRequest();
        req.setWaterMarkTemplateId(id);
        try {
            DeleteWaterMarkTemplateResponse res = client.execute(req);
            return res;
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        return null;
    }


}



