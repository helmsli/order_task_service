package com.company.orderTask.controller.rest;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.orderAccess.Const.OrderAccessConst;
import com.company.orderTask.OrderTaskConst;
import com.company.orderTask.domain.OrderTaskRunInfo;
import com.company.orderTask.scheduler.StepTaskCheckScheduler;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

@RestController
@RequestMapping("/orderTask")
public class OrderTaskController {
	@Resource(name="stepTaskCheckScheduler")
	private StepTaskCheckScheduler stepTaskCheckScheduler;
	@RequestMapping(method = RequestMethod.POST,value = "{category}/{dbId}/{orderId}/{stepId}/runOrderTask")
	public  ProcessResult runTaskImmediate(@PathVariable String category,@PathVariable String dbId,@PathVariable String orderId,@PathVariable String stepId,@RequestBody OrderTaskRunInfo orderTaskRunInfo) {
		ProcessResult processResult =new ProcessResult();
		processResult.setRetCode(OrderTaskConst.RESULT_Success);
		try {
			stepTaskCheckScheduler.immediateRunTask(category, stepId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
}
