package com.company.orderTask.scheduler;

import org.springframework.util.StringUtils;

import com.company.orderAccess.serverManager.impl.RedisOrderTaskService;
import com.company.orderTask.OrderTaskConst;
import com.company.orderTask.domain.OrderTaskInDef;
import com.company.orderTask.domain.OrderTaskRunInfo;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderFlowStepdef;
import com.xinwei.orderDb.domain.OrderMain;

public class OrderInTask extends OrderBaseTask {
	private OrderTaskRunInfo orderTaskRunInfo;
	public OrderInTask(OrderBaseTask orderBaseTask)
	{
		super.setOrderBaseTalk(orderBaseTask);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/**
		 * 1.从redis获取主订单的状态，判断状态是否和当前状态一致。
		 * 2.判断当前的业务是否已经过期
		 * 3.如果没有过期，获取任务的地址，调用地址，根据返回数值，跳转到新的状态，
		 * 4.更新redis和数据库，进入新的业务状态调度；
		 */
		if(StringUtils.isEmpty(this.orderTaskInDef.getUrl())||
				orderTaskInDef.getCategory()==	orderTaskInDef.catogory_manual)
				
		{
			//输出错误日志
			return;
		}
		//如果已经过期
		if(System.currentTimeMillis()>orderTaskRunInfo.getExpireTime())
		{
			return ;
		}
		
		ProcessResult processResult = this.dbOrderTaskService.getOrderMain(this.orderTaskRunInfo.getCatetory(),this.orderTaskRunInfo.getOrderId());
		
		if(processResult.getRetCode()!=OrderTaskConst.RESULT_Success)
		{
			return;
		}
		OrderMain orderMain = (OrderMain)processResult.getResponseInfo();
		if(orderMain.getCurrentStatus()!=orderTaskRunInfo.getCurrentStatus() ||
				orderMain.getCurrentStep()!=orderTaskRunInfo.getCurrentStep()||
				orderMain.getFlowId()!=orderTaskRunInfo.getFlowId())
		{
			//状态不正确；
			return;
		}
		String url = this.orderTaskInDef.getUrl();
		//注意需要确定需要多少个http连接请求，这个地方需要特别注意
		//调用redis：
		//
		/**
		 * 定义执行完任务失败后根据返回数值控制状态跳转，这个字段需要填写表格，以；分割行，以，分割列
格式如下：
错误码支持区间配置；支持离散的；
类型(0-单值，1-区间，2-离散数值)返回数值错误码,步骤,是否通知运维(0--不通知，其余通知）;
		 */
	}

}
