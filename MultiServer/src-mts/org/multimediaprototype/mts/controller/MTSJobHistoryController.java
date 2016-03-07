package org.multimediaprototype.mts.controller;

import com.wordnik.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.dao.impl.MTSJobHistoryService;
import org.multimediaprototype.mts.dao.model.MTSJobHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by haihong.xiahh on 2015/11/24.
 */
@RestController
@RequestMapping("/api/mts/jobhistory")
@Api(value = "MTSJobHistory封装")
public class MTSJobHistoryController {
    @Autowired
    MTSJobHistoryService mtsJobHistoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("获取所有任务列表")
    public ResponseObject getJobHistoryList(
            @ApiParam("当前页数")
            @RequestParam(required = false, defaultValue = "1")
            Integer pageNumber,
            @ApiParam("页大小")
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize

    ) {
        List<MTSJobHistory> mtsJobHistories = mtsJobHistoryService.getJobHistoryList(pageNumber, pageSize);
        ResponseObject res = new ResponseObject();

        res.setData(mtsJobHistories);
        return res;
    }

    @RequestMapping(value = "/listByPid", method = RequestMethod.GET)
    @ApiOperation("通过管道ID获取所有任务列表")
    public ResponseObject getJobHistoryListByPipelineId(
            @ApiParam("管道id")
            @RequestParam(required = true)
            String pipelineId,
            @ApiParam("当前页数")
            @RequestParam(required = false, defaultValue = "1")
            Integer pageNumber,
            @ApiParam("页大小")
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize

    ) {
        List<MTSJobHistory> mtsJobHistories = mtsJobHistoryService.getJobHistoryListByPipelineId(pipelineId, pageNumber, pageSize);
        ResponseObject res = new ResponseObject();

        res.setData(mtsJobHistories);
        return res;
    }

    @RequestMapping(value = "/listByUid", method = RequestMethod.GET)
    @ApiOperation("通过用户ID获取所有任务列表")
    public ResponseObject getJobHistoryListByUserId(
            @ApiParam("用户id")
            @RequestParam(required = true)
            Long userId,
            @ApiParam("当前页数")
            @RequestParam(required = false, defaultValue = "1")
            Integer pageNumber,
            @ApiParam("页大小")
            @RequestParam(required = false, defaultValue = "10")
            Integer pageSize

    ) {
        List<MTSJobHistory> mtsJobHistories = mtsJobHistoryService.getJobHistoryListByUserId(userId, pageNumber, pageSize);
        ResponseObject res = new ResponseObject();

        res.setData(mtsJobHistories);
        return res;
    }


}
