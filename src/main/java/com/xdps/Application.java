package com.xdps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 程序入口类
 *
 * @autherAdmin Deram Zhao
 * @creat 2017/8/24
 */
@SpringBootApplication()
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@ServletComponentScan
public class Application {
    public static void main(String []args){
        SpringApplication.run(Application.class,args);
    }
}
