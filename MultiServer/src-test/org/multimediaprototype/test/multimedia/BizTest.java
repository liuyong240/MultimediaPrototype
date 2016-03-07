package org.multimediaprototype.test.multimedia;

import com.aliyun.oss.model.Bucket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.multimediaprototype.mts.service.ITranscodeService;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.multimediaprototype.oss.service.IOSSService;
import org.multimediaprototype.test.SprintJunitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by haihong.xiahh on 2015/11/26.
 */
public class BizTest extends SprintJunitTest {
    @Autowired
    private IOSSService ossService;
    @Autowired
    private ITranscodeService transcodeService;

    @Test
    public void testUploadFileFromUser() {

        List<Bucket> list = ossService.listBucket();

        Assert.assertNotEquals(list, null);

    }

}
