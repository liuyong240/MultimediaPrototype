package org.multimediaprototype.mts.service.impl;

import com.aliyun.api.AliyunClient;
import com.aliyun.api.domain.SnapshotJob;
import com.aliyun.api.mts.mts20140618.request.QuerySnapshotJobListRequest;
import com.aliyun.api.mts.mts20140618.request.SubmitSnapshotJobRequest;
import com.aliyun.api.mts.mts20140618.response.QuerySnapshotJobListResponse;
import com.aliyun.api.mts.mts20140618.response.SubmitSnapshotJobResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.mts.service.ISnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
@Service
public class SnapshotService implements ISnapshotService {

    @Autowired
    AliyunClientManager aliyunClientManager;

    private AliyunClient mtsClient;

    // log4j
    private Logger log = LogManager.getLogger(SnapshotService.class);

    @PostConstruct
    public void setupClient() {
        mtsClient = aliyunClientManager.getMtsClient();
    }

    /**
     * 提交截图作业
     *
     * @param input          作业输入，JSON对象
     * @param snapshotConfig 截图配置，JSON对象
     * @param userData       用户自定义数据，最大长度1024字节
     * @return               截图作业对象实例
     */
    @Override
    public SnapshotJob submitSnapshotJob(String input, String snapshotConfig, String userData) {

        try {
            SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest();

            request.setInput(input);
            request.setSnapshotConfig(snapshotConfig);
            request.setUserData(userData);
            request.check(); // 检查输入参数

            SubmitSnapshotJobResponse response = mtsClient.execute(request);
            if(!response.isSuccess()) {
                //throw new RuntimeException("SubmitSnapshotJobRequest failed");
                log.error("SubmitSnapshotJobRequest failed");
            }

            return response.getSnapshotJob();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 查询截图作业
     *
     * @param snapshotJobIds 截图作业Id列表，最多一次查10个
     * @return               返回截图作业列表和不存在的截图作业ID列表集合
     */
    @Override
    public HashMap querySnapshotJobList(List<String> snapshotJobIds) {

        QuerySnapshotJobListRequest request = new QuerySnapshotJobListRequest();
        try {
            for(int index=0; index < snapshotJobIds.size(); index++) {

                request.setSnapshotJobIds(snapshotJobIds.get(index));
            }
            request.check(); // 检查输入参数

            QuerySnapshotJobListResponse response = mtsClient.execute(request);
            if(!response.isSuccess()) {
                //throw new RuntimeException("QuerySnapshotJobListRequest failed");
                log.error("QuerySnapshotJobListRequest failed");
            }

            //ollection<Object> collection = new HashSet<>();
            HashMap map = new HashMap();

            List<SnapshotJob> snapshotJobList = response.getSnapshotJobList();
            List<String> nonExistSanpshotJobIds = response.getNonExistSnapshotJobIds();

            map.put("snapshotJobs", snapshotJobList);
            map.put("nonExistSanpshotJobIds", nonExistSanpshotJobIds);

            return map;

        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
            return null;
        }
    }
}
