package com.company.orderTask.service.impl;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.orderDef.service.OrderDefService;
import com.company.orderTask.domain.OrderTaskRunInfo;
import com.company.orderTask.service.OrderTaskScheduler;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderFlowDef;
import com.xinwei.orderDb.domain.OrderFlowStepdef;
@Service("orderTaskScheduler")
public class OrderTaskSchedulerImpl extends OrderDefService implements OrderTaskScheduler,InitializingBean {
	
	@Value("${order.defDbUrl}")  
	private String orderDefDbUrl;
	
	@Override
	public ProcessResult receiveTaskNotify(OrderTaskRunInfo orderTaskInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		super.setHttpOrderDbDefUrl(orderDefDbUrl);
	}

}
