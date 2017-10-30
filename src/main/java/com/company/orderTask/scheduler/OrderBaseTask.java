package com.company.orderTask.scheduler;

import com.company.orderAccess.serverManager.impl.DbOrderTaskService;
import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderDef.service.OrderDefService;
import com.company.orderTask.domain.OrderTaskInDef;
import com.xinwei.orderDb.domain.OrderFlowStepdef;

public class OrderBaseTask implements Runnable {
	protected RedisOrderTaskService redisOrderTaskService;
	protected OrderTaskInDef orderTaskInDef;
	protected int schedulerNumber=10;
	protected OrderTaskPool schedulerThreadPool;
	protected OrderTaskPool orderTaskPool;
	protected OrderFlowStepdef orderFlowStepdef;
	
	protected OrderDefService orderDefService;
	protected DbOrderTaskService dbOrderTaskService;
	
	public void setOrderBaseTalk(OrderBaseTask srcOrderTask)
	{
		this.redisOrderTaskService = srcOrderTask.redisOrderTaskService;
		
		this.orderTaskInDef=srcOrderTask.orderTaskInDef;
		this.schedulerNumber=srcOrderTask.schedulerNumber;
		this.schedulerThreadPool=srcOrderTask.schedulerThreadPool;
		this.orderTaskPool=srcOrderTask.orderTaskPool;
		this.orderFlowStepdef=srcOrderTask.orderFlowStepdef;
		this.orderDefService=srcOrderTask.orderDefService;
		this.dbOrderTaskService = dbOrderTaskService;
	}
	
	public RedisOrderTaskService getRedisOrderTaskService() {
		return redisOrderTaskService;
	}



	public void setRedisOrderTaskService(RedisOrderTaskService redisOrderTaskService) {
		this.redisOrderTaskService = redisOrderTaskService;
	}



	public OrderTaskInDef getOrderTaskInDef() {
		return orderTaskInDef;
	}



	public void setOrderTaskInDef(OrderTaskInDef orderTaskInDef) {
		this.orderTaskInDef = orderTaskInDef;
	}



	public int getSchedulerNumber() {
		return schedulerNumber;
	}



	public void setSchedulerNumber(int schedulerNumber) {
		this.schedulerNumber = schedulerNumber;
	}



	public OrderTaskPool getSchedulerThreadPool() {
		return schedulerThreadPool;
	}



	public void setSchedulerThreadPool(OrderTaskPool schedulerThreadPool) {
		this.schedulerThreadPool = schedulerThreadPool;
	}



	public OrderTaskPool getOrderTaskPool() {
		return orderTaskPool;
	}



	public void setOrderTaskPool(OrderTaskPool orderTaskPool) {
		this.orderTaskPool = orderTaskPool;
	}



	public OrderFlowStepdef getOrderFlowStepdef() {
		return orderFlowStepdef;
	}



	public void setOrderFlowStepdef(OrderFlowStepdef orderFlowStepdef) {
		this.orderFlowStepdef = orderFlowStepdef;
	}



	public OrderDefService getOrderDefService() {
		return orderDefService;
	}

	public void setOrderDefService(OrderDefService orderDefService) {
		this.orderDefService = orderDefService;
	}

	public DbOrderTaskService getDbOrderTaskService() {
		return dbOrderTaskService;
	}

	public void setDbOrderTaskService(DbOrderTaskService dbOrderTaskService) {
		this.dbOrderTaskService = dbOrderTaskService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
     
}
