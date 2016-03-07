package org.multimediaprototype.sts.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.CreateAccessKeyResponse;
import com.aliyuncs.ram.model.v20150501.CreatePolicyResponse;
import com.aliyuncs.ram.model.v20150501.CreateRoleResponse;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;

/**
 * Created by haihong.xiahh on 2015/12/29.
 */
public interface IRAMService {
    void createRAMUser(String userName) throws ClientException;
    void deleteRAMUser(String userName) throws ClientException;

    CreateAccessKeyResponse.AccessKey createAccessKey(String userName) throws ClientException;
    ListAccessKeysResponse.AccessKey getAccessKey(String userName) throws ClientException;

    CreatePolicyResponse.Policy createAssumeOSSWriteRolePolicy(String policyName, String roleName) throws ClientException;
    CreatePolicyResponse.Policy createAssumeOSSReadRolePolicy(String policyName, String roleName) throws ClientException;
    CreatePolicyResponse.Policy createOSSReadPolicy(String policyName) throws ClientException;
    CreatePolicyResponse.Policy createOSSWritePolicy(String policyName) throws ClientException;
    void deleteRAMPolicy(String policyName) throws ClientException;

    CreateRoleResponse.Role createRAMRole(String roleName, String roleDesc) throws ClientException;
    void deleteRAMRole(String roleName) throws ClientException;

    void attachPolicyToUser(String userName, String policyName) throws ClientException;
    void attachPolicyToRole(String roleName, String policyName) throws ClientException;

    void detachPolicyFromUser(String userName, String policyName) throws ClientException;
    void detachPolicyFromRole(String roleName, String policyName) throws ClientException;
}
