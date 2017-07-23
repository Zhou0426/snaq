package cn.jagl.aq.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardType;
import cn.jagl.aq.service.DepartmentService;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:applicationContext.xml"}) 
@org.springframework.transaction.annotation.Transactional
public class DepartmentDaoHibernate4Test {
	@Resource DepartmentService departmentService;  
	@Test
	public void testGetUpNearestImplDepartment() {
		Department department=departmentService.getUpNearestImplDepartment("1002003");
		int i=0;
		i++;
	}
	@Test
	public void testGetUpNearestByDepartmentType() {
		Department department=departmentService.getUpNearestByDepartmentType("1002003", "fgs");
		int i=0;
		i++;
	}
	@SuppressWarnings("unused")
	@Test
	public void getDownDepartmentByDepartmentType(){
		String h=departmentService.getDownDepartmentByDepartmentType("1", "mk");
		String hql="select i from Department i where "+h;
		List<Department> jsonList=departmentService.findByPage(hql, 1, 10000);
		int i=0;
		for(Department de:jsonList){
			System.out.println(de.getDepartmentName());
			System.out.println(de.getDepartmentSn());
			i++;
		}
		System.out.println(i);		
	}
	@Test
	public void testGetImplStandard(){
		Standard standard=departmentService.getImplStandards("1", StandardType.ÆÀ·Ö±ê×¼);
		int i=0;
		i++;
	}
}
