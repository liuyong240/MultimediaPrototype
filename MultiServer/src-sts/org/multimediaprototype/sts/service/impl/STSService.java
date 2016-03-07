package org.multimediaprototype.sts.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.common.util.FileUtil;
import org.multimediaprototype.common.util.StringUtil;
import org.multimediaprototype.sts.service.ISTSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/12/22.
 */
@Service
@Scope("singleton")
public class STSService implements ISTSService {
    public static final String STS_API_VERSION = "2015-04-01";

    @Autowired
    private AliyunClientManager aliyunClientManager;

    private DefaultAcsClient stsClient;
    private Logger log = LogManager.getLogger(STSService.class);
    private String READ_POLICY,WRITE_POLICY;

    @Value("#{propsUtil['aliyunConsole.accountId']}")
    private String accountId;
    @Value("#{propsUtil['aliyunConsole.bucket']}")
    private String defaultBucket;
    @Value("#{propsUtil['aliyunConsole.stsWriteSubdir']}")
    private String defaultPath;
    @Value("#{propsUtil['aliyunConsole.ramOSSReadRoleName']}")
    private String ramOSSReadRoleName;
    @Value("#{propsUtil['aliyunConsole.ramOSSWriteRoleName']}")
    private String ramOSSWriteRoleName;

    private static final String BASE_ACCESS = "acs:ram::{accountId}:role/{roleName}";
    private String readAccess;
    private String writeAccess;

    @PostConstruct
    public void setupClient() {
        log.debug("STS Service setup");
        // init client
        stsClient = aliyunClientManager.getSTSClient();

        // read policy from file
        String readPolicyFilePath = this.getClass().getResource("policy/read_policy_4_appuser.json").getPath();
        String writePolicyFilePath = this.getClass().getResource("policy/write_policy_4_appuser.json").getPath();
        READ_POLICY = FileUtil.getFileContent(readPolicyFilePath);
        WRITE_POLICY = FileUtil.getFileContent(writePolicyFilePath);
        log.error(READ_POLICY);
        log.error(WRITE_POLICY);

        // init roleArn
        readAccess = StringUtil.template(BASE_ACCESS, new HashMap<String, String>(){
            {
                put("accountId", accountId);
                put("roleName", ramOSSReadRoleName);
            }
        });
        writeAccess = StringUtil.template(BASE_ACCESS, new HashMap<String, String>(){
            {
                put("accountId", accountId);
                put("roleName", ramOSSWriteRoleName);
            }
        });
    }

    /**
     * 将获取的读权限路径为 multimedia-input/user/roleSessionName
     * @param roleSessionName 用于区分用户的tag，如用户ID
     * @return
     */
    @Override
    public AssumeRoleResponse assumeReadRole(final String roleSessionName) throws ClientException {
        String policy = StringUtil.template(READ_POLICY, new HashMap<String, String>() {
            {
                put("bucketName", defaultBucket);
                put("subDir", defaultPath);;
                put("roleSessionName", roleSessionName);
            }
        });
        log.info(policy);
        return assumeRole(readAccess, roleSessionName, policy);
    }

    /**
     * 将获取的读权限路径为 multimedia-input/user/roleSessionName
     * @param roleSessionName 用于区分用户的tag，如用户ID
     * @return
     */
    @Override
    public AssumeRoleResponse assumeWriteRole(final String roleSessionName) throws ClientException {
        String policy = StringUtil.template(WRITE_POLICY, new HashMap<String, String>() {
                    {
                        put("bucketName", defaultBucket);
                        put("subDir", defaultPath);;
                        put("roleSessionName", roleSessionName);
                    }
                }
        );
        log.info(policy);
        return assumeRole(writeAccess, roleSessionName, policy);
    }

    private AssumeRoleResponse assumeRole(String roleArn, String roleSessionName, String policy) throws ClientException {
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(ProtocolType.HTTPS);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);

            // 发起请求，并得到response
            AssumeRoleResponse response = stsClient.getAcsResponse(request);
            log.debug(response.getCredentials().getSecurityToken());
            log.debug(response.getCredentials().getExpiration());
            return response;
    }
}
