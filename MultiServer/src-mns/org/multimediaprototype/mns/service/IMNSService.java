package org.multimediaprototype.mns.service;

import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;

import java.util.List;

/**
 * Created by haihong.xiahh on 2015/12/1.
 */
public interface IMNSService {

    //[ 发送消息操作 ]----------------------------------------------------------------------------------------------------
    //将jobId写入queue
    boolean sendMessage(String jobId);

    //将多个jobId批量写入queue
    Integer batchSendMessage(List<String> jobIds);

    //[ 消费消息操作 ]----------------------------------------------------------------------------------------------------
    //从queue里获取消息 TODO batchReceive
    Message receiveMessage(String queueName);

    //从queue里获取消息 TODO batchReceive
    Message receiveMessage();

    //查看默认queue中的单个消息，不改变消息状态
    Message viewMessage();

    //查看指定队列中的单个消息，不改变消息状态
    Message viewMessage(String queueName);

    List<Message> batchViewMessage();

    List<Message> batchViewMessage(String queueName);
    //消费queue中的消息，改变消息状态，不删除

    //[ 删除消息操作 ]----------------------------------------------------------------------------------------------------
    //从queue里删除消息
    boolean deleteMessage(String jobId);

    //从指定名称的queue中删除指定消息
    boolean deleteMessage(String queueName, String jobId);

    Long batchDeleteMessage(List<String> jobIds);

    //从queue里批量删除消息
    Long batchDeleteMessage(String queueName, List<String> jobIds);

    //从队列中删除所有消息
    Long deleteAllMessages();

    //删除指定队列中的所有消息
    Long deleteAllMessages(String queueName);

    boolean deleteMessageByRecepitHandle(String queueName, String recepitHandle);

    //[ 队列操作 ]-------------------------------------------------------------------------------------------------------
    // 根据QueueMeta对象创建MNS队列
    boolean createQueue(QueueMeta queueMeta);

    // 删除MNS中指定名称的队列
    boolean deleteQueue(String queueName);

    // 列出用户的MNS队列
    List<QueueMeta> listQueue();

    // 根据队列名称获取队列信息
    List<QueueMeta> listQueueByName(String queueName);

    // 根据队列名查询单个队列信息
    QueueMeta listUniqueQueueByName(String queueName);
}
