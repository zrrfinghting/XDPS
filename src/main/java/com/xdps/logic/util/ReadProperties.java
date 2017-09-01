package com.xdps.logic.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 读取properties文件
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/30
 */
public class ReadProperties {
    /**
     * 根据传入的文件名读取配置文件信息
     *
     * @param string
     * @return
     */
    public static Properties getPropes(String string) {
        Properties propes;
        try {
            Resource resource = new ClassPathResource(string);
            propes = PropertiesLoaderUtils.loadProperties(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return propes;
    }
}
