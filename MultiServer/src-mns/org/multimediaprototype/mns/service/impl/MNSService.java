package org.multimediaprototype.mns.service.impl;


import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.PagingListResult;
import com.aliyun.mns.model.QueueMeta;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.multimediaprototype.base.AliyunClientManager;
import org.multimediaprototype.mns.service.IMNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by haihong.xiahh on 2015/12/1.
 */
@Service
public class MNSService implements IMNSService {

    @Autowired
    private AliyunClientManager aliyunClientManager;

    @Value("#{propsUtil['aliyunConsole.mnsQueneName']}")
    private String defaultMNSQueueName;

    @PostConstruct
    public void setupClient() {
        mnsClient = aliyunClientManager.getMnsClient();
    }

    private MNSClient mnsClient;

    private static Logger logger = LogManager.getLogger(MNSService.class);

    /**
     * 向默认队列发送消息
     *
     * @param msgContext 要发送的消息内容
     * @return           发送成功返回true，发送失败返回false
     */
    @Override
    public boolean sendMessage(String msgContext) {
        // 默认指定队列
        String queueName = defaultMNSQueueName;
        return sendMessageImpl(queueName, msgContext);
    }

    /**
     * 向默认队列发送批量消息
     *
     * @param jobIds 转码作业编号列表
     * @return       返回发送到队列的消息数
     */
    @Override
    public Integer batchSendMessage(List<String> jobIds) {
        // 默认指定队列
        String queueName = defaultMNSQueueName;
        return batchSendMessageImpl(queueName, jobIds);
    }


