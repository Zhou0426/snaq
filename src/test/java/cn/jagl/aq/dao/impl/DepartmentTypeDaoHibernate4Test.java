package cn.jagl.aq.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.service.DepartmentTypeService;
import cn.jagl.aq.service.StandardIndexService;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:applicationContext.xml"}) 
@org.springframework.transaction.annotation.Transactional 
public class DepartmentTypeDaoHibernate4Test {
	@Resource DepartmentTypeService departmentTypeService;  
	@Resource DepartmentTypeDaoHibernate4 departmentTypeDao; 
	@Test
	public void testGetImplDepartmentTypes() {
		List<DepartmentType> departmentTypes=new ArrayList<DepartmentType>();
		departmentTypes=departmentTypeService.getImplDepartmentTypes("1");
		int i=0;
		i++; 
	}

	@Test
	public void testGetDownDepartmentTypeByParent(){
		String str=departmentTypeDao.getDownDepartmentTypeByParent("JGMK");
		System.out.println(str);
	}
}
