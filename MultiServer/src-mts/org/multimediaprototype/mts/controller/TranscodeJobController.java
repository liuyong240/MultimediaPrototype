package org.multimediaprototype.mts.controller;

import com.aliyun.api.mts.mts20140618.response.QueryJobListByPidResponse;
import com.aliyun.api.mts.mts20140618.response.QueryJobListResponse;
import com.aliyun.api.mts.mts20140618.response.SearchJobResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.ITranscodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * Created by haihong.xiahh on 2015/11/17.
 */
@RestController
@RequestMapping("/api/mts/transcode")
@Api(value = "mts转码封装")
public class TranscodeJobController {
    @Autowired
    private ITranscodeService transcodeService;

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    @ApiOperation("提交转码任务")
    public ResponseObject submitTranscodeJob(
            @ApiParam("用户id")
            @RequestParam(required = true)
            Long userId,

            @ApiParam("输入OSS文件名")
            @RequestParam(required = true)
            String inputObj,

            @ApiParam("输入OSS bucket")
            @RequestParam(required = false)
            String inputBucket,

            @ApiParam("输入OSS locaiton")
            @RequestParam(required = false)
            String inputLocation
    ) {
        List<String> jobIds = transcodeService.submitTranscodeJobs(userId, inputObj, inputBucket, inputLocation);
        ResponseObject res = new ResponseObject();
        res.setData(jobIds);

        return res;
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    @ApiOperation("取消转码任务")
    public ResponseObject cancelTranscodeJob(
            @ApiParam("jobId")
            @RequestParam(required = true)
            String jobId)
    {
        boolean flag = transcodeService.cancelTranscodeJob(jobId);
        ResponseObject res = new ResponseObject();
        res.setData(flag);
        
        return res;
    }


    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ApiOperation("查询转码作业")
    public Map<String, String> queryTranscodeJobs(
            @ApiParam("转码作业Id列表, 以,分割")
            @RequestParam(required = true)
            String jobIds) {

        return transcodeService.queryTranscodeJobs(jobIds);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation("搜索转码作业")
    public SearchJobResponse searchTranscodeJobs(
            @ApiParam("当前页号")
            @RequestParam(required = false)
            Long pageNumber,
            @ApiParam("分页查询设置的每页大小，默认10，最大不超过100")
            @RequestParam(required = false)
            Long pageSize,
            @ApiParam("转码任务状态")
            @RequestParam(required = false)
            String state,
            @ApiParam("创建转码作业时间范围中的下限值")
            @RequestParam(required = false)
            String startOfJobCreatedTimeRange,
            @ApiParam("创建转码作业时间的上限")
            @RequestParam(required = false)
            String endOfJobCreatedTimeRange) {

        return transcodeService.searchTranscodeJobs(pageNumber, pageSize, state, startOfJobCreatedTimeRange, endOfJobCreatedTimeRange);
    }

    @RequestMapping(value = "/querybypid", method = RequestMethod.POST)
    @ApiOperation("查询管道转码作业")
    public QueryJobListByPidResponse queryTranscodeJobsByPid(
            @ApiParam("管道ID")
            @RequestParam(required = true)
            String pipelineId,
            @ApiParam("当前页号")
            @RequestParam(required = false)
            Long pageNumber,
            @ApiParam("分页查询设置的每页大小，默认10，最大不超过100")
            @RequestParam(required = false)
            Long pageSize) {

        return transcodeService.queryTranscodeJobsByPid(pipelineId, pageNumber, pageSize);
    }

}
