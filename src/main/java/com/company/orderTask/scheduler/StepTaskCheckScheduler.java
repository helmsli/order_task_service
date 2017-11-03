package com.company.orderTask.scheduler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.RestTemplate;

import com.company.orderAccess.serverManager.impl.DbOrderTaskService;
import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderDef.service.OrderDefService;
import com.company.orderTask.domain.OrderTaskInDef;
import com.company.orderTask.service.OrderTaskScheduler;

import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlowDef;
import com.xinwei.orderDb.domain.OrderFlowStepdef;

@Component("stepTaskCheckScheduler")
@EnableScheduling
public class StepTaskCheckScheduler implements InitializingBean{
	private Logger log = LoggerFactory.getLogger(getClass());
	
	//该进程调度哪种类型的订单
	@Value("${order.task.category}")
	private String orderTaskCategory;
	
	@Value("${order.task.redoNumberOnecTimes:1000}")
	private int redoNumberOnecTime;
	
	@Value("${order.task.step:all}")
	private String orderTaskStep;
	
	@Resource(name="orderTaskScheduler")
	private OrderTaskScheduler orderTaskScheduler;

	@Resource(name="redisOrderTaskService")
	private RedisOrderTaskService redisOrderTaskService;
	
	@Resource(name="dbOrderTaskService")
	private DbOrderTaskService dbOrderTaskService;
	
	@Value("${order.task.maxSchedulerThread:5}")
	private int maxSchedulerThread;
	//后续需要保证每一步的任务调度是均等的，即如果某一步的任务过多会侵占别的任务
	@Value("${order.task.initSchedulerThread:5}")
	private int initSchedulerThread;
	
	@Value("${order.task.SchedulerkeepAliveTime:300}")
	private int schedulerkeepAliveTime;
	
	@Value("${order.task.SchedulerQueneSize:300}")
	private int schedulerQueneSize;
	
	
	@Value("${order.task.runningNotifyUrl}")
	private String orderTaskRunningNotifyUrl;
	
	private boolean isInit = false;
	@Autowired
	private RestTemplate restTemplate;
	 @Autowired  
	 private ApplicationContext context;

	 
	 /**
	  * 用于cron的任务执行线程池
	  */
	 private OrderTaskPool schedulerTaskPool;
	 
