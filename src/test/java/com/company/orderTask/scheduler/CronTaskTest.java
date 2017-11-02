package com.company.orderTask.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.orderTask.domain.OrderTaskInDef;
import com.xinwei.nnl.common.util.JsonUtil;

import ch.qos.logback.core.Context;

@RunWith(SpringRunner.class)
@SpringBootTest

public class CronTaskTest {
	@Autowired  
	 private ApplicationContext context;
	
@Test
public void test()
{
	OrderTaskInDef orderTaskInfo = new OrderTaskInDef();
	orderTaskInfo.setCategory(0);
	orderTaskInfo.setInitThreadNumber(0);
	orderTaskInfo.setKeepAliveTime(100);
	orderTaskInfo.setMaxThreadNumber(100);
	orderTaskInfo.setName("abc");
	orderTaskInfo.setQueneSize(122);
	orderTaskInfo.setRestMethod("abc");
	orderTaskInfo.setRetryExpress("aaaa");
	orderTaskInfo.setUrl("aaa");
	orderTaskInfo.setRunExpress("ddd");
	System.out.println(JsonUtil.toJson(orderTaskInfo));
	
	try {
		
		Thread.sleep(2000000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
	String[] beanDefinitionNames = context.getBeanDefinitionNames();
	for (String beanName : beanDefinitionNames)
	{
		System.out.println(beanName);
		if(beanName.contains("schedu")|| beanName.contains("chedu"))
		{
			System.out.println("**********************************aaaaa**************************************");;
			//Object object = beanFactory.getBean(beanName);
			
		}
	}
	
	
//	ThreadPoolTaskScheduler threadPoolTaskScheduler = context.getBean(ThreadPoolTaskScheduler.class);
	context.getBean("schedulerFactory"); 
	
	context.getBean("schedulerFactory"); 
	
	context.getBean("schedulerFactory"); 
	
	context.getBean("schedulerFactory"); 
	
}
}
