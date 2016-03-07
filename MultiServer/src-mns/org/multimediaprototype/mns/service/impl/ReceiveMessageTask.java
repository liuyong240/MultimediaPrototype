package org.multimediaprototype.mns.service.impl;

import com.aliyun.mns.client.MNSClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.client.CloudQueue;


/**
 * Created by zhuowu.zm on 2015/12/7.
 */
public class ReceiveMessageTask extends RecursiveTask<List<Message>> {

    private static final long serialVersionUID = 1090632135694267263L;

    private static Logger logger = LogManager.getLogger(ReceiveMessageTask.class);

    private String queueName;

    private Integer messageCounts; // 传入的消息总数

    private CloudQueue cloudQueue;

    private MNSClient mnsClient;

    private List<Message> totalReceiveMessages = new ArrayList<>();

    private final static int defaultMaxPeekCounts = 16; // MNS一次只能peek最多16条数据，大于该数会报错

    public ReceiveMessageTask(String queueName,
                              Integer messageCounts,
                              MNSClient mnsClient,
                              CloudQueue cloudQueue) {

        this.queueName = queueName;
        this.messageCounts = messageCounts;
        this.mnsClient = mnsClient;
        this.cloudQueue = cloudQueue;
    }


    protected List<Message> compute() {

        try{
            if(messageCounts == 0) {
                return null;
            }
            if(messageCounts == 1){
                List<Message> msglist = cloudQueue.batchPeekMessage(messageCounts);

                totalReceiveMessages.addAll(msglist);
                return totalReceiveMessages;
            }
            // 消息数目大于等于2时可能产生分片
            else {

                // 拆分成子任务
                List<ReceiveMessageTask> receiveTasks = createSubReceiveTasks(messageCounts);
                for(ReceiveMessageTask subreceiveTask: receiveTasks) {
                    subreceiveTask.fork();
                }

                for(ReceiveMessageTask subreceiveTask: receiveTasks) {

                    totalReceiveMessages.addAll(subreceiveTask.join());
                }
                return totalReceiveMessages;
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    private List<ReceiveMessageTask> createSubReceiveTasks(int messageCounts) {
        //System.out.println("进入createSubReceiveTasks函数执行...");
        // 保存被拆分的任务列表
        List<ReceiveMessageTask> subTasks = new ArrayList<>();

        int actualPeekCounts = checkMaxPeekCounts(messageCounts);
        List<Message> messageList = cloudQueue.batchPeekMessage(actualPeekCounts);

        totalReceiveMessages.addAll(messageList); // 将peek到的消息加入到totalReceiveMessages列表

        // 一次Peek之后剩余未取出的消息数
        int remainCounts = messageCounts - messageList.size();

        if(remainCounts > 0){
            ReceiveMessageTask subTask = new ReceiveMessageTask(queueName, remainCounts, mnsClient, cloudQueue);
            subTasks.add(subTask);
        }else {

        }

        return subTasks;
    }

    /**
     * 检查传入bathPeekMessage方法中的参数
     * 默认不超过16
     *
     * @param peekCounts 传入的peek参数
     * @return 返回正确的peek参数
     */
    protected int checkMaxPeekCounts(int peekCounts) {
        if(peekCounts <= 0)
            return 0;
        if(peekCounts > defaultMaxPeekCounts)
            return defaultMaxPeekCounts;
        else
            return peekCounts;
    }
}
