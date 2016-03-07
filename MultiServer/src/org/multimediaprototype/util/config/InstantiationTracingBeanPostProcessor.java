package org.multimediaprototype.util.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.context.SecurityContextHolder;

public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LogManager.getLogger(InstantiationTracingBeanPostProcessor.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      
    	SecurityContextHolder.setStrategyName("MODE_INHERITABLETHREADLOCAL");
    	logger.debug("onApplicationEvent...");
 }
}