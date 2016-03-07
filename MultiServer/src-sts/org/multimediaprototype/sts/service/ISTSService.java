package org.multimediaprototype.sts.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

/**
 * Created by haihong.xiahh on 2015/12/22.
 */
public interface ISTSService {
    /**
     *
     * @param roleSessionName，最小长度为2
     * @return
     */
    AssumeRoleResponse assumeReadRole(String roleSessionName) throws ClientException;

    AssumeRoleResponse assumeWriteRole(String roleSessionName) throws ClientException;
}