    private boolean sendMessageImpl(String queueName, String msgContent) {

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            Message msg = new Message();
            logger.info("Message Body Content: " + msgContent);
            msg.setMessageBody(msgContent);
            logger.info("Message object: " + msg.toString());
            if(msg!=null && !msg.isErrorMessage()) {
                cloudQueue.putMessage(msg);
                String msgBody = msg.getMessageBodyAsString();
                logger.debug("Insert jobId: [" + msgBody + "] to MNS queue.");
                return true;
            }
            else{
                logger.error(msg.getErrorMessageDetail()); // 获取错误消息的详细信息
                return false;
            }

        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        }
    }

    private Integer batchSendMessageImpl(String queueName, List<String> jobIds) {

        List<Message> msgList = new ArrayList<>();
        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            for(String jobId : jobIds) {
                Message msg = new Message();
                msg.setMessageBody(jobId);
                if(!msg.isErrorMessage()) {
                    msgList.add(msg);
                }
                //cloudQueue.putMessage(msg);
            }
            cloudQueue.batchPutMessage(msgList);
            return msgList.size();

        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return msgList.size();

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return msgList.size();
        }
    }


    /**
     * 默认队列接收消息
     *
     * @return 返回消息对象
     */
    @Override
    public Message receiveMessage() {
        // defaultMNSQueueName为默认配置的队列名称
        return receiveMessageImpl(defaultMNSQueueName);
    }

    /**
     * 指定队列接收消息
     *
     * @param queueName 指定传入的队列名称
     * @return          返回消息对象
     */
    @Override
    public Message receiveMessage(String queueName) {
        return receiveMessageImpl(queueName);
    }

    private Message receiveMessageImpl(String queueName) {

      try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            //Message msg = cloudQueue.peekMessage(); // 查看队列消息，不改变消息状态
            Message msg = cloudQueue.popMessage(); // 查看消息队列，消息状态变为InActive
            return msg;

        }catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
            return null;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
            return null;
      }
    }

    /**
     * 删除默认队列的指定recepitHandle消息
     *
     * @param jobId 待删除的消息句柄
     * @return      删除成功返回true，失败返回false
     */
    @Override
    public boolean deleteMessage(String jobId) {
        String queueName = defaultMNSQueueName;
        return deleteMessageImpl(queueName, jobId);
    }

    /**
     * 删除指定队列中的指定jobId
     *
     * @param queueName 队列名称
     * @param jobId     待删除的指定消息
     * @return
     */
    @Override
    public boolean deleteMessage(String queueName, String jobId) {
        return deleteMessageImpl(queueName, jobId);
    }


    @Override
    public boolean deleteMessageByRecepitHandle(String queueName, String recepitHandle) {
        if(queueName == null){
            queueName = defaultMNSQueueName;
        }
        return deleteMessageByRecepitHandleImpl(queueName, recepitHandle);
    }

    private boolean deleteMessageImpl(String queueName, String jobId) {

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            Message msg = cloudQueue.peekMessage();


            //消息不为空也不为错误消息时执行
            if(msg != null && !msg.isErrorMessage()) {

                String msgBody = msg.getMessageBodyAsString();
                if(jobId.contentEquals(msgBody)) {
                    String recepitHandle = msg.getReceiptHandle();
                    cloudQueue.deleteMessage(recepitHandle);
                    return true;
                }
                else
                    return false;
            }
            else {
                logger.error("NULL Message object: " + msg);
                return false;
            }
        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        }
    }

    private boolean deleteMessageByRecepitHandleImpl(String queueName, String recepitHandle) {
        try {

            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            cloudQueue.deleteMessage(recepitHandle);
            return true;

        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        }
    }
    /**
     * 批量删除默认队列中的jobIds
     *
     * @param jobIds 指定的jobIds列表
     * @return       批量删除的jobIds数量
     */
    @Override
    public Long batchDeleteMessage(List<String> jobIds) {
        String queueName = defaultMNSQueueName;
        return batchDeleteMessageImpl(queueName, jobIds);
    }

    /**
     * 批量删除MNS队列中的指定消息
     *
     * @param queueName 队列名称
     * @param jobIds    要删除的jobId列表
     * @return
     */
    @Override
    public Long batchDeleteMessage(String queueName, List<String> jobIds) {
        return batchDeleteMessageImpl(queueName, jobIds);
    }

    private Long batchDeleteMessageImpl(String queueName, List<String> jobIds) {

        CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
        List<String> recepitHandleList = new ArrayList<>();
        List<Message> messages = cloudQueue.batchPopMessage(jobIds.size());
        for(Message message: messages) {
            String msgBody = message.getMessageBodyAsString();
            if(jobIds.contains(msgBody)) {
                recepitHandleList.add(message.getReceiptHandle());
            }
        }
        cloudQueue.batchDeleteMessage(recepitHandleList);

        return (long)recepitHandleList.size();
    }

    /**
     * 删除默认MNS队列中的所有消息
     *
     * @return
     */
    @Override
    public Long deleteAllMessages() {
        String queueName = defaultMNSQueueName;
        return deleteAllMessagesImp(queueName);
    }

    /**
     * 删除指定MNS队列中的所有消息
     *
     * @param queueName 指定队列名称
     * @return          删除消息数
     */
    @Override
    public Long deleteAllMessages(String queueName) {
        return deleteAllMessagesImp(queueName);
    }

    private Long deleteAllMessagesImp(String queueName) {

        // 第三个参数默认值从1:1000，但填0也返回所有(SDK方法问题)
        PagingListResult<QueueMeta> result = mnsClient.listQueue(queueName, "", null);
        List<QueueMeta> queueMetaList = result.getResult();
        CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);

        long popNum = 0L;
        int deleteNum; // 删除的消息数

        int ret = queueMetaList.size();
        // 表示有相似名称的Queue不止一个
        if(ret !=0 && ret > 1) {
            for(QueueMeta queueMeta: queueMetaList) {
                if(queueMeta.getQueueName().contentEquals(queueName)) {
                    //获取活跃+非活跃消息数
                    popNum = queueMeta.getActiveMessages() + queueMeta.getInactiveMessages();
                }else {
                    logger.error("Queue name: [ " + queueName + " ] cannot be found!");
                }
            }
            deleteNum = (int)popNum;
            batchDeleteOpeartion(deleteNum, cloudQueue);

            return popNum;
        }else {
            // 只有一个队列的情况
            QueueMeta queueMeta = queueMetaList.get(0);
            popNum = queueMeta.getActiveMessages() + queueMeta.getInactiveMessages();

            deleteNum = (int)popNum;
            batchDeleteOpeartion(deleteNum, cloudQueue);

            return popNum;
        }
    }

    // 批量删除队列消息操作
    private void batchDeleteOpeartion(int deleteNum, CloudQueue cloudQueue) {
        try {
            List<String> recepitHandleList = new ArrayList<>();
            for(int index = 0; index < deleteNum; index++) {
                Message msg = cloudQueue.popMessage();
                recepitHandleList.add(msg.getReceiptHandle());
            }

            cloudQueue.batchDeleteMessage(recepitHandleList);

        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
        }
    }


    /**
     * 创建MNS队列
     *
     * @param queueMeta 队列元数据对象
     * @return          创建成功返回true，失败返回false
     */
    @Override
    public boolean createQueue(QueueMeta queueMeta) {

        try {
            mnsClient.createQueue(queueMeta); // 创建队列
            return true;

        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        }
    }

    /**
     * 删除MNS指定队列
     *
     * @param queueName 队列名称
     * @return          删除成功返回true，失败返回false
     */
    @Override
    public boolean deleteQueue(String queueName) {

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            cloudQueue.delete();

            return true;

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());

            return false;
        }
    }

    /**
     * 列出MNS中所有队列信息
     *
     * @return
     */
    @Override
    public List<QueueMeta> listQueue() {
        return mnsClient.listQueue(null, null, null).getResult();
    }

    // 查询指定名称的队列
    @Override
    public List<QueueMeta> listQueueByName(String queueName) {
        // listQueue的参数默认值，参考MNS手册
        return mnsClient.listQueue(queueName, null, null).getResult();
    }

    // 查询指定名称的单个队列
    @Override
    public QueueMeta listUniqueQueueByName(String queueName) {
        List<QueueMeta> queueMetaList = listQueueByName(queueName);
        QueueMeta queueMeta = new QueueMeta();

        if(queueMetaList.size() == 0) {
            queueMeta = null;
        }
        if(queueMetaList.size() == 1) {
            queueMeta = queueMetaList.get(0);
        }
        if(queueMetaList.size() > 1) {

            for(QueueMeta qMeta: queueMetaList) {
                String qName = qMeta.getQueueName();
                if(qName.contentEquals(queueName)) {
                    queueMeta = qMeta;
                }
            }
        }
        return queueMeta;
    }


    //[ 消费消息操作 ]----------------------------------------------------------------------------------------------------
    @Override
    public Message viewMessage() {

        String queueName = defaultMNSQueueName;
        return viewMessageImpl(queueName);
    }

    @Override
    public Message viewMessage(String queueName) {

        return viewMessageImpl(queueName);
    }

    private Message viewMessageImpl(String queueName) {

        CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
        Message message = cloudQueue.peekMessage();
        return message;
    }

    @Deprecated
    @Override
    public List<Message> batchViewMessage() {

        String queueName = defaultMNSQueueName;
        return batchViewMessageImpl(queueName);
    }

    @Deprecated
    @Override
    public List<Message> batchViewMessage(String queueName) {

        return batchViewMessageImpl(queueName);
    }

    /**
     * 该方法已过期，不建议使用
     * @param queueName
     * @return
     */
    @Deprecated
    private List<Message> batchViewMessageImpl(String queueName) {

        try {
            CloudQueue cloudQueue = mnsClient.getQueueRef(queueName);
            QueueMeta queueMeta = cloudQueue.getAttributes();

            long inactiveMsgs = queueMeta.getInactiveMessages();
            long activeMesgs =queueMeta.getActiveMessages();

            int msgCounts = (int)(inactiveMsgs + activeMesgs);
            List<Message> msgList = cloudQueue.batchPeekMessage(msgCounts);
//            List<Message> msgList = cloudQueue.batchPopMessage(msgCounts);
//            for(Message msg: msgList) {
//                String recepitHandle = msg.getReceiptHandle();
//                cloudQueue.changeMessageVisibilityTimeout(recepitHandle, 1); //将取出的消息状态立即改回Active
//                cloudQueue.changeMessageVisibilityTimeout(recepitHandle, 10);
//            }

//            for(Message message: msgList) {
//                System.out.println(message.toString());
//            }

            return msgList;

        } catch (ServiceException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());

            return null;
        } catch (ClientException e) {
            logger.error(e.getMessage());
            logger.error(e.getStackTrace().toString());

            return null;
        }
    }
}
