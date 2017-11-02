package com.company.orderTask.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderTask.domain.OrderTaskInDef;
import com.company.orderTask.domain.OrderTaskRunInfo;
import com.xinwei.orderDb.domain.OrderFlowStepdef;

public class CronTask extends OrderBaseTask {
	private Logger log = LoggerFactory.getLogger(getClass());
	 
	public CronTask()
	{
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//从任务调度中获取
		try {
			//log.debug("sehcduler cron task:"  + this.orderFlowStepdef.getCatetory());
			GetSchedulerRunnable getSchedulerRunnable = new GetSchedulerRunnable(this);
			this.schedulerThreadPool.executeTask(getSchedulerRunnable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class GetSchedulerRunnable extends OrderBaseTask
	{
		public GetSchedulerRunnable(OrderBaseTask orderBaseTask)
		{
			super.setOrderBaseTalk(orderBaseTask);
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				int numbers = orderTaskPool.getWorkQueueFreeSize();
				List<OrderTaskRunInfo> lists = this.redisOrderTaskService.getSchedulerOrderTasks(orderFlowStepdef.getCatetory(),orderFlowStepdef.getStepId(), numbers);
				if(lists!=null)
				{
					for(int i=0;i<lists.size();i++)
					{
						log.debug("sehcduler cron task:"  + lists.get(i));
						
						OrderInTask orderInTask = new OrderInTask(this);
						orderInTask.setOrderTaskRunInfo(lists.get(i));
						this.orderTaskPool.executeTask(orderInTask);
						
					}
				}
				if(numbers<(orderTaskPool.getMaxThreadSize()/2))
				{
					//notify 运维to check
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
