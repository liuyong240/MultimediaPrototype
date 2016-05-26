package org.multimediaprototype.admin.controller.api;

import com.aliyun.api.AliyunResponse;
import com.aliyun.api.domain.Template;
import com.aliyun.api.domain.WaterMarkTemplate;
import com.aliyun.api.mts.mts20140618.response.DeleteTemplateResponse;
import com.aliyun.api.mts.mts20140618.response.DeleteWaterMarkTemplateResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.admin.model.MTSJobTemplate;
import org.multimediaprototype.admin.service.MTSService;
import org.multimediaprototype.common.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dx.yang on 15/11/11.
 */

/**
 * <h1>MTS 模板管理 RESTful接口</h1>
 * <p>用于后台管理的前端页面调用</p>
 * <p>详细使用可查看RESTful文档: <a>http://127.0.0.1:8080/swagger/index.html</a></p>
 */
@RestController
@RequestMapping("/admin/api/mts")
@Api(value = "mts封装")
public class MTSController {

    private Logger logger = LogManager.getLogger(MTSController.class);

    /*
    * 注入MTSService
    * */
    @Autowired
    private MTSService mtsService;

    @RequestMapping(value = "/jt/test", method = RequestMethod.GET)

    public ResponseEntity triggerTranscodeTest() {

        String url = "http://multimedia-input.oss-cn-hangzhou.aliyuncs.com/output/prefix-1449543005321568-suffix.flv";


        return null;
    }

    // 后台设定作业配置，入库。进行转码时，只需在配置中
    // 修改输入和输出文件名等信息，即可进行转码。
    @RequestMapping(value = "/jt", method = RequestMethod.GET)
    @ApiOperation(value = "获取转码模板", notes = "根据id, 名称, 操作人, 是否默认使用等条件查询模板")
    public ResponseObject getJobTemplates(
            @ApiParam("策略id")
            @RequestParam(required = false, defaultValue = "-1")
            long id,
            @ApiParam("策略名称")
            @RequestParam(required = false)
            String name,
            @ApiParam("最后操作人")
            @RequestParam(required = false, defaultValue = "-1")
            long adminId,
            @ApiParam("正在被使用")
            @RequestParam(required = false)
            Boolean using
    ) {
        List<MTSJobTemplate> list = mtsService.getJobTemplates(id, name, adminId, using);
        ResponseObject res = new ResponseObject();
        res.setData(list);
        return res;
    }

    @RequestMapping(value = "/jt", method = RequestMethod.POST)
    @ApiOperation(value = "新建或更新作业模板")
    public ResponseObject addAndUpdateJobTemplate(
            @ApiParam(value = "作业模板的json", required = true)
            @RequestBody
            Map<String, Object> params
    ) {
        Integer result = mtsService.addAndUpdateJobTemplate(params);
        ResponseObject res = new ResponseObject();
        if (result == null) {
            res.setCode(1);
        }
        return res;
    }

    @RequestMapping(value = "/jt/{id}", method = RequestMethod.DELETE)
    @ApiOperation("删除作业模板")
    public ResponseObject delete(
            @ApiParam(value = "作业模板id", required = true)
            @PathVariable
            Integer id
    ) {
        Integer result = mtsService.deleteJobTemplate(id);
        ResponseObject res = new ResponseObject();
        if (result == null) {
            res.setCode(1);
        }
        return res;
    }

    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    @ApiOperation("获取自定义模板")
    public ResponseObject getTemplates(
            @ApiParam("第几页")
            @RequestParam(required = false, defaultValue = "1")
            long pageNumber,
            @ApiParam("每页多少个")
            @RequestParam(required = false, defaultValue = "10")
            long pageSize,
            @ApiParam("模板状态: All, Normal, Deleted")
            @RequestParam(required = false, defaultValue = "All")
            String state
    ) {
        HashMap<String, Object> templates = mtsService.SearchTemplate(pageNumber, pageSize, state);
        ResponseObject res = new ResponseObject();
        if (templates == null) {
            res.setCode(1);
        } else {
            res.setData(templates);
        }
        return res;
    }

