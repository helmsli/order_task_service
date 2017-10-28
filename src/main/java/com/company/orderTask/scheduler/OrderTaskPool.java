package com.company.orderTask.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

public class OrderTaskPool {
	
	BlockingQueue<Runnable> workQueue =null;
	
	private ThreadPoolExecutor notifyTaskPool = null;
	
	
	public OrderTaskPool(int threadPoolMaxSize,int threadPoolInitSize,int threadPoolKeepAliveTime,int threadPoolQueneSize)
	{
		workQueue =new ArrayBlockingQueue<Runnable>(threadPoolQueneSize);
		notifyTaskPool = new ThreadPoolExecutor(threadPoolInitSize, threadPoolMaxSize, threadPoolKeepAliveTime, TimeUnit.SECONDS,workQueue);
	
	}
	/**
	 * 执行订单任务
	 * @param runable
	 */
	public void executeTask(Runnable runable)
	{
		try {
			notifyTaskPool.execute(runable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
