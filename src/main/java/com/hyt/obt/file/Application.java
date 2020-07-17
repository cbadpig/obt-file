package com.hyt.obt.file;

import com.hyt.obt.file.entity.MonitorYml;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 * @author liuq
 *
 */
@EnableCaching
@EnableAsync
@ServletComponentScan //开启自动注解
@MapperScan(basePackages = {"com.hyt.obt.file.mapper"})
@ComponentScan(basePackages = {"com.hyt.obt"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({MonitorYml.class})
public class Application extends SpringBootServletInitializer {
	
	public Application() {    
		super();   
		setRegisterErrorPageFilter(false);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    return builder.sources(Application.class);
	}
	
	/**
	 * main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
    
}
