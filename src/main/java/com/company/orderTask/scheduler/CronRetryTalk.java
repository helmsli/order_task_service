package com.company.orderTask.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.company.orderTask.domain.OrderTaskRunInfo;

public class CronRetryTalk extends CronTask {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//从任务调度中获取
		try {
			//log.debug("sehcduler cron task:"  + this.orderFlowStepdef.getCatetory());
			RedoOrderTask redoOrderTask = new RedoOrderTask(this);
			if(this.schedulerThreadPool.getWorkQueueFreeSize()>0)
			{
				this.schedulerThreadPool.executeTask(redoOrderTask);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class RedoOrderTask extends OrderBaseTask
	{
		public RedoOrderTask(OrderBaseTask orderBaseTask)
		{
			super.setOrderBaseTalk(orderBaseTask);
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				int retryTimes = 3;
				if(!StringUtils.isEmpty(this.orderFlowStepdef.getRetryTimes().trim()))
				{
					retryTimes = Integer.parseInt(orderFlowStepdef.getRetryTimes().trim());
					if(retryTimes<=0)
					{
						retryTimes=3;
					}
				}
				int timeoutMills = orderTaskInDef.getTimeoutMills();
				if(timeoutMills<=0)
				{
					timeoutMills = 30000;
				}
				if(redoNumberOnecTime<=0)
				{
					redoNumberOnecTime = 1000;
				}
				this.redisOrderTaskService.redoTimeoutTask(orderFlowStepdef.getCatetory(), orderFlowStepdef.getStepId(), this.redoNumberOnecTime, timeoutMills, retryTimes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
