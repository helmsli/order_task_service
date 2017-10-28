package com.company.orderTask.service;

import com.company.orderTask.domain.OrderTaskRunInfo;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface OrderTaskScheduler {
	/**
	 * 接收任务调度通知
	 * @param orderTaskInfo
	 * @return
	 */
	public ProcessResult receiveTaskNotify(OrderTaskRunInfo orderTaskInfo);
}
