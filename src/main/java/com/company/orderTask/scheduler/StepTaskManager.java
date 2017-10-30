package com.company.orderTask.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
@Service("stepTalkManager")
public class StepTaskManager {
	/**
	 * 保存任务步骤的Task
	 */
	private Map<String,OrderTaskPool> taskPoolMap = new ConcurrentHashMap<String,OrderTaskPool>();
	
	
	protected String getTaskRunPoolKey(String category,String step)
	{
		return "taskPool:" + category + ":" + step;
	}
	
	
	public synchronized boolean  createTalkRunPool(String category,String step,OrderTaskPool orderTaskPool)
	{
		
		String key = getTaskRunPoolKey(category,step);
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
	
	public  OrderTaskPool  getTalkRunPool(String category,String step)
	{
		
		String key = getTaskRunPoolKey(category,step);
		if(taskPoolMap.containsKey(key))
		{
			return taskPoolMap.get(key);
		}
		return null;
	}
	
	
	protected String getTaskRedoPoolKey(String category,String step)
	{
		return "taskPoolRedo:" + category + ":" + step;
	}
	
	
	public synchronized boolean  createTalkRedoPool(String category,String step,OrderTaskPool orderTaskPool)
	{
		
		String key = getTaskRedoPoolKey(category,step);
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
	
	public  OrderTaskPool  getTalkRedoPool(String category,String step)
	{
		
		String key = getTaskRedoPoolKey(category,step);
		if(taskPoolMap.containsKey(key))
		{
			return taskPoolMap.get(key);
		}
		return null;
	}
	
	/**
	 * 获取任务调度的线程池
	 * @param category
	 * @param step
	 * @return
	 */
	protected String getTaskSchedulerPoolKey(String category,String step)
	{
		return "taskPoolSche:" + category + ":" + step;
	}
	
	
	public synchronized boolean  createTalkSchedulerPool(String category,String step,OrderTaskPool orderTaskPool)
	{
		
		String key = getTaskSchedulerPoolKey(category,step);
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
	
	public  OrderTaskPool  getTalkSchedulerPool(String category,String step)
	{
		
		String key = getTaskSchedulerPoolKey(category,step);
		if(taskPoolMap.containsKey(key))
		{
			return taskPoolMap.get(key);
		}
		return null;
	}
}
