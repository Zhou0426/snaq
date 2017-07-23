package cn.jagl.aq.dao.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import cn.jagl.aq.service.UnitAppraisalsService;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:applicationContext.xml"}) 
//@org.springframework.transaction.annotation.Transactional
public class UnitAppraisalsServiceTest {
	@Resource UnitAppraisalsService unitAppraisalsService;  
	@Test
	public void testGetUpNearestImplDepartment() {
		unitAppraisalsService.score();
		System.out.print("game over");
	}
}
