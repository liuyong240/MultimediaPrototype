package org.multimediaprototype.base;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.DefaultAliyunClient;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.DefaultMNSClient;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by haihong.xiahh on 2015/11/12.
 */

@Component
@Scope("singleton")
public class AliyunClientManagerImpl implements AliyunClientManager {

    // common config
    @Value("#{propsUtil['aliyunConsole.accesskeyId']}")
    private String accessKeyId;
    @Value("#{propsUtil['aliyunConsole.accesskeySecret']}")
    private String accessKeySecret;

    @Value("#{propsUtil['aliyunConsole.ramAccessId']}")
    private String ramAccessKeyId;
    @Value("#{propsUtil['aliyunConsole.ramAccesskeySecret']}")
    private String ramAccessKeySecret;

    // client url
    @Value("#{propsUtil['aliyunConsole.mtsServerUrl']}")
    private String mtsServerUrl;
    @Value("#{propsUtil['aliyunConsole.ossEndpointHz']}")
    private String ossEndpointHz;
    @Value("#{propsUtil['aliyunConsole.mnsEndpointHz']}")
    private String mnsEndpointHz;

    // client
    private AliyunClient mtsClient;
    private OSSClient ossClient;
    private MNSClient mnsClient;
    private DefaultAcsClient stsClient;
    private DefaultAcsClient ramClient;

    private static Logger log = LogManager.getLogger(AliyunClientManagerImpl.class);

    @Override
    @PostConstruct
    public void setup() {
        log.info("ClientManager setup");

        log.debug(ossEndpointHz);
        log.debug(mtsServerUrl);
        log.debug(mnsEndpointHz);

        // init mts client
        mtsClient = new DefaultAliyunClient(mtsServerUrl, accessKeyId, accessKeySecret);

        // init oss client
        ossClient = new OSSClient(ossEndpointHz, accessKeyId, accessKeySecret);

        // init mns client
        CloudAccount cloudAccount = new CloudAccount(accessKeyId, accessKeySecret, mnsEndpointHz);
        mnsClient = cloudAccount.getMNSClient();

        // init sts client
        IClientProfile profile = DefaultProfile.getProfile(Constants.STS_REGION_HANGZHOU, ramAccessKeyId, ramAccessKeySecret);
        stsClient = new DefaultAcsClient(profile);

        // init ram client
        IClientProfile ramProfile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        ramClient = new DefaultAcsClient(ramProfile);
    }

    @Override
    public AliyunClient getMtsClient() {
        return mtsClient;
    }

    @Override
    public OSSClient getOssClient() {
        return ossClient;
    }

    public MNSClient getMnsClient() {
        return mnsClient;
    }

    @Override
    public DefaultAcsClient getSTSClient() {
        return stsClient;
    }

    public DefaultAcsClient getRamClient() {
        return ramClient;
    }
}

