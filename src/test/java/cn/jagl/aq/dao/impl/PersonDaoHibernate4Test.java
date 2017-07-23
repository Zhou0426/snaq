package cn.jagl.aq.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.UnsafeCondition;
import cn.jagl.aq.service.PersonService;
import cn.jagl.aq.service.UnsafeConditionService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"classpath:applicationContext.xml"}) 
@org.springframework.transaction.annotation.Transactional 
public class PersonDaoHibernate4Test {
	@Resource PersonService personService;  
	@Test
	public void test() {
		personService.getResourcePermissionScope("1000031", "0101");
	}
	@Resource UnsafeConditionService unsafeConditionService;  
	@Test
	public void countHqltest() {
		List<UnsafeCondition> unsafeConditions=unsafeConditionService.query("select i from UnsafeCondition i,Person p where i in elements(p.InconformityItems) and p.id='admin')",1,100);
		for(UnsafeCondition un:unsafeConditions){
			System.out.println(un.getInconformityItemSn());
		}
	}
	@Test
	public void countSqltest() {
		List<?> unsafeConditions=unsafeConditionService.getBySql("select inconformity_item_sn from inconformity_item_checker where person_id='admin'");
		String str="(";
		for(Object un:unsafeConditions){
			str=str+"'"+un+"',";
		}
		str=str.substring(0, str.length()-1);
		str+=")";
		List<UnsafeCondition> jsonList=unsafeConditionService.query("select i from UnsafeCondition i where i.inconformityItemSn in "+str, 1, 10000);
		for(UnsafeCondition un:jsonList){
			System.out.println(un);
		}
	}
	@Test
	public void findSet(){
		String hql="select distinct p FROM Person p LEFT JOIN p.unsafeActs u WHERE u.id is not null and u.deleted=false";
		String countHql="select count(distinct p.id) FROM Person p LEFT JOIN p.unsafeActs u WHERE u.id is not null and u.deleted=false";
		Long total=personService.getCountByHql(countHql);
		List<Person> personList=personService.findByPage(hql, 1, 1000);
		int i=0;
		for(Person person:personList){
			i++;
			System.out.println(person.getPersonName());
		}
		System.out.println(total);
		System.out.println(i);
	}
//	@Test
//	public void testGetSuperviseDepartment(){
//		personService.getSuperviseDepartment("admin");
//	}
	@Test
	public void testGetResources(){
		personService.getResources("1000406");
	}
	@Test
	public void testGetRols(){
		personService.getRoles("admin");
	}
	@Test
	public void testGetResourcePermissionScope(){
		List<Department> departments= personService.getResourcePermissionScope("1042562","1403");
		System.out.println(departments);
	}
}
