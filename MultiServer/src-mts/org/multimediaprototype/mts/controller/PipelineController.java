package org.multimediaprototype.mts.controller;

import com.aliyun.api.domain.Pipeline;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.IPipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by zhuowu.zm on 2015/11/17.
 */
@RestController
@RequestMapping("/api/mts/pipeline")
@Api(value = "MTS管道封装")
public class PipelineController {

    @Autowired
    private IPipelineService pipelineService;

    /**
     * 获取用户管道列表
     *
     * @return       用户管道列表
     */
    @RequestMapping(value = "/user/pipelinelist", method = RequestMethod.GET)
    @ApiOperation("获取用户管道列表")
    public ResponseObject getPipelineList() {

        List<Pipeline> pipelineList = pipelineService.getPipelineList();
        ResponseObject resObj = new ResponseObject();
        resObj.setData(pipelineList);

        return resObj;
    }

    /**
     * 修改指定管道名称
     *
     * @param pipelineId   管道ID
     * @param pipelineName 管道名称
     * @return             更新后的管道实例
     */
    @RequestMapping(value = "/user/updatepipelinename", method = RequestMethod.POST)
    @ApiOperation("修改指定管道名称")
    public ResponseObject updatePipelineName(
            @ApiParam("管道Id")
            @RequestParam(required = true)
            String pipelineId,
            @ApiParam("管道将被修改成的名称")
            @RequestParam(required = true)
            String pipelineName) {

        Pipeline pipeline = pipelineService.updatePipelineName(pipelineId, pipelineName);
        ResponseObject resObj = new ResponseObject();

        if(pipeline == null) {
            resObj.setCode(1);
        }
        resObj.setData(pipeline);

        return resObj;
    }

    /**
     * 修改指定管道状态
     *
     * @param pipelineId    管道ID
     * @param pipelineState 管道状态：Active, Paused
     * @return              更新后的管道实例
     */
    @RequestMapping(value = "/user/updatepipelinestate", method = RequestMethod.POST)
    @ApiOperation("修改指定管道状态")
    public ResponseObject updatePipelineState(
            @ApiParam("管道Id")
            @RequestParam(required = true)
            String pipelineId,
            @ApiParam("管道将被修改成的状态")
            @RequestParam(required = true)
            String pipelineState) {

        Pipeline pipeline = pipelineService.updatePipelineState(pipelineId, pipelineState);
        ResponseObject resObj = new ResponseObject();

        if(pipeline == null) {
            resObj.setCode(1);
        }
        resObj.setData(pipeline);

        return resObj;
    }

    /**
     * 搜索指定状态的管道
     *
     * @param pipelineState 管道状态：All, Active, Paused, Deleted,默认是All
     * @return              满足条件的管道列表实例
     */
    @RequestMapping(value = "/user/searchpipelinebystate", method = RequestMethod.GET)
    @ApiOperation("搜索指定状态的管道")
    public ResponseObject searchpipelinebystate(
            @ApiParam("管道状态")
            @RequestParam(required = false)
            String pipelineState) {

        List<Pipeline> pipelineList = pipelineService.searchPipelineByState(pipelineState);
        ResponseObject resObj = new ResponseObject();
        resObj.setData(pipelineList);

        return resObj;
    }

    /**
     * 删除指定管道（预留接口）
     *
     * @param pipelineId 管道Id
     * @return           返回删除结果，true表示删除成功，false表示删除失败
     */
    @RequestMapping(value = "/user/deletepipeline", method = RequestMethod.POST)
    @ApiOperation("删除指定管道（预留接口）")
    public ResponseObject deletePipeline(
            @ApiParam("管道Id")
            @RequestParam(required = true)
            String pipelineId) {

        boolean result = pipelineService.deletePipeline(pipelineId);
        ResponseObject resObj = new ResponseObject();
        resObj.setData(result);

        return resObj;
    }
}
