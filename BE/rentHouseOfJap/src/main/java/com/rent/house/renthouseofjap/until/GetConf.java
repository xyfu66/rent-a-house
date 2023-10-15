package com.rent.house.renthouseofjap.until;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("json.file")
public class GetConf {
    //正常读取读入文件位置
    private String path;
    //模板位置
    private String template;
    //具体文件名
    private formDetail detail = new formDetail();

    @Data
    @Configuration
    @ConfigurationProperties("json.file.detail")
    public class formDetail {
        private String json;
        private String data;
        private String Info;
    }
}
