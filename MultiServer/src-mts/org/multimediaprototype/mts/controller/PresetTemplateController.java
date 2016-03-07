package org.multimediaprototype.mts.controller;

import com.aliyun.api.domain.AnalysisJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.mts.service.IPresetTemplateSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
@RestController
@RequestMapping("/api/mts/presettemplate")
@Api(value = "MTS预置模板")
public class PresetTemplateController {

    @Autowired
    private IPresetTemplateSerivce presetTemplateSerivce;

    /**
     * 查询模板分析作业
     *
     * @param analysisJobIds 模板分析作业Id列表
     * @return               返回一个集合对象，包括两部分
     *                       1. 模板分析作业列表
     *                       2. 不存在的模板分析作业ID列表，无数据时该结构不返回
     */
    @RequestMapping(value = "/queryanalysisjoblist", method = RequestMethod.GET)
    @ApiOperation("查询模板分析作业")
    public ResponseObject queryAnalysisJobList(
            @ApiParam("查询作业列表")
            @RequestParam(required = true)
            List<String> analysisJobIds) {

        HashMap collection = presetTemplateSerivce.queryAnalysisJobList(analysisJobIds);
        ResponseObject res = new ResponseObject();

        res.setData(collection);
        return res;
    }

    /**
     * 提交模板分析作业
     *
     * @param input      输入文件信息，JSON对象
     * @param priority   任务在管道内转码优先级，范围[1-10]，默认6，10为最高优先级
     * @param userData   用户自定义数据
     * @param pipelineId 管道ID
     * @return           模板分析作业对象实例
     */
    @RequestMapping(value = "/submitanalysisjob", method = RequestMethod.POST)
    @ApiOperation("提交模板分析作业")
    public ResponseObject submitAnalysisJob(
            @ApiParam("输入文件信息，JSON对象")
            @RequestParam(required = true)
            String input,
            @ApiParam("任务在管道内转码优先级，范围[1-10]，默认6，10为最高优先级")
            @RequestParam(required = false)
            String priority,
            @ApiParam("用户自定义数据")
            @RequestParam(required = false)
            String userData,
            @ApiParam("管道ID")
            @RequestParam(required = true)
            String pipelineId) {

        AnalysisJob analysisJob = presetTemplateSerivce.submitAnalysisJob(input, priority, userData, pipelineId);
        ResponseObject res = new ResponseObject();
        if(analysisJob == null) {
            res.setCode(1);
        }
        res.setData(analysisJob);

        return res;
    }
}
