package org.multimediaprototype.test.multimedia.sts;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.ResponseParsers;
import com.aliyun.oss.model.*;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.junit.Assert;
import org.junit.Test;
import org.multimediaprototype.base.Constants;
import org.multimediaprototype.sts.service.impl.STSService;
import org.multimediaprototype.test.SprintJunitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * Created by haihong.xiahh on 2015/12/22.
 */
public class STSTest extends SprintJunitTest {

    @Autowired
    private STSService stsService;
    @Value("#{propsUtil['aliyunConsole.bucket']}")
    private String defaultBucket;
    @Value("#{propsUtil['aliyunConsole.stsWriteSubdir']}")
    private String defaultPath;
    @Value("#{propsUtil['aliyunConsole.ramAccessId']}")
    private String ramAccessKeyId;
    @Value("#{propsUtil['aliyunConsole.ramAccesskeySecret']}")
    private String ramAccessKeySecret;
    @Value("#{propsUtil['aliyunConsole.ossEndpointHz']}")
    private String ossEndpoint;

    public static final String objectName = "test.mp3";
    public static final String sessionName = "test-xhh";

    @Test
    public void testUploadFileUseSTSToken() {
        // 1. put/get with ram client, expected failed
        OSSClient ossClient = new OSSClient(ossEndpoint, ramAccessKeyId, ramAccessKeySecret);
        File file = new File("src-test/org/multimediaprototype/test/multimedia/sts/test.mp3");
        checkPutObject(ossClient, file, false);
        checkGetObject(ossClient, false);
        checkListObjects(ossClient, false);

        // 2.assume write role, expected success
        AssumeRoleResponse writeResponse = null;
        try {
            writeResponse = stsService.assumeWriteRole(sessionName);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            Assert.assertTrue(e.getMessage(), true);
        }
        Assert.assertNotNull(writeResponse);
        String writeAccessId = writeResponse.getCredentials().getAccessKeyId();
        String writeAccessKey = writeResponse.getCredentials().getAccessKeySecret();
        String writeToken = writeResponse.getCredentials().getSecurityToken();

        // 3. put object with write role, expected success
        OSSClient ossClientWrite = new OSSClient(ossEndpoint, writeAccessId, writeAccessKey, writeToken);
        checkPutObject(ossClientWrite, file, true);

        // 3. get object / list objects with read role, expected success
        AssumeRoleResponse readResponse = null;
        try {
            readResponse = stsService.assumeReadRole(sessionName);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            Assert.assertTrue(e.getMessage(), true);
        }
        Assert.assertNotNull(readResponse);
        String readAccessId = readResponse.getCredentials().getAccessKeyId();
        String readAccessKey = readResponse.getCredentials().getAccessKeySecret();
        String readToken = readResponse.getCredentials().getSecurityToken();
        OSSClient ossClientRead = new OSSClient(ossEndpoint, readAccessId, readAccessKey, readToken);
        checkGetObject(ossClientRead, true);
        checkListObjects(ossClientRead, true);

    }

    private String getObjectFullName() {
        return String.format("%s/%s/%s", defaultPath, sessionName, objectName);
    }

    private void checkPutObject(OSSClient ossClient, File file, boolean expectedRet) {
        boolean ret;
        try {
            ossClient.putObject(defaultBucket, getObjectFullName(), file);
            ret = true;
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
            ret = false;
        }
        Assert.assertEquals(expectedRet, ret);
    }

    private void checkGetObject(OSSClient ossClient, boolean expectedRet) {
        boolean ret;
        try {
            ossClient.getObject(defaultBucket, getObjectFullName());
            ret = true;
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
            ret = false;
        }
        Assert.assertEquals(expectedRet, ret);
    }

    private void checkListObjects(OSSClient ossClient, boolean expectedRet) {
        boolean ret;
        try {
            ossClient.listObjects(defaultBucket, getObjectFullName());
            ret = true;
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
            ret = false;
        }
        Assert.assertEquals(expectedRet, ret);
    }
}
