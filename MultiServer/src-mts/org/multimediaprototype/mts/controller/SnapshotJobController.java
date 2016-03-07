package org.multimediaprototype.mts.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.ISnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.api.domain.SnapshotJob;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/17.
 */
@RestController
@RequestMapping("/api/mts/snapshot")
@Api(value = "MTS截图作业封装")
public class SnapshotJobController {

    @Autowired
    private ISnapshotService snapshotService;

    @Autowired
    AliyunClientManager aliyunClientManager;

    /**
     * 提交截图作业
     *
     * @param input          作业输入，JSON对象
     * @param snapshotConfig 截图配置，JSON对象
     * @param userData       用户自定义数据，最大长度1024字节
     * @return
     */
    @RequestMapping(value = "/submitsnapshotjob", method = RequestMethod.POST)
    @ApiOperation("提交截图作业")
    public ResponseObject submitSnapshotJob(
            @ApiParam("作业输入，JSON对象")
            @RequestParam(required = true)
            String input,
            @ApiParam("截图配置，JSON对象")
            @RequestParam(required = true)
            String snapshotConfig,
            @ApiParam("用户自定义数据")
            @RequestParam(required = false)
            String userData) {

        SnapshotJob snapshotJob = snapshotService.submitSnapshotJob(input, snapshotConfig, userData);
        ResponseObject res = new ResponseObject();

        if(snapshotJob == null) {
            res.setCode(1);
        }
        res.setData(snapshotJob);
        return res;
    }

    /**
     * 查询截图作业
     *
     * @param snapshotJobIds 截图作Id业列表，最多一次查10个
     * @return               返回一个集合，包括两部分：
     *                       1. 截图作业列表；
     *                       2. 不存在的截图作业列表，无数据时该部分不返回；
     */
    @RequestMapping(value = "/querysnapshotjoblist", method = RequestMethod.GET)
    @ApiOperation("查询截图作业")
    public ResponseObject querySnapshotJobList(
            @ApiParam("截图作业Id列表")
            @RequestParam(required = true)
            List<String> snapshotJobIds) {

        HashMap collection = snapshotService.querySnapshotJobList(snapshotJobIds);
        ResponseObject res = new ResponseObject();

        res.setData(collection);
        return res;
    }
}
