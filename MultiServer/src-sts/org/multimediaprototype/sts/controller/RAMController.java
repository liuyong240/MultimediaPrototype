package org.multimediaprototype.sts.controller;

import com.aliyuncs.exceptions.ClientException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.common.model.ResponseObject;
import org.multimediaprototype.sts.service.impl.RAMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by haihong.xiahh on 2015/12/30.
 */
@RestController
@RequestMapping("/api/ram")
@Api(value = "RAM封装")
public class RAMController {
    @Autowired
    private RAMService ramService;

    @Value("#{propsUtil['aliyunConsole.ramUserName']}")
    private String ramUserName;
    @Value("#{propsUtil['aliyunConsole.ramOSSReadRoleName']}")
    private String ramOSSReadRoleName;
    @Value("#{propsUtil['aliyunConsole.ramOSSWriteRoleName']}")
    private String ramOSSWriteRoleName;
    @Value("#{propsUtil['aliyunConsole.ramOSSReadPolicyName']}")
    private String ramOSSReadPolicyName;
    @Value("#{propsUtil['aliyunConsole.ramOSSWritePolicyName']}")
    private String ramOSSWritePolicyName;
    @Value("#{propsUtil['aliyunConsole.ramAssumeOSSReadRolePolicyName']}")
    private String ramAssumeOSSReadRolePolicyName;
    @Value("#{propsUtil['aliyunConsole.ramAssumeOSSWriteRolePolicyName']}")
    private String ramAssumeOSSWriteRolePolicyName;

    private Logger log = LogManager.getLogger(RAMController.class);

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation("根据已有的子帐号，创建相关角色和权限")
    public ResponseObject initRAM() {
        ResponseObject responseObject = new ResponseObject();
        // create ram role
        try {
            ramService.createRAMRole(ramOSSReadRoleName, "OSS Read Role");
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create ram role failed for [%s], %s",
                    ramOSSReadRoleName, e.getMessage()));
            return responseObject;
        }

        try {
            ramService.createRAMRole(ramOSSWriteRoleName, "OSS Write Role");
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create ram role failed for [%s], %s",
                    ramOSSWriteRoleName, e.getMessage()));
            return responseObject;
        }

        // create oss read policy
        try {
            ramService.createOSSReadPolicy(ramOSSReadPolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create oss read policy failed for [%s], %s",
                    ramOSSReadPolicyName, e.getMessage()));
            return responseObject;
        }

        // attach policy to role
        try {
            ramService.attachPolicyToRole(ramOSSReadRoleName, ramOSSReadPolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("attach policy[%s] to role[%s] failed, %s",
                    ramOSSReadPolicyName, ramOSSReadRoleName, e.getMessage()));
            return responseObject;
        }

        // create oss write policy
        try {
            ramService.createOSSWritePolicy(ramOSSWritePolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create oss write policy failed for [%s], %s",
                    ramOSSWritePolicyName, e.getMessage()));
            return responseObject;
        }

        // attach policy to role
        try {
            ramService.attachPolicyToRole(ramOSSWriteRoleName, ramOSSWritePolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("attach policy[%s] to role[%s] failed, %s",
                    ramOSSWritePolicyName, ramOSSWriteRoleName, e.getMessage()));
            return responseObject;
        }

        // create assume oss read role policy
        try {
            ramService.createAssumeOSSReadRolePolicy(ramAssumeOSSReadRolePolicyName, ramOSSReadRoleName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create assume oss read role policy [%s] failed for role [%s], %s",
                    ramAssumeOSSReadRolePolicyName, ramOSSReadRoleName, e.getMessage()));
            return responseObject;
        }

        // attach policy to user
        try {
            ramService.attachPolicyToUser(ramUserName, ramAssumeOSSReadRolePolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("attach policy [%s] to user [%s] failed, %s",
                    ramAssumeOSSReadRolePolicyName, ramUserName, e.getMessage()));
            return responseObject;
        }

        // create assume oss write role policy
        try {
            ramService.createAssumeOSSWriteRolePolicy(ramAssumeOSSWriteRolePolicyName, ramOSSWriteRoleName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("create assume oss read role policy [%s] failed for role [%s], %s",
                    ramAssumeOSSWriteRolePolicyName, ramOSSWriteRoleName, e.getMessage()));
            return responseObject;
        }

        // attach policy to user
        try {
            ramService.attachPolicyToUser(ramUserName, ramAssumeOSSWriteRolePolicyName);
        } catch (ClientException e) {
            responseObject.setCode(1);
            responseObject.setError(String.format("attach policy [%s] to uesr [%s] failed, %s",
                    ramAssumeOSSWriteRolePolicyName, ramUserName, e.getMessage()));
            return responseObject;
        }

        responseObject.setCode(0);
        return responseObject;
    }


    @RequestMapping(value = "/destroy", method = RequestMethod.GET)
    @ApiOperation("销毁子帐号的相关角色和权限，但子帐号不销毁")
    public ResponseObject destroyRAM() {
        ResponseObject responseObject = new ResponseObject();
        int ret = 0;
        String error = "";


        String[] assumeRolePolicys = {ramAssumeOSSReadRolePolicyName, ramAssumeOSSWriteRolePolicyName};
        for (String assumeRolePolicy : assumeRolePolicys) {
            // detach policy from user
            try {
                ramService.detachPolicyFromUser(ramUserName, assumeRolePolicy);
            } catch (ClientException e) {
                ret += 1;
                error += String.format("detach policy [%s] from user [%s] failed, %s \n",
                        ramUserName, assumeRolePolicy, e.getMessage());
            }

            // delete policy
            try {
                ramService.deleteRAMPolicy(assumeRolePolicy);
            } catch (ClientException e) {
                ret += 1;
                error += String.format("delete policy [%s] failed, %s \n", assumeRolePolicy, e.getMessage());
            }
        }

        Map<String, String> rolePolicyMap = new HashMap<String, String>() {
            {
                put(ramOSSReadRoleName, ramOSSReadPolicyName);
                put(ramOSSWriteRoleName, ramOSSWritePolicyName);
            }
        };
        for (String roleName : rolePolicyMap.keySet()) {
            String policyName = rolePolicyMap.get(roleName);
            // detach policy from role
            try {
                ramService.detachPolicyFromRole(roleName, policyName);
            } catch (ClientException e) {
                ret += 1;
                error += String.format("detach policy [%s] from role [%s] failed, %s \n", roleName, policyName, e.getMessage());
            }
            // delete policy
            try {
                ramService.deleteRAMRole(roleName);
            } catch (ClientException e) {
                ret += 1;
                error += String.format("delete role [%s] failed, %s \n", roleName, e.getMessage());
            }
            // delete policy
            try {
                ramService.deleteRAMPolicy(policyName);
            } catch (ClientException e) {
                ret += 1;
                error += String.format("delete policy [%s] failed, %s \n", policyName, e.getMessage());
            }
        }
        responseObject.setCode(ret);
        responseObject.setError(error);
        return responseObject;
    }

}
