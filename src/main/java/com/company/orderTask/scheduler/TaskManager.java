package com.company.orderTask.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {
	/**
	 * 保存任务步骤的Task
	 */
	private Map<String,OrderTaskPool> taskPoolMap = new ConcurrentHashMap<String,OrderTaskPool>();
	
	
	protected String getPoolKey(String category,String step)
	{
		return "taskPool:" + category + ":" + step;
	}
	
	public synchronized boolean  createNewPool(String category,String step,OrderTaskPool orderTaskPool)
	{
		
		String key = getPoolKey(category,step);
		if(taskPoolMap.containsKey(key))
		{
			return false;
		}
		else
		{
			taskPoolMap.put(key, orderTaskPool);
		}
		return false;
	}
	
	public  OrderTaskPool  getNewPool(String category,String step)
	{
		
		String key = getPoolKey(category,step);
		if(taskPoolMap.containsKey(key))
		{
			return taskPoolMap.get(key);
		}
		return null;
	}
	
}
