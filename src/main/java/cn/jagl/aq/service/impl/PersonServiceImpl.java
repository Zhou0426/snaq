package cn.jagl.aq.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.jagl.aq.dao.PersonDao;
import cn.jagl.aq.dao.PersonRecordDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.PersonRecord;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.RoleResource;
import cn.jagl.aq.service.PersonService;
@Service("personService")
public class PersonServiceImpl implements PersonService {
	@javax.annotation.Resource(name="personDao")	
	private PersonDao personDao;
	@javax.annotation.Resource(name="personRecordDao")	
	private PersonRecordDao personRecordDao;
	
	@Override
	public void addPerson(Person person) {
		personDao.save(person);
		Person Person=personDao.getByPersonId(person.getPersonId());
		PersonRecord personRecord=new PersonRecord();
		personRecord.setPerson(Person);
		personRecord.setDepartment(person.getDepartment());
		personRecord.setStartDateTime(LocalDateTime.now());
		personRecordDao.save(personRecord);
	}

	@Override
	public List<Person> getAllPeople() {
		return personDao.findAll(Person.class);
	}

	@Override
	public void deletePerson(int id) {		
		Person person = this.getById(id);
		List<PersonRecord> list=personRecordDao.getListByPersonId(person.getPersonId());
		for(PersonRecord pe:list){
			personRecordDao.delete(PersonRecord.class, pe.getId());
		}
		personDao.delete(Person.class, id);
	}

	@Override
	public Person getByPersonId(String personId) {
		return personDao.getByPersonId(personId);
	}

	@Override
	public List<Person> findByPage(String hql , int pageNo, int pageSize) {
		return personDao.findByPage(hql, pageNo, pageSize);
	}
	@Override
	public	long findCount(Class<Person> person) {
		return personDao.findCount(person);
	}
	
	@Override
	public List<Resource> getMenuResourceByPersonId(String personId){
		List<Resource> menus = new ArrayList<Resource>();
		return menus;
	}
	
	@Override
	public boolean hasResource(String personId, String resourceSn){
		Set<Role> roles = personDao.getByPersonId(personId).getRoles();
		for(Role role : roles){
			Set<RoleResource> roleResources = role.getRoleResources();
			for(RoleResource roleResource : roleResources){
				if(resourceSn.equals(roleResource.getResource().getResourceSn())){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public List<Department> getResourcePermissionScope(String personId, String resourceSn){
		return personDao.getResourcePermissionScope(personId, resourceSn);
	}

	@Override
	public List<DepartmentType> getResourceDepartmentTypeScope(String personId, String resourceSn){
		List<DepartmentType> departmentTypes = new ArrayList<DepartmentType>();
		
		Set<Role> roles = personDao.getByPersonId(personId).getRoles();
		for(Role role : roles){
			Set<RoleResource> roleResources = role.getRoleResources();
			for(RoleResource roleResource : roleResources){
				if(resourceSn.equals(roleResource.getResource().getResourceSn())){
					Set<DepartmentType> depTypes = roleResource.getDepartmentTypes();
					for(DepartmentType departmentType : depTypes){
						departmentTypes.add(departmentType);
					}
				}
			}
		}
		return departmentTypes;
	}

	@Override
	public List<Person> getDepartmentsByDepartmentSn(String departmentSn) {
		return personDao.getDepartmentsByDepartmentSn(departmentSn);
	}

	@Override
	public void updatePerson(Person person) {
		personDao.update(person);
		PersonRecord personData=personRecordDao.getByPersonId(person.getPersonId());
		if(personData!=null){
			if(person.getDeleted()==true){
				personData.setEndDateTime(LocalDateTime.now());
			}else{
				if(!personData.getDepartment().getDepartmentSn().equals(person.getDepartment().getDepartmentSn())){
					
					personData.setEndDateTime(LocalDateTime.now());
					personData.setPerson(person);
					personRecordDao.update(personData);
					PersonRecord personRecordNew=new PersonRecord();
					personRecordNew.setPerson(person);
					personRecordNew.setDepartment(person.getDepartment());
					personRecordNew.setStartDateTime(LocalDateTime.now());
					personRecordDao.save(personRecordNew);
				}else{
					List<PersonRecord> list=personRecordDao.getListByPersonId(personData.getPerson().getPersonId());
					for(PersonRecord pe:list){
						pe.setPerson(person);
						personRecordDao.update(pe);
					}
				}
			}
		}else{
			PersonRecord personRecordNew=new PersonRecord();
			personRecordNew.setPerson(person);
			personRecordNew.setDepartment(person.getDepartment());
			personRecordNew.setStartDateTime(LocalDateTime.now());
			personRecordDao.save(personRecordNew);
		}
	}

	@Override
	public List<Resource> getMenu(String personId, String parentResourceSn) {
		return personDao.getMenu(personId, parentResourceSn);
	}
	@Override
	public long findNum(String departmentSn) {
		return personDao.findNum(departmentSn);
	}

	@Override
	public List<Person> getByPersonIds(String ids) {
		return personDao.getByPersonIds(ids);
	}

	@Override
	public List<String> getPersonIds() {
		return personDao.getPersonIds();
	}
	@Override
	public List<Role> getRoles(Person person) {
		return personDao.getRoles(person);
	}

	@Override
	public long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn) {
		return personDao.getPersonNumByDepartmentSn(persons, departmentSn);
	}

	@Override
	public List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn) {
		return personDao.getPersonByDepartmentSn(persons, departmentSn);
	}

	@Override
	public long findTotal(String roleSn,String departmentSn) {
		return personDao.findTotal(roleSn, departmentSn);
	}

	@Override
	public long findTotalByDept(String departmentSn) {
		return personDao.findTotalByDept(departmentSn);
	}

	@Override
	public List<Person> getBySql(String sql, int page, int rows) {
		return personDao.getBySql(sql, page, rows);
	}
	public HashMap<String, String> getResources(String personId) {
		return personDao.getResources(personId);
	}

	@Override
	public HashMap<String, String> getRoles(String personId) {
		return personDao.getRoles(personId);
	}

	@Override
	public List<String> getPersonSnByDepartmentSn(String departmentSn) {
		return personDao.getPersonSnByDepartmentSn(departmentSn);
	}

	@Override
	public List<Person> searchByQ(String q) {
		return personDao.serachByQ(q);
	}

	@Override
	public long countBySQL(String sql) {
		return personDao.countBySQL(sql);
	}

	@Override
	public Long getCountByHql(String hql) {
		return personDao.getCountByHql(hql);
	}

	@Override
	public long countHql(String hql) {
		return personDao.countHql(hql);
	}

	@Override
	public Person getById(int id) {
		return personDao.getById(id);
	}

	@Override
	public List<Person> getPersonsByHql(String hql) {
		return personDao.getPersonsByHql(hql);
	}

	@Override
	public Set<Person> getPersonsByRoleId(int roleId) {
		return personDao.getPersonsByRoleId(roleId);
	}

	@Override
	@Scheduled(cron="0 10 0 * * ?") 
	public void documentUpload() {
		personDao.documentUpload();
	}

	@Override
	public List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn) {
		return personDao.getResourcePermissionScopeNotDeleted(personId,resourceSn);
	}
}