    @RequestMapping(value = "/template/{ids}", method = RequestMethod.GET)
    @ApiOperation("根据模板ID获取模板信息")
    public ResponseObject getTemplateById(
            @ApiParam(value = "模板ID, 多个之间用逗号分隔", required = true)
            @PathVariable("ids")
            String ids
    ) {
        List<Template> list = mtsService.QueryTemplateList(ids);
        ResponseObject res = new ResponseObject();
        if (list == null) {
            res.setCode(1);
        } else {
            res.setData(list);
        }
        return res;
    }

    @RequestMapping(value = "/template", method = RequestMethod.POST)
    @ApiOperation(value = "新建或更新模板", notes = "模板json里有id就是更新.没有id就是新建.")
    public ResponseObject addAndUpdateTemplate(
            @ApiParam(value = "模板json", required = true)
            @RequestBody
            Map<String, String> template
    ) {
        /*
        *   模板JSON里存在ID, 调用更新接口
        *   不存在则调用新建接口
        * */
        AliyunResponse aliyunResponse;
        if (template.get("Id") != null) {
            aliyunResponse = mtsService.UpdateTemplate(template);
        } else {
            aliyunResponse = mtsService.AddTemplate(template);
        }

        return new ResponseObject(aliyunResponse);

    }

    @RequestMapping(value = "/template/{id}", method = RequestMethod.DELETE)
    @ApiOperation("删除模板")
    public ResponseObject deleteTemplate(
            @ApiParam(value = "模板id", required = true)
            @PathVariable
            String id
    ) {
        DeleteTemplateResponse aliyunResponse = mtsService.DeleteTemplate(id);
        return new ResponseObject(aliyunResponse);
    }


    @RequestMapping(value = "/watermarks", method = RequestMethod.GET)
    @ApiOperation("获取自定义水印模板")
    public ResponseObject getWaterMarks(
            @ApiParam("第几页")
            @RequestParam(required = false, defaultValue = "1")
            long pageNumber,
            @ApiParam("每页多少个")
            @RequestParam(required = false, defaultValue = "10")
            long pageSize,
            @ApiParam("模板状态: All, Normal, Deleted")
            @RequestParam(required = false, defaultValue = "All")
            String state
    ) {
        Map<String, Object> templates = mtsService.SearchWaterMarkTemplate(pageNumber, pageSize, state);
        ResponseObject res = new ResponseObject();
        if (templates == null) {
            res.setCode(1);
        } else {
            res.setData(templates);
        }
        return res;
    }

    @RequestMapping(value = "/watermark/{ids}", method = RequestMethod.GET)
    @ApiOperation("根据水印模板ID获取模板信息")
    public ResponseObject getWaterMarkTemplateById(
            @ApiParam(value = "水印模板ID, 多个之间用逗号分隔", required = true)
            @PathVariable("ids")
            String ids
    ) {
        List<WaterMarkTemplate> list = mtsService.QueryWaterMarkTemplateList(ids);
        ResponseObject res = new ResponseObject();
        if (list == null) {
            res.setCode(1);
        } else {
            res.setData(list);
        }
        return res;
    }

    @RequestMapping(value = "/watermark", method = RequestMethod.POST)
    @ApiOperation(value = "新建或更新水印模板", notes = "模板JSON里有id是更新操作, 无id是新建操作")
    public ResponseObject addAndUpdateWatermark(
            @ApiParam(value = "水印模板json", required = true)
            @RequestBody
            Map<String, String> template
    ) {
        AliyunResponse aliyunResponse;

        if (template.get("Id") != null) {
            aliyunResponse = mtsService.UpdateWaterMarkTemplate(template);
        } else {
            aliyunResponse = mtsService.AddWaterMarkTemplate(template);
        }

        return new ResponseObject(aliyunResponse);
    }

    @RequestMapping(value = "/watermark/{id}", method = RequestMethod.DELETE)
    @ApiOperation("删除水印模板")
    public ResponseObject deleteWaterMarkTemplate(
            @ApiParam(value = "水印模板id", required = true)
            @PathVariable
            String id
    ) {
        DeleteWaterMarkTemplateResponse aliyunResponse = mtsService.DeleteWaterMarkTemplate(id);
        return new ResponseObject(aliyunResponse);
    }

}


