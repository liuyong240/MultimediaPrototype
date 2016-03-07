package org.multimediaprototype.test.multimedia.mns;


import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;
import org.junit.Test;
import org.multimediaprototype.mns.service.ScheduleService;
import org.multimediaprototype.mns.service.impl.MNSService;
import org.multimediaprototype.mns.service.impl.ReceiveMessageTask;
import org.multimediaprototype.mts.service.impl.TranscodeService;
import org.multimediaprototype.test.SprintJunitTest;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.concurrent.ForkJoinPool;


/**
 * Created by zhuowu.zm on 2015/11/30.
 */
public class MNSTest extends SprintJunitTest {

    @Autowired
    private TranscodeService transcodeService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MNSService mnsService;

    // MTS转码设置
    // 设置用户ID为2001，以区别正式用户ID
    private static final Long testUserId = 2001L;
    private static final String testLocation = "oss-cn-hangzhou";
    private static final String testBucket = "multimedia-input";
    private static final String testFile = "input/sample.mp4";

    private static final String testQueueName = "hz-mizhon-test";


    /**
     * 数据准备
     * 向MNS队列插入测试数据
     */
//    @Before
//    public void setUp() {
//
//        //createQueue(testQueueName);
//        //insertMessageToQueue(insertCounts);
//    }


//    @Test
//    public void test_insertMessages() {
//
//        Integer insertCounts = 17;
//        for (int index = 0; index < insertCounts; index++) {
//            System.out.println("Insert around: " + (index + 1));
//            transcodeService.submitTranscodeJobs(testUserId, testFile, testBucket, testLocation);
//        }
//    }


    @Test
         public void test_ForkJoinPool() {

        String mns_accessKeyId = ServiceSettings.getMNSAccessKeyId();
        String mns_accessSecret = ServiceSettings.getMNSAccessKeySecret();
        String mns_endPoint = ServiceSettings.getMNSAccountEndpoint();

        CloudAccount account = new CloudAccount(
                mns_accessKeyId,
                mns_accessSecret,
                mns_endPoint);

        MNSClient mnsClient = account.getMNSClient();
        CloudQueue cloudQueue = mnsClient.getQueueRef(testQueueName);

        // 获取CPU核数
        Integer cpus = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(cpus);

        // 并非精确值
        int totalMsgCounts = (int)(cloudQueue.getAttributes().getActiveMessages() +
                                   cloudQueue.getAttributes().getInactiveMessages());
        System.out.println("队列 {" + testQueueName + "} 中总消息数：" + totalMsgCounts);

        ReceiveMessageTask task = new ReceiveMessageTask(testQueueName, totalMsgCounts, mnsClient, cloudQueue);
        List<Message> myresults = forkJoinPool.invoke(task);

        System.out.println("获取消息总数: " + myresults.size());
        for(Message msg: myresults) {
            System.out.println("[" + msg.getMessageBodyAsString() + "]");
        }
    }


    /**
     * 删除队列中所有消息
     */
    @Test
    public void test_batchDeleteMessage(){
        //clearAllMessage();
    }

    private void createQueue(String queueName) {

        try {
            QueueMeta queueMeta = new QueueMeta();
            queueMeta.setQueueName(queueName);
            mnsService.createQueue(queueMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertMessageToQueue(int insertRecords) {

        // 插入MNS队列的记录条数
        for(int index = 0; index < insertRecords; index++) {
            System.out.println("Insert around: " + (index + 1));
            transcodeService.submitTranscodeJobs(testUserId, testFile, testBucket, testLocation);
        }
    }

    private void clearAllMessage() {

        mnsService.deleteAllMessages(testQueueName);
        //deleteQueue(queueName);
    }

    private void deleteQueue(String queueName) {

        mnsService.deleteQueue(queueName);
    }
}
