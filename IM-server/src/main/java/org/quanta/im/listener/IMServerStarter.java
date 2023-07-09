package org.quanta.im.listener;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.quanta.im.server.IMServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Log4j2
public class IMServerStarter implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    IMServer imServer;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            try {
                imServer.run();
            } catch (Exception e) {
                log.error("netty服务启动异常，异常信息为:{}",e.getMessage());
            }
        }
    }
}