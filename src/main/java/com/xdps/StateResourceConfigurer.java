package com.xdps;

import com.xdps.logic.util.ReadProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Properties;

/**
 * 自定义配置类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/6/20
 */
@Configuration
public class StateResourceConfigurer extends WebMvcConfigurerAdapter {
    /**
     * 配置访问静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //读取静态资源存放路径
        Properties propers = ReadProperties.getPropes("/application.properties");
        String uploadFilePath = propers.getProperty("uploadFilePath");
        String folder = propers.getProperty("folder");//文件夹名称
        //registry.addResourceHandler("/bookpicture/**").addResourceLocations("file:C:/bookpicture/");  file:C:/bookpicture/
        registry.addResourceHandler("/" + folder + "/**").addResourceLocations(uploadFilePath);
        super.addResourceHandlers(registry);
    }
}
