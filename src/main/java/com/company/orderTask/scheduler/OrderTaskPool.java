package com.company.orderTask.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

public class OrderTaskPool {
	
	BlockingQueue<Runnable> workQueue =null;
	
	private ThreadPoolExecutor notifyTaskPool = null;
	
	private int threadPoolQueneSize;
	
	private int maxThreadSize;
	
	/**
	 * 返回工作线程空闲队列个数
	 * @return
	 */
	public int getWorkQueueFreeSize() {
		return threadPoolQueneSize - workQueue.size();
	}
	
	
	
	public int getThreadPoolQueneSize() {
		return threadPoolQueneSize;
	}



	


	public int getMaxThreadSize() {
		return maxThreadSize;
	}
	
	public OrderTaskPool(int threadPoolInitSize,int threadPoolMaxSize,int threadPoolKeepAliveTime,int threadPoolQueneSize)
	{
		this.threadPoolQueneSize=threadPoolQueneSize;
		maxThreadSize=threadPoolMaxSize;
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
