package com.company.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.orderTask.domain.OrderTaskInDef;
import com.xinwei.nnl.common.util.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTaskApplicationTests {

	@Test
	public void contextLoads() {
		OrderTaskInDef orderTaskInfo = new OrderTaskInDef();
		System.out.println(JsonUtil.toJson(orderTaskInfo));
		//String str = '{\"name\":\"first\", \"category\":\"test\", \"url\":\"http://127.0.0.1/orderDb/addOrderMain\",\"restMethod\":\"addOrderMain\",\"runExpress\":\"0/3 * * * * ?\",\"retryExpress\":\"0/10 * * * * ?\",\"maxThreadNumber\" : 15,\"initThreadNumber\":1,\"keepAliveTime\":100,\"queneSize\":20}'
	}
	
	

}
