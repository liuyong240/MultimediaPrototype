package org.multimediaprototype.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by haihong.xiahh on 2015/11/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/front-servlet.xml",
        "classpath*:config/spring-dao-context.xml"})
@WebAppConfiguration
public class SprintJunitTest {
    @Test
    public void test() {}
}
