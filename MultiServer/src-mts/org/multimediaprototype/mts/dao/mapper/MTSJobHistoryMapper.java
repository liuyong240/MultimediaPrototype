package org.multimediaprototype.mts.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.multimediaprototype.mts.dao.model.MTSJobHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTSJobHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    int insert(MTSJobHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    int insertSelective(MTSJobHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    MTSJobHistory selectByPrimaryKey(Long id);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    int updateByPrimaryKeySelective(MTSJobHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mts_job_history
     *
     * @mbggenerated Thu Nov 26 17:14:00 CST 2015
     */
    int updateByPrimaryKey(MTSJobHistory record);

    int updateByJobIdSelective(MTSJobHistory record);

    MTSJobHistory selectByJobId(@Param("jobId") String jobId);

    List<MTSJobHistory> selectByPipelineId(@Param(value = "pipelineId") String pipelineId,
                                           @Param(value = "start") Integer start,
                                           @Param(value = "offset") Integer offset);

    List<MTSJobHistory> selectByOffset(@Param(value = "start") Integer start,
                                       @Param(value = "offset") Integer offset);


    List<MTSJobHistory> selectByUserId(@Param(value = "userId") Long userId,
                                       @Param(value = "start") Integer start,
                                       @Param(value = "offset") Integer offset);

}