package org.quanta.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Configuration
@ConfigurationProperties(prefix = "im")
@Data
public class IMConfig {
    private int port;

    private String serverName;
}
