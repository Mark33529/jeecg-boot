package org.jeecg.modules.generator.entity;

import org.jeecg.modules.generator.entity.Code;
import org.jeecg.modules.generator.entity.Template;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: online配置
 * @Author: cj
 * @Date:	2019-06-23
 * @Version:V1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "online")
public class OnlineProperties {

    private String template1;

    private String template2;

    private String template3;

    private Template template;

    private Code code;

}
