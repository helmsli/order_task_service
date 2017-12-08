package com.company.orderTask;

import com.company.orderTask.domain.OrderTaskInDef;
import com.xinwei.nnl.common.util.JsonUtil;

public class TestJson {
	public static void main(String[] args) {
		TestJson testJson = new TestJson();
		// TODO Auto-generated method stub
		String taskIn="{\"name\":\"等待付款\", \"category\":2}";
	    JsonUtil.fromJson(taskIn,OrderTaskInDef.class);
		
	}
}
