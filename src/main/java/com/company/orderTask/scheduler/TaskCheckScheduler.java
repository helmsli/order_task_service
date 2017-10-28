package com.company.orderTask.scheduler;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderDef.service.OrderDefService;
import com.company.orderTask.domain.OrderTaskInDef;
import com.company.orderTask.service.OrderTaskScheduler;

import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlowDef;
import com.xinwei.orderDb.domain.OrderFlowStepdef;

@Component
@EnableScheduling
public class TaskCheckScheduler implements InitializingBean{
	
	@Value("${order.task.category}")
	private String orderTaskCategory;
	
	@Value("${order.task.schedulerNumber}")
	private int schedulerNumber;
	
	@Value("${order.task.step:all}")
	private String orderTaskStep;
	
	@Resource(name="orderTaskScheduler")
	private OrderTaskScheduler orderTaskScheduler;

	@Resource(name="redisOrderTaskService")
	private RedisOrderTaskService redisOrderTaskService;
	
	private boolean isInit = false;
	
	 @Autowired  
	 private ApplicationContext context;

	 private Map<String,String> cronMap = new java.util.concurrent.ConcurrentHashMap<String,String>();
	/**
	 * 检查调度任务是否运行正常
	 */
	@Scheduled (cron = "${order.task.checkScheuler.express}")
	public void checkTaskController()
	{
		try {
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	protected Map<String,String> getScheculerStep()
	{
		if(this.orderTaskStep.compareToIgnoreCase("all")==0)
		{
			return null;
		}
		String[] steps = StringUtils.split(this.orderTaskStep, ",");
		Map<String,String> stepMap = new HashMap<String,String>();
		for(int i=0;i<steps.length;i++)
		{
			stepMap.put(steps[i], steps[i]);
		}
		return stepMap;
	}
	
	protected boolean init()
	{
		Map<String,String> stepMaps = this.getScheculerStep();
		if(stepMaps.size()==0)
		{
			return false;
		}
		OrderDefService orderDefService =(OrderDefService)orderTaskScheduler;
		OrderFlowDef orderFlowDef= orderDefService.getOrderDef(orderTaskCategory, "");
		//获取该订单的多有步骤信息
		List<OrderFlowStepdef> orderStepList = orderDefService.getOrderStepDef(orderTaskCategory,"");
		for(int i=0;i<orderStepList.size();i++)
		{
			
			OrderFlowStepdef orderFlowStepdef= orderStepList.get(i);
			//如果不是所有步骤
			if(stepMaps!=null)
			{
				//如果不包含这个步骤
				if(!stepMaps.containsKey(orderFlowStepdef.getStepId()))
				{
					continue;
				}
			}
			if(!StringUtils.isEmpty(orderFlowStepdef.getRunInfo()))
			{
				OrderTaskInDef orderTaskInDef = JsonUtil.fromJson(orderFlowStepdef.getRunInfo(),OrderTaskInDef.class);
				
			}
		}
		//OrderTalkInDef
		isInit = true;
		return true;
	}
	
	public void createCronTaskIn(String step,OrderTaskInDef orderTaskInDef)
	{
		
		if(!StringUtils.isEmpty(orderTaskInDef.getRunExpress()))
		{
			if(!cronMap.containsKey(step+"in:1"))
			{
				ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) context.getBean("scheduler");  
				CronTrigger trigger = new CronTrigger(orderTaskInDef.getRunExpress()); // Every 5 minutes  
				Runnable taskObject=new CronTask(this.redisOrderTaskService,orderTaskInDef,schedulerNumber);
				scheduler.schedule(taskObject, trigger);
				cronMap.put(step+"in:1", step);
			}
		}
		
		if(!StringUtils.isEmpty(orderTaskInDef.getRetryExpress()))
		{
			if(!cronMap.containsKey(step+"in:2"))
			{
				ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) context.getBean("scheduler");  
				CronTrigger trigger = new CronTrigger(orderTaskInDef.getRetryExpress()); // Every 5 minutes  
				Runnable taskObject=new CronTask(this.redisOrderTaskService,orderTaskInDef,schedulerNumber);
				scheduler.schedule(taskObject, trigger);
				cronMap.put(step+"in:1", step);
			}
		}
		
			
			  
	}
	
	
	
}
