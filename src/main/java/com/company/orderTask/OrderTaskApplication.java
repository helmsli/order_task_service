package com.company.orderTask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
//@EnableRedisHttpSession
//@EnableRedisHttpSession 
@ComponentScan ("com.company.security")
@MapperScan ("com.company.security.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
@Import({RedisOrderTaskService.class})
public class OrderTaskApplication {
    public static  ApplicationContext  app = null;
	public static void main(String[] args) {
		app = SpringApplication.run(OrderTaskApplication.class, args);
		
		try {
			
		}
		catch(Throwable e)
		{
		   e.printStackTrace();	
		}
	}
}