	 private Map<String,CronTask> cronMap = new java.util.concurrent.ConcurrentHashMap<String,CronTask>();
	/**
	 * 检查调度任务是否运行正常
	 */
	@Scheduled (cron = "${order.task.checkScheuler.express}")
	public void checkTaskController()
	{
		try {
			log.debug("checkTaskController ");
			if(!isInit)
			{
				try {
					initStepRunInTask();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("system will exit:",e);
					//System.exit(0);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 立即执行任务
	 * @param category
	 * @param step
	 * @return
	 */
	public boolean immediateRunTask(String category,String step)
	{
		String key = this.getRunTaskkey(category, step);
		if(this.cronMap.containsKey(key))
		{
			CronTask cronTask = this.cronMap.get(key);
			cronTask.run();
			return true;
		}
		return false;
	}
	
	protected String getRunTaskkey(String category,String step)
	{
		return  category + ":" + step +":in:1";
	}
	
	protected String getRedoTaskkey(String category,String step)
	{
		return  category + ":" + step +":in:2";
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.schedulerTaskPool= new OrderTaskPool(this.initSchedulerThread,this.maxSchedulerThread,this.schedulerkeepAliveTime,this.schedulerQueneSize);
		
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
	
	protected boolean initStepRunInTask()
	{
		//获取配置文件的步骤信息，格式为，分割等多个步骤，如果填写all为全部不厚
		Map<String,String> stepMaps = this.getScheculerStep();
		if(stepMaps!=null && stepMaps.size()==0)
		{
			return false;
		}
		//将服务对象转化为数据库对象
		OrderDefService orderDefService =(OrderDefService)orderTaskScheduler;
		//获取流程定义
		OrderFlowDef orderFlowDef= orderDefService.getOrderDef(orderTaskCategory, "default");
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
			//如果步骤的runin信息不为空
			if(!StringUtils.isEmpty(orderFlowStepdef.getRunInfo()))
			{
				OrderTaskInDef orderTaskInDef = JsonUtil.fromJson(orderFlowStepdef.getTaskIn(),OrderTaskInDef.class);
				createCronTaskIn(orderFlowStepdef,orderTaskInDef);
			}
		}
		//OrderTalkInDef
		isInit = true;
		return true;
	}
	/**
	 * 创建订单的进入订单的任务
	 * @param step
	 * @param orderTaskInDef
	 */
	public void createCronTaskIn(OrderFlowStepdef orderFlowStepdef,OrderTaskInDef orderTaskInDef)
	{
		OrderTaskPool orderTaskPool=null;
		//将服务对象转化为数据库对象
		OrderDefService orderDefService =(OrderDefService)orderTaskScheduler;
				
		if(!StringUtils.isEmpty(orderTaskInDef.getRunExpress()))
		{
			String key =this.getRunTaskkey(orderFlowStepdef.getCatetory(),orderFlowStepdef.getStepId());
			
			if(!cronMap.containsKey(key))
			{
				orderTaskPool = new OrderTaskPool(orderTaskInDef.getInitThreadNumber(),orderTaskInDef.getMaxThreadNumber(),orderTaskInDef.getKeepAliveTime(),orderTaskInDef.getQueneSize());
				//创建线程任务；
				ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) context.getBean("threadPoolTaskScheduler");  
				CronTrigger trigger = new CronTrigger(orderTaskInDef.getRunExpress()); // Every 5 minutes  
				CronTask taskObject=new CronTask();
				taskObject.setOrderTaskPool(orderTaskPool);
				taskObject.setOrderTaskInDef(orderTaskInDef);
				taskObject.setRedisOrderTaskService(redisOrderTaskService);
				taskObject.setRedoNumberOnecTime(redoNumberOnecTime);
				taskObject.setSchedulerThreadPool(this.schedulerTaskPool);
				taskObject.setOrderFlowStepdef(orderFlowStepdef);
				taskObject.setTaskImmediateNotifyUrl(orderTaskRunningNotifyUrl);
				taskObject.setDbOrderTaskService(dbOrderTaskService);
				taskObject.setOrderDefService(orderDefService);
				taskObject.setRestTemplate(restTemplate);
				scheduler.schedule(taskObject, trigger);
				cronMap.put(key, taskObject);
			}
		}
		
		if(!StringUtils.isEmpty(orderTaskInDef.getRetryExpress()))
		{
			String key =this.getRedoTaskkey(orderFlowStepdef.getCatetory(),orderFlowStepdef.getStepId());
			if(!cronMap.containsKey(key))
			{
				if(orderTaskPool==null)
				{
					orderTaskPool = new OrderTaskPool(orderTaskInDef.getInitThreadNumber(),orderTaskInDef.getMaxThreadNumber(),orderTaskInDef.getKeepAliveTime(),orderTaskInDef.getQueneSize());
				}
				ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) context.getBean("threadPoolTaskScheduler");  
				CronTrigger trigger = new CronTrigger(orderTaskInDef.getRetryExpress()); // Every 5 minutes  
				CronTask taskObject=new CronRetryTalk();
				taskObject.setOrderTaskPool(orderTaskPool);
				taskObject.setOrderTaskInDef(orderTaskInDef);
				taskObject.setRedisOrderTaskService(redisOrderTaskService);
				taskObject.setRedoNumberOnecTime(redoNumberOnecTime);
				taskObject.setSchedulerThreadPool(this.schedulerTaskPool);
				taskObject.setOrderFlowStepdef(orderFlowStepdef);
				taskObject.setDbOrderTaskService(dbOrderTaskService);
				taskObject.setOrderDefService(orderDefService);
				taskObject.setRestTemplate(restTemplate);
				taskObject.setTaskImmediateNotifyUrl(orderTaskRunningNotifyUrl);
				scheduler.schedule(taskObject, trigger);
				cronMap.put(key, taskObject);
			}
		}
		
			
			  
	}
	
	
	
}
