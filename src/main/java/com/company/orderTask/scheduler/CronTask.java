package com.company.orderTask.scheduler;

import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderTask.domain.OrderTaskInDef;

public class CronTask implements Runnable {
	private RedisOrderTaskService redisOrderTaskService;
	private OrderTaskInDef orderTaskInDef;
	private int schedulerNumber;
	
	public CronTask(RedisOrderTaskService redisOrderTaskService,OrderTaskInDef orderTaskInDef,int schedulerNumber)
	{
		this.redisOrderTaskService=redisOrderTaskService;
		this.orderTaskInDef=orderTaskInDef;
		this.schedulerNumber = schedulerNumber;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
