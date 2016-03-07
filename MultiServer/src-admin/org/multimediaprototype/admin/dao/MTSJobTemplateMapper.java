package org.multimediaprototype.admin.dao;

import org.apache.ibatis.annotations.Param;
import org.multimediaprototype.admin.model.MTSJobTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dx.yang on 15/11/16.
 */

@Repository
public interface MTSJobTemplateMapper {
    List<MTSJobTemplate> getJobTemplate(
            @Param("id") long id,
            @Param("name") String name,
            @Param("adminId") long adminId,
            @Param("using") Boolean using
    );

    Integer insertJobTemplate(
            @Param("name") String name,
            @Param("desc") String desc,
            @Param("outputs") String outputs,
            @Param("outputBucket") String outputBucket,
            @Param("outputLocation") String outputLocation,
            @Param("pipelineId") String pipelineId,
            @Param("adminId") Integer adminId,
            @Param("lastUpdate") Integer lastUpdate,
            @Param("using") Boolean using
    );

    Integer updateJobTemplate(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("desc") String desc,
            @Param("outputs") String outputs,
            @Param("outputBucket") String outputBucket,
            @Param("outputLocation") String outputLocation,
            @Param("pipelineId") String pipelineId,
            @Param("adminId") Integer adminId,
            @Param("lastUpdate") Integer lastUpdate,
            @Param("using") Boolean using
    );

    Integer deleteJobTemplate(@Param("id") Integer id);
}
