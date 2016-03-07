package org.multimediaprototype.sts.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.common.util.FileUtil;
import org.multimediaprototype.common.util.StringUtil;
import org.multimediaprototype.sts.service.IRAMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haihong.xiahh on 2015/12/29.
 */
@Service
@Scope("singleton")
public class RAMService implements IRAMService {
    @Autowired
    private AliyunClientManager aliyunClientManager;
    private DefaultAcsClient ramClient;
    private Logger log = LogManager.getLogger(RAMService.class);
    private String POLICY_SOURCE_DIR;

    @Value("#{propsUtil['aliyunConsole.accountId']}")
    private String accountId;
    @Value("#{propsUtil['aliyunConsole.bucket']}")
    private String defaultBucket;
    @Value("#{propsUtil['aliyunConsole.stsWriteSubdir']}")
    private String defaultSubDir;


    @PostConstruct
    public void setupClient() {
        log.debug("RAM Service setup");
        ramClient = aliyunClientManager.getRamClient();
        POLICY_SOURCE_DIR = this.getClass().getResource("policy").getPath();
        log.error(POLICY_SOURCE_DIR);
    }


    @Override
    public void createRAMUser(String userName) throws ClientException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(userName);
        ramClient.getAcsResponse(request);
    }

    @Override
    public void deleteRAMUser(String userName) throws ClientException {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setUserName(userName);
        ramClient.getAcsResponse(request);
    }

    public CreateAccessKeyResponse.AccessKey createAccessKey(String userName) throws ClientException {
        CreateAccessKeyRequest request = new CreateAccessKeyRequest();
        request.setUserName(userName);
        CreateAccessKeyResponse response = ramClient.getAcsResponse(request);
        return response.getAccessKey();
    }

    public ListAccessKeysResponse.AccessKey getAccessKey(String userName) throws ClientException {
        ListAccessKeysRequest request = new ListAccessKeysRequest();
        request.setUserName(userName);
        ListAccessKeysResponse response = ramClient.getAcsResponse(request);
        List<ListAccessKeysResponse.AccessKey> keys = response.getAccessKeys();
        if (keys != null && keys.size() > 1) {
            return keys.get(0);
        }
        throw new ClientException("key not exists");
    }

    private CreatePolicyResponse.Policy createPolicy(String policyName, String policyDocument) throws ClientException {
        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setPolicyName(policyName);
        request.setPolicyDocument(policyDocument);
        CreatePolicyResponse response = ramClient.getAcsResponse(request);
        return response.getPolicy();
    }

    public CreatePolicyResponse.Policy createOSSWritePolicy(String policyName) throws ClientException {
        // init policyDocument
        String policyDocument = FileUtil.getFileContent(String.format("%s/oss_write_policy.json", POLICY_SOURCE_DIR));
        Map<String, String> policyData = new HashMap<>();
        policyData.put("bucketName", defaultBucket);
        policyData.put("subDir", defaultSubDir);
        policyDocument = StringUtil.template(policyDocument, policyData);

        // do request
        return createPolicy(policyName, policyDocument);
    }

    public CreatePolicyResponse.Policy createOSSReadPolicy(String policyName) throws ClientException {
        // init policyDocument
        String policyDocument = FileUtil.getFileContent(String.format("%s/oss_read_policy.json", POLICY_SOURCE_DIR));
        Map<String, String> policyData = new HashMap<>();
        policyData.put("bucketName", defaultBucket);
        policyData.put("subDir", defaultSubDir);
        policyDocument = StringUtil.template(policyDocument, policyData);

        // do request
        return createPolicy(policyName, policyDocument);
    }

    public CreatePolicyResponse.Policy createAssumeOSSReadRolePolicy(String policyName, String roleName) throws ClientException {
        // init policyDocument
        String policyDocument = FileUtil.getFileContent(String.format("%s/assume_oss_read_role_policy.json", POLICY_SOURCE_DIR));
        Map<String, String> policyData = new HashMap<>();
        policyData.put("accountId", accountId);
        policyData.put("roleName", roleName);
        policyDocument = StringUtil.template(policyDocument, policyData);

        // do request
        return createPolicy(policyName, policyDocument);
    }

    public CreatePolicyResponse.Policy createAssumeOSSWriteRolePolicy(String policyName, String roleName) throws ClientException {
        // init policyDocument
        String policyDocument = FileUtil.getFileContent(String.format("%s/assume_oss_write_role_policy.json", POLICY_SOURCE_DIR));
        Map<String, String> policyData = new HashMap<>();
        policyData.put("accountId", accountId);
        policyData.put("roleName", roleName);
        policyDocument = StringUtil.template(policyDocument, policyData);

        // do request
        return createPolicy(policyName, policyDocument);
    }


    @Override
    public void deleteRAMPolicy(String policyName) throws ClientException {
        // list policy versions and delete
        ListPolicyVersionsRequest listPolicyVersionsRequest = new ListPolicyVersionsRequest();
        listPolicyVersionsRequest.setPolicyName(policyName);
        listPolicyVersionsRequest.setPolicyType("Custom");
        try {
            ListPolicyVersionsResponse listPolicyVersionsResponse = ramClient.getAcsResponse(listPolicyVersionsRequest);
            List<ListPolicyVersionsResponse.PolicyVersion> policyVersions = listPolicyVersionsResponse.getPolicyVersions();
            if (policyVersions != null) {
                for (ListPolicyVersionsResponse.PolicyVersion policyVersion : policyVersions) {
                    DeletePolicyVersionRequest request = new DeletePolicyVersionRequest();
                    request.setPolicyName(policyName);
                    request.setVersionId(policyVersion.getVersionId());
                    ramClient.getAcsResponse(request);
                }
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        // delete policy version
        DeletePolicyRequest request = new DeletePolicyRequest();
        request.setPolicyName(policyName);
        ramClient.getAcsResponse(request);
    }


    @Override
    public CreateRoleResponse.Role createRAMRole(String roleName, String roleDesc) throws ClientException {
        // init policyDocument
        String rolePolicy = FileUtil.getFileContent(String.format("%s/role_policy_4_ramuser.json", POLICY_SOURCE_DIR));
        Map<String, String> policyData = new HashMap<>();
        policyData.put("accountId", accountId);
        rolePolicy = StringUtil.template(rolePolicy, policyData);

        // do request
        CreateRoleRequest request = new CreateRoleRequest();
        request.setRoleName(roleName);
        request.setDescription(roleDesc);
        request.setAssumeRolePolicyDocument(rolePolicy);
        CreateRoleResponse response = ramClient.getAcsResponse(request);
        return response.getRole();
    }

    @Override
    public void deleteRAMRole(String roleName) throws ClientException {
        DeleteRoleRequest request = new DeleteRoleRequest();
        request.setRoleName(roleName);
        ramClient.getAcsResponse(request);
    }

    @Override
    public void attachPolicyToUser(String userName, String policyName) throws ClientException {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(userName);
        request.setPolicyName(policyName);
        request.setPolicyType("Custom");
        ramClient.getAcsResponse(request);
    }

    @Override
    public void attachPolicyToRole(String roleName, String policyName) throws ClientException {
        AttachPolicyToRoleRequest request = new AttachPolicyToRoleRequest();
        request.setPolicyName(policyName);
        request.setRoleName(roleName);
        request.setPolicyType("Custom");
        ramClient.getAcsResponse(request);
    }

    @Override
    public void detachPolicyFromUser(String userName, String policyName) throws ClientException {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(userName);
        request.setPolicyName(policyName);
        request.setPolicyType("Custom");
        ramClient.getAcsResponse(request);
    }

    @Override
    public void detachPolicyFromRole(String roleName, String policyName) throws ClientException {
        DetachPolicyFromRoleRequest request = new DetachPolicyFromRoleRequest();
        request.setPolicyName(policyName);
        request.setRoleName(roleName);
        request.setPolicyType("Custom");
        ramClient.getAcsResponse(request);
    }
}
