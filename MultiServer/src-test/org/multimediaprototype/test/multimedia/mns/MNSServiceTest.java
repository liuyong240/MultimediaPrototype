package org.multimediaprototype.test.multimedia.mns;

import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;
import org.junit.Assert;
import org.junit.Test;
import org.multimediaprototype.mns.service.ScheduleService;
import org.multimediaprototype.mns.service.impl.MNSService;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.test.SprintJunitTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by zhuowu.zm on 2015/12/5.
 */

public class MNSServiceTest extends SprintJunitTest {

    @Autowired
    private MNSService mnsService;
    @Autowired
    private TranscodeService transcodeService;

    // MTS转码设置
    // 设置用户ID为2001，以区别正式用户ID
    private static final Long testUserId = 2001L;
    private static final String testLocation = "oss-cn-hangzhou";
    private static final String testBucket = "multimedia-input";
    private static final String testFile = "input/sample.mp4";

    private static Logger log = LogManager.getLogger(MNSServiceTest.class);

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 检查listUniqueQueueByName函数
     */
    @Test
    public void test_listUniqueQueueByName() {

        List<QueueMeta> queueMetaList = mnsService.listQueueByName("hz-mizhon-test");
        for(QueueMeta queueMeta: queueMetaList) {
            String name = queueMeta.getQueueName();
            log.debug("Queue name: " + name);
            System.out.println("Queue name: " + name);
        }
        Assert.assertEquals(queueMetaList.size(), 1);
    }

    /**
     * 插入测试数据
     */
    @Test
    public void test_insertMessages() {

        Integer insertCounts = 3;
        for (int index = 0; index < insertCounts; index++) {
            System.out.println("Insert around: " + (index + 1));
            transcodeService.submitTranscodeJobs(testUserId, testFile, testBucket, testLocation);
        }
    }

    @Test
    public void test_deleteMessage() {

        List<Message> messages = mnsService.batchViewMessage("hz-mizhon-test");
        for(Message msg: messages) {
            String jobId = msg.getMessageBodyAsString();
            boolean isdleted = mnsService.deleteMessage(jobId);
            if(isdleted){
                System.out.println("Delete jobId: [" + jobId + "]");
            }else {
                System.out.println("NOT delete jobId: [" + jobId + "]");
            }
        }
    }

    @Test
    public void test_ScheduleService() {

        scheduleService.schedule();
    }
}
