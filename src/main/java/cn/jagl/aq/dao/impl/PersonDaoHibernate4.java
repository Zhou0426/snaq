package cn.jagl.aq.dao.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.opensymphony.xwork2.ActionContext;

import cn.jagl.aq.Interceptor.LoginInterceptor;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.PersonDao;
import cn.jagl.aq.dao.SmsDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.Resource;
import cn.jagl.aq.domain.Role;
import cn.jagl.aq.domain.Sms;
import cn.jagl.util.AwsS3Util;
import cn.jagl.util.RandomUtil;
import cn.jagl.util.SmsUtil;

@SuppressWarnings("unchecked")
@Repository("personDao")
public class PersonDaoHibernate4 extends BaseDaoHibernate5<Person>
	implements PersonDao 
{
	private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
	@javax.annotation.Resource(name="smsDao")
	private SmsDao smsDao;
	
	@Override
	public Person getByPersonId(String personId) {

		Query query = getSessionFactory().getCurrentSession()
		.createQuery("select p from Person p where personId=:personId")
		.setString("personId", personId);
		return (Person)query.uniqueResult();
	}
	public List<Person> getDepartmentsByDepartmentSn(String departmentSn){
		Query query =getSessionFactory().getCurrentSession()
				.createQuery("select p from Person p where p.department.departmentSn=:departmentSn").setString("departmentSn", departmentSn);
				return (List<Person>)query.list();
	}
	@Override
	public List<Resource> getMenu(String personId, String parentResourceSn) {
		String sql;
		Query query;
		if(parentResourceSn==null){
			sql="select distinct resource.* from resource inner join role_resource on resource.resource_sn=role_resource.resource_sn inner join role on role_resource.role_sn=role.role_sn inner join person_role on role.role_sn=person_role.role_sn where resource.deleted=0 and resource.resource_type='menu' and parent_resource_sn is null and person_role.person_id=:person_id order by resource.show_sequence";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(Resource.class).setString("person_id", personId);
		}else{
			sql="select distinct resource.* from resource inner join role_resource on resource.resource_sn=role_resource.resource_sn inner join role on role_resource.role_sn=role.role_sn inner join person_role on role.role_sn=person_role.role_sn where resource.deleted=0 and resource.resource_type='menu' and parent_resource_sn=:parent_resource_sn and person_role.person_id=:person_id  order by resource.show_sequence";
			query =getSessionFactory().getCurrentSession()
					.createSQLQuery(sql).addEntity(Resource.class).setString("parent_resource_sn", parentResourceSn).setString("person_id", personId);
		}
		return (List<Resource>)query.list();
	}
	@Override
	public long findNum(String departmentSn) {
		// TODO Auto-generated method stub
		String hql="select COUNT(p) FROM Person p WHERE p.department.departmentSn like :departmentSn";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("departmentSn", departmentSn+"%");
		return (Long)query.uniqueResult();
	}
	/**
	 * @author mahui
	 * @mtthod 
	 * @date 2016年7月9日下午3:50:42
	 */
	@Override
	public List<Person> getByPersonIds(String ids) {
		String hql="FROM Person p WHERE id in("+ ids+")";
		List<Person> personList=getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.list();
		return personList;
	}
	@Override
	public List<Department> getResourcePermissionScope(String personId, String resourceSn) {
		//第1步：建立存储权限范围的列表变量
		List<Department> departments=new ArrayList<Department>();		
		//第2步：获取personId对资源resourceSn有权限的集团类型角色，并添加权限范围：顶级部门
		String hql="select department.* from department where parent_department_sn is null and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=0 and role_resource.resource_sn='"+resourceSn+"')";
		List<Department> departmentList=getSessionFactory().getCurrentSession()
						.createSQLQuery(hql).addEntity(Department.class)
						.list();
		if(departmentList.size()>0){
			return departmentList;
		}
		//第3步：获取personId对资源resourceSn有权限的分公司类型角色，并添加权限范围：分公司
		hql="select department.* from department where department.department_type_sn='fgs' and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=1 and role_resource.resource_sn='"+resourceSn+"' and LOCATE(department.department_sn,person.department_sn)=1) order by CHAR_LENGTH(department.department_sn) desc";
		Department department=(Department) getSessionFactory().getCurrentSession()
								.createSQLQuery(hql).addEntity(Department.class)
								.uniqueResult();
		if(department!=null)
			departments.add(department);
		//第4步：获取personId对资源resourceSn有权限的贯标角色，并添加权限范围：贯标单位
		hql="select department.* from department inner join department_type on department.department_type_sn=department_type.department_type_sn where department_type.is_impl_department_type=1 and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=2 and role_resource.resource_sn='"+resourceSn+"' and LOCATE(department.department_sn,person.department_sn)=1)  order by CHAR_LENGTH(department.department_sn) desc";
		department=(Department) getSessionFactory().getCurrentSession()
										.createSQLQuery(hql).addEntity(Department.class)
										.uniqueResult();
		if(department!=null)
			departments=mergeDepartmentList(departments,department);
		//第5步：获取其角色在role_resource_department中对资源resourceSn的部门范围列表，并添加到权限范围列表中
		hql="select department.* from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role_resource_department on role_resource.role_sn=role_resource_department.role_sn and role_resource.resource_sn=role_resource_department.resource_sn inner join department on role_resource_department.department_sn=department.department_sn WHERE person.person_id='"+personId+"' and role_resource.resource_sn='"+resourceSn+"'";
		departmentList=getSessionFactory().getCurrentSession()
				.createSQLQuery(hql).addEntity(Department.class)
				.list();
		if(departmentList.size()>0)
			departments=mergeDepartmentList(departments,departmentList);
		//第6步：获取其角色在role_resource_department_type中对资源a的部门类型列表，并将相应的部门添加到权限范围列表中
		hql="select distinct department.* from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role_resource_department_type on role_resource.role_sn=role_resource_department_type.role_sn and role_resource.resource_sn=role_resource_department_type.resource_sn inner join department on role_resource_department_type.department_type_sn=department.department_type_sn WHERE person.person_id='"+personId+"' and role_resource.resource_sn='"+resourceSn+"'";
		departmentList=getSessionFactory().getCurrentSession()
				.createSQLQuery(hql).addEntity(Department.class)
				.list();
		if(departmentList.size()>0)
			departments=mergeDepartmentList(departments,departmentList);
		return departments;
	}
	public List<Department> mergeDepartmentList(List<Department> departments,Department department){
		Iterator<Department> iterator=departments.iterator();
		Boolean haveOffspring=false;//有后代
		Boolean haveAncestors=false;//有祖先
		Boolean haveSelf=false;//有自己
		while(iterator.hasNext()){
			Department dept=iterator.next();
			if(dept.getDepartmentSn().indexOf(department.getDepartmentSn())==0&&dept.getDepartmentSn().length()>department.getDepartmentSn().length()){
				iterator.remove();
				haveOffspring=true;//有后代，需要添加
			}
			if(department.getDepartmentSn().indexOf(dept.getDepartmentSn())==0&&department.getDepartmentSn().length()>department.getDepartmentSn().length()){
				haveAncestors=true;//有祖先，不需要添加
			}
			if(dept.getDepartmentSn().equals(department.getDepartmentSn())){
				haveSelf=true;//有自己不需要添加
			}
		}
		if(haveOffspring){//有后代，需要添加
			departments.add(department);
		}else{//没有后代
			if(!haveAncestors&&!haveSelf){
				departments.add(department);
			}
		}
		return departments;
	}
	public List<Department> mergeDepartmentList(List<Department> departments1,List<Department> departments2){
		for(Department department:departments2){
			departments1=mergeDepartmentList(departments1,department);
		}
		return departments1;
	}
	@Override
	public List<String> getPersonIds() {
		String hql="select p.personId FROM Person p";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}
	@Override
	public List<Role> getRoles(Person person) {
		String hql="select p.roles FROM Person p WHERE p.id= ("+person.getId() +")";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Role>)query.list();
	}
	//筛选当前list集合里的人员中属于该部门下的人数
	@Override
	public long getPersonNumByDepartmentSn(List<Person> persons, String departmentSn) {
		String pi="(";
		for(Person person:persons){
			pi+="'"+person.getPersonId()+"',";
		}
		pi=pi.substring(0, pi.length()-1);
		pi+=")";
		String hql="select Count(*) FROM Person p WHERE p.personId in  "+pi+"  and p.department.departmentSn like '"+departmentSn+"%'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	}
	@Override
	public List<Person> getPersonByDepartmentSn(List<Person> persons, String departmentSn) {
		String pi="(";
		for(Person person:persons){
			pi+="'"+person.getPersonId()+"',";
		}
		pi=pi.substring(0, pi.length()-1);
		pi+=")";
		String hql="select p FROM Person p WHERE p.personId in  "+pi+"  and p.department.departmentSn like '"+departmentSn+"%'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<Person>)query.list();
	}
	@Override
	public long findTotal(String roleSn,String departmentSn) {
		//String hql="select Count(*) FROM Person p WHERE p.department.departmentSn like '"+departmentSn+"%'";
		String sql="select distinct Count(*) FROM person,role,person_role where person.person_id=person_role.person_id and person_role.role_sn=role.role_sn and role.role_sn='"+roleSn+"' and person.department_sn like '"+departmentSn+"%'";
		//Query query=getSessionFactory().getCurrentSession().createSQLQuery(sql);
		System.out.println(getSessionFactory().getCurrentSession().createQuery(sql).uniqueResult());
		return ((BigInteger)getSessionFactory().getCurrentSession().createQuery(sql).uniqueResult()).longValue();
	}
	@Override
	public long findTotalByDept(String departmentSn) {
		// TODO Auto-generated method stub
		String hql="select Count(*) FROM Person p where p.department.departmentSn like'"+departmentSn+"%'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	}
	@Override
	public List<Person> getBySql(String sql, int page, int rows) {
		// TODO Auto-generated method stub
		return (List<Person>)getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(Person.class).setFirstResult((page - 1) * rows)//设置每页起始的记录编号
				.setMaxResults(rows)//设置需要查询的最大结果集
				.list();
	}
	@Override
	public HashMap<String, String> getResources(String personId) {
		String sql="select distinct resource.* FROM resource inner join role_resource  on resource.resource_sn=role_resource.resource_sn inner join person_role on role_resource.role_sn=person_role.role_sn WHERE person_role.person_id=:personId";
		Query query=getSessionFactory().getCurrentSession().createSQLQuery(sql).addEntity(Resource.class);
		query=query.setParameter("personId", personId);
		List<Resource> resources=new ArrayList<Resource>();
		resources=(List<Resource>)query.list();
		HashMap<String, String> permissions=new HashMap<String, String>();
		for(Resource resource:resources){
			permissions.put(resource.getResourceSn(), resource.getUrl());
		}
		return permissions;
	}
	@Override
	public HashMap<String, String> getRoles(String personId) {
		String hql="select r from Role r inner join r.persons p where p.personId=:personId";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		query=query.setParameter("personId", personId);
		List<Role> rolesList=query.list();
		HashMap<String, String> roles=new HashMap<String, String>();
		for(Role role:rolesList){
			roles.put(role.getRoleSn(), role.getRoleName());
		}
		return roles;
	}
	@Override
	public List<String> getPersonSnByDepartmentSn(String departmentSn) {
		String hql="select p.personId FROM Person p where p.department.departmentSn like '"+departmentSn+"%'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (List<String>)query.list();
	}
	@Override
	public List<Person> serachByQ(String q) {
		FullTextSession fts = Search.getFullTextSession(getSessionFactory().getCurrentSession());
		/*try {
			fts.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		fts.getSearchFactory();
	    QueryBuilder qb = fts.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
	    org.apache.lucene.search.Query luceneQuery =  qb.keyword().onFields("personName","personId").matching(q).createQuery();
	    FullTextQuery query = fts.createFullTextQuery(luceneQuery, Person.class);
	    
	    List<Person> data = query.list();
		return data;
	}	
	@Override
	public long countBySQL(String sql) {
		return((BigInteger)getSessionFactory().getCurrentSession().createSQLQuery(sql).list().get(0)).longValue();
	}
	//hql查询总数
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
	//hql查询总数
	@Override
	public Long getCountByHql(String hql) {
		return (Long) getSessionFactory().getCurrentSession().createQuery(hql).uniqueResult();
	}
	@Override
	public Person getById(int id) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("select p from Person p where p.id=:id")
				.setInteger("id", id);
				return (Person)query.uniqueResult();
	}
	@Override
	public List<Person> getPersonsByHql(String hql) {
		return (List<Person>)getSessionFactory().getCurrentSession().createQuery(hql).list();
	}
	@Override
	public Set<Person> getPersonsByRoleId(int roleId) {
		String hql="select p FROM Person p left join p.roles r where r.id='"+roleId+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		Set set = new HashSet(Arrays.asList((List<Person>)query.list()));
		return set;
	}
	//通过文件名获取扩展名
	private String getFileExt(String filename){
		return FilenameUtils.getExtension(filename);
	}
	//通过文件名获取没有扩展名的文件名
	private String getFileName(String filename){
		return filename.replace("."+getFileExt(filename), "");
	}
	
	//加载配置数据的静态代码块
	private static String logLocaltion;
	static{
		org.springframework.core.io.Resource resource = new ClassPathResource("awsconf.properties");
		InputStream in;
		try {
			in = resource.getInputStream();
			Properties prop = new Properties();
			prop.load(in);
			logLocaltion=prop.getProperty("logLocaltion");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * 区分主机是window还是linux系统
	 * @return
	 */
	public static boolean isWindowsOS(){
	     boolean isWindowsOS = false;
	     String osName = System.getProperty("os.name");
		     if(osName.toLowerCase().indexOf("windows")>-1){
		      isWindowsOS = true;
		     }
	     return isWindowsOS;
	}

	@Override
	public void documentUpload() {
		PersonDaoHibernate4.log.info("start datetime：{}",java.time.LocalDateTime.now());
		//String path = ServletActionContext.getServletContext().getRealPath("/");
		LocalDate date = LocalDate.now().minusDays(1);
		String path = logLocaltion + "/" + date.toString() + "-snaq.log";
		boolean judge = true;
		int i = 0;
		while(judge || i > 1){
			File file=new File(path);
			String localip = "";
		    InetAddress ip = null;
		    try {
			     //如果是Windows操作系统
			     if(isWindowsOS()){
			      ip = InetAddress.getLocalHost();
			     }
			     //如果是Linux操作系统
			     else{
				      boolean bFindIP = false;
				      Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
				        .getNetworkInterfaces();
				      while (netInterfaces.hasMoreElements()) {
					       if(bFindIP){
					    	   break;
					       }
					       NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					       //----------特定情况，可以考虑用ni.getName判断
					       //遍历所有ip
					       Enumeration<InetAddress> ips = ni.getInetAddresses();
					       while (ips.hasMoreElements()) {
						       ip = (InetAddress) ips.nextElement();
						       if( ip.isSiteLocalAddress() 
						                   && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
						                   && ip.getHostAddress().indexOf(":")==-1){
						            bFindIP = true;
						            break; 
						       }
					       }
				      }
			     }
		    }
		    catch (Exception e) {
		     e.printStackTrace();
		    }
		    if(null != ip){
		    	localip = ip.getHostAddress().replace(".", "-");;
		    }

			//循环文件并上传
			if(file.exists() == true){
					if (((file.getName().lastIndexOf("-snaq") != -1 && LocalDate.now().minusDays(1).isEqual(LocalDate.parse(file.getName().substring(0, file.getName().lastIndexOf("-snaq")))))) || file.getName().equals("snaq.log")) {

						String folder = String.valueOf(LocalDate.now().getYear()) + String.valueOf(LocalDate.now().getMonthOfYear());
						//判断日志文件是否带日期
						String fileName = "";
						if(file.getName().indexOf(date.toString()) == -1){
							fileName = date.toString() + "-";
						}
						fileName = fileName + this.getFileName(file.getName()) + localip + "." + this.getFileExt(file.getName());
						Boolean complete = AwsS3Util.uploadFile("log/" + folder + "/"  + fileName, file);
						//上传失败则发送短信
						String messageContent="";
						if(complete == false){
							messageContent = LocalDate.now().minusDays(1) + "日志文件上传出错！请前往查看！";							
						}else{
							messageContent = LocalDate.now().minusDays(1) + "日志文件上传成功！请前往查看！";
						}
						String serialNumber = RandomUtil.getRandmDigital20();
						int result = SmsUtil.sendSms(serialNumber, "13775899277", messageContent);
						//写入实体Sms
						Sms sms = new Sms();
						sms.setSerialNumber(serialNumber);
						sms.setUserNumber("13775899277");
						sms.setMessageContent(messageContent);
						sms.setResultCode(result);
						sms.setSuccessTimestamp(new Timestamp(System.currentTimeMillis()));
						smsDao.save(sms);
					}
				judge = false;
			}else{
				path = logLocaltion + "/snaq.log";
				i++;
			}
		}
	}
	@Override
	public List<Department> getResourcePermissionScopeNotDeleted(String personId, String resourceSn) {
		//第1步：建立存储权限范围的列表变量
				List<Department> departments=new ArrayList<Department>();		
				//第2步：获取personId对资源resourceSn有权限的集团类型角色，并添加权限范围：顶级部门
				String hql="select department.* from department where deleted=0 and parent_department_sn is null and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=0 and role_resource.resource_sn='"+resourceSn+"')";
				List<Department> departmentList=getSessionFactory().getCurrentSession()
								.createSQLQuery(hql).addEntity(Department.class)
								.list();
				if(departmentList.size()>0){
					return departmentList;
				}
				//第3步：获取personId对资源resourceSn有权限的分公司类型角色，并添加权限范围：分公司
				hql="select department.* from department where deleted=0 and department.department_type_sn='fgs' and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=1 and role_resource.resource_sn='"+resourceSn+"' and LOCATE(department.department_sn,person.department_sn)=1) order by CHAR_LENGTH(department.department_sn) desc";
				Department department=(Department) getSessionFactory().getCurrentSession()
										.createSQLQuery(hql).addEntity(Department.class)
										.uniqueResult();
				if(department!=null)
					departments.add(department);
				//第4步：获取personId对资源resourceSn有权限的贯标角色，并添加权限范围：贯标单位
				hql="select department.* from department inner join department_type on department.department_type_sn=department_type.department_type_sn where department.deleted=0 and department_type.is_impl_department_type=1 and exists(select person.id from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role on role_resource.role_sn=role.role_sn  WHERE person.person_id='"+personId+"' and role.role_type=2 and role_resource.resource_sn='"+resourceSn+"' and LOCATE(department.department_sn,person.department_sn)=1)  order by CHAR_LENGTH(department.department_sn) desc";
				department=(Department) getSessionFactory().getCurrentSession()
												.createSQLQuery(hql).addEntity(Department.class)
												.uniqueResult();
				if(department!=null)
					departments=mergeDepartmentList(departments,department);
				//第5步：获取其角色在role_resource_department中对资源resourceSn的部门范围列表，并添加到权限范围列表中
				hql="select department.* from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role_resource_department on role_resource.role_sn=role_resource_department.role_sn and role_resource.resource_sn=role_resource_department.resource_sn inner join department on role_resource_department.department_sn=department.department_sn WHERE department.deleted=0 and person.person_id='"+personId+"' and role_resource.resource_sn='"+resourceSn+"'";
				departmentList=getSessionFactory().getCurrentSession()
						.createSQLQuery(hql).addEntity(Department.class)
						.list();
				if(departmentList.size()>0)
					departments=mergeDepartmentList(departments,departmentList);
				//第6步：获取其角色在role_resource_department_type中对资源a的部门类型列表，并将相应的部门添加到权限范围列表中
				hql="select distinct department.* from person inner join person_role on person.person_id=person_role.person_id inner join role_resource on person_role.role_sn=role_resource.role_sn inner join role_resource_department_type on role_resource.role_sn=role_resource_department_type.role_sn and role_resource.resource_sn=role_resource_department_type.resource_sn inner join department on role_resource_department_type.department_type_sn=department.department_type_sn WHERE department.deleted=0 and person.person_id='"+personId+"' and role_resource.resource_sn='"+resourceSn+"'";
				departmentList=getSessionFactory().getCurrentSession()
						.createSQLQuery(hql).addEntity(Department.class)
						.list();
				if(departmentList.size()>0)
					departments=mergeDepartmentList(departments,departmentList);
				return departments;
	}

}


