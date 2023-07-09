package org.quanta.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/2
 */
@EnableAsync
@MapperScan("org.quanta.im.mapper")
@SpringBootApplication
public class IMApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class, args);
    }
}
