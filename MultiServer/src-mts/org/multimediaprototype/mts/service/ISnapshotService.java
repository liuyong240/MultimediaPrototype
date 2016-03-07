package org.multimediaprototype.mts.service;

import com.aliyun.api.domain.SnapshotJob;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhuowu.zm on 2015/11/18.
 */
public interface ISnapshotService {

    // 提交截图作业
    SnapshotJob submitSnapshotJob(String input, String snapshotConfig, String userData);

    // 查询截图作业
    HashMap querySnapshotJobList(List<String> snapshotJobIds);
}
