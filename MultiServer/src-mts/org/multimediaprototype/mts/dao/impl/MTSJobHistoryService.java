package org.multimediaprototype.mts.dao.impl;

import org.multimediaprototype.mts.dao.IMTSJobHistoryService;
import org.multimediaprototype.mts.dao.mapper.MTSJobHistoryMapper;
import org.multimediaprototype.mts.dao.model.MTSJobHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by haihong.xiahh on 2015/11/23.
 */
@Service
public class MTSJobHistoryService implements IMTSJobHistoryService {
    @Autowired
    private MTSJobHistoryMapper mtsJobHistoryMapper;

    @Override
    public int insert(MTSJobHistory mtsJob) {
        return mtsJobHistoryMapper.insertSelective(mtsJob);
    }

    @Override
    public int updateStatus(String jobId, String status) {
        MTSJobHistory job = new MTSJobHistory();
        job.setJobId(jobId);
        job.setStatus(status);
        job.setGmtModified(new Date());
        return mtsJobHistoryMapper.updateByJobIdSelective(job);
    }

    @Override
    public int updateNotifiedStatus(String jobId, String status) {
        MTSJobHistory job = new MTSJobHistory();
        job.setJobId(jobId);
        job.setStatusNotified(status);
        job.setGmtModified(new Date());
        return mtsJobHistoryMapper.updateByJobIdSelective(job);
    }

    @Override
    public int delete(Long id) {
        // logic delete
        MTSJobHistory job = new MTSJobHistory();
        job.setId(id);
        job.setDeleted(1);
        job.setGmtModified(new Date());
        return mtsJobHistoryMapper.updateByPrimaryKeySelective(job);
    }


    @Override
    public MTSJobHistory getJobHistory(Long id) {
        return mtsJobHistoryMapper.selectByPrimaryKey(id);
    }
    @Override
    public MTSJobHistory getJobHistory(String jobId) {
        return mtsJobHistoryMapper.selectByJobId(jobId);
    }

    @Override
    public List<MTSJobHistory> getJobHistoryListByPipelineId(String pipelineId, Integer pageNumber, Integer pageSize) {
        pageNumber = getValidPageNumber(pageNumber);
        pageSize = getValidPageSize(pageSize);
        Integer start = (pageNumber - 1) * pageSize + 1;
        return mtsJobHistoryMapper.selectByPipelineId(pipelineId, start, pageSize);
    }

    @Override
    public List<MTSJobHistory> getJobHistoryList(Integer pageNumber, Integer pageSize) {
        pageNumber = getValidPageNumber(pageNumber);
        pageSize = getValidPageSize(pageSize);
        Integer start = (pageNumber - 1) * pageSize + 1;
        return mtsJobHistoryMapper.selectByOffset(start, pageSize);
    }

    @Override
    public List<MTSJobHistory> getJobHistoryListByUserId(Long userId, Integer pageNumber, Integer pageSize) {
        pageNumber = getValidPageNumber(pageNumber);
        pageSize = getValidPageSize(pageSize);
        Integer start = (pageNumber - 1) * pageSize + 1;
        return mtsJobHistoryMapper.selectByUserId(userId, start, pageSize);
    }


    private Integer getValidPageNumber(Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        return pageNumber;
    }

    private Integer getValidPageSize(Integer pageSize) {
        if (pageSize == null || pageSize > 100 || pageSize < 10) {
            pageSize = 10;
        }
        return pageSize;
    }

    /**
     * 根据jobId获取数据库中记录
     *
     * @param jobIds JobId列表
     * @return       对应JobId的数据库作业列表
     */
    @Override
    public List<MTSJobHistory> getJobHistoryListByJobIds(List<String> jobIds) {

        return null;
    }

}
