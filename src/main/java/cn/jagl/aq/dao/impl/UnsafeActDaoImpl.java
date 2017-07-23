package cn.jagl.aq.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.UnsafeActDao;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.UnsafeAct;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:03:59
 */
@Repository("unsafeActDao")
public class UnsafeActDaoImpl extends BaseDaoHibernate5<UnsafeAct> implements UnsafeActDao {
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	//根据条件查询不安全行为数目
		@Override
		public long query(String departmentSn,String departmentTypeSn, String specialitySnIndex,String inconformityLevelSnIndex,String begin, String end) {		
			Query query;
			StringBuffer hqls = new StringBuffer();		
			hqls.append("select COUNT(*) FROM UnsafeAct i WHERE i.deleted=false and i.checkedDepartment.departmentSn like '"+departmentSn+"%' and i.checkDateTime>'"+begin+"' and i.checkDateTime<'"+end+"'");		  
			if(specialitySnIndex!=null&&specialitySnIndex.length()>0){
				hqls.append(" AND i.speciality.specialitySn='"+specialitySnIndex+"'");	
			 }	
			if(inconformityLevelSnIndex!=null&&inconformityLevelSnIndex.length()>0){			  
				hqls.append(" AND i.inconformityLevel='"+inconformityLevelSnIndex+"'");	
			}	
			if(departmentTypeSn!=null && departmentTypeSn.length()>0){
				   hqls.append(" AND i.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSn);
			}
			String hql=hqls.toString();
			query=getSessionFactory().getCurrentSession().createQuery(hql);
			return (Long)query.uniqueResult();
		}
		
		@Override
		public long countByHql(String hql) {
			Query query=getSessionFactory().getCurrentSession().createQuery(hql);
			return (Long)query.uniqueResult();
		}
		@Override
		public void deleteByIds(String ids) {
			String hql="update UnsafeAct i SET i.deleted=true,i.periodicalCheck=null,i.specialCheck=null,i.systemAudit=null WHERE i.id in ("+ids+")";
			getSessionFactory().getCurrentSession()
			.createQuery(hql).executeUpdate();
		}

		@Override
		public UnsafeAct getById(int id) {
			String hql="select u from UnsafeAct u where u.id='"+id+"'";
			Query query=getSessionFactory().getCurrentSession().createQuery(hql);
			return (UnsafeAct)query.uniqueResult();
		}

		@Override
		public UnsafeAct getBySn(String inconformityItemSn) {
			String hql="select i from UnsafeAct i where inconformityItemSn=:inconformityItemSn";
			
			return (UnsafeAct) getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.setString("inconformityItemSn", inconformityItemSn)
					.uniqueResult();
		}
		@Override
		public Map<String, Object> showData(String checkDeptSn, String departmentSn,
						String str,String specialitySn,	String unsafeActStandardSn,
						String checkerFromSn,String checkTypeSn,String unsafeActLevelSn,
						String timeData,Timestamp beginTime,Timestamp endTime,
						String checkers,int page,int rows)
		{
			  //获取hql
			  String hql = this.hql(checkDeptSn, departmentSn, str, 
						  specialitySn, unsafeActStandardSn, checkerFromSn,
						  checkTypeSn, unsafeActLevelSn, timeData,
						  beginTime, endTime, checkers);
			  
		  	  //查询总数的hql语句
		  	  String countHql=hql.replaceFirst("distinct i", "count(distinct i.inconformityItemSn)");
		  	  
		  	  List<UnsafeAct> jsonList=new ArrayList<UnsafeAct>();
		  	  if(page != 0 && !"".equals(page) && rows != 0 && !"".equals(rows))
		  	  {
		  		  jsonList = this.findByPage(hql, page, rows);
	    	  }else{
	    		  jsonList = this.find(hql);
	    	  }
		  	  Long total = this.countByHql(countHql);
		  	  
		  	  JSONArray jsonArray = this.ListToJSONArray(jsonList);
		    
		  	  Map<String, Object> json = new HashMap<String, Object>();
		  	  json.put("total", total);// total键 存放总记录数，必须的
		  	  json.put("rows", jsonArray);// rows键 存放每页记录 list
		  	  return json;
		}
		@Override
		public Map<String, Object> queryMyUnsafeAct(String personId, Integer page, Integer rows) {
			String hql = "SELECT DISTINCT u FROM UnsafeAct u LEFT JOIN u.checkers c"
					+ " WHERE u.deleted != true AND (u.editor.personId = '" + personId +"'"
					+ " OR u.violator.personId = '" + personId +"' OR c.personId = '" + personId +"')"
					+ " ORDER BY u.checkDateTime desc";
			String countHql = hql.replaceFirst("DISTINCT u", "COUNT(DISTINCT u.inconformityItemSn)");
			
			Long total = this.countByHql(countHql);
			
			List<UnsafeAct> lists = this.findByPage(hql, page, rows);
			JSONArray jsonArray = this.ListToJSONArray(lists);
			
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", total);// total键 存放总记录数，必须的
	        json.put("rows", jsonArray);// rows键 存放每页记录 list
			return json;
		}
		/**
		 * 将list转为jsonArray
		 * @param jsonList
		 * @return
		 */
		public JSONArray ListToJSONArray(List<UnsafeAct> jsonList){
			  JSONArray jsonArray=new JSONArray();
		  	  for(UnsafeAct inc:jsonList){
		  		  JSONObject jo=new JSONObject();
		  		  //检查人checkers
		  		  Set<Person> set=inc.getCheckers();
	  			  if(set.size()>0){
	  				  String s="";
	  				  for(Iterator<Person> iter=set.iterator();iter.hasNext();){
	  					  s+=iter.next().getPersonName()+",";
	  				  }
	  				  s=s.substring(0, s.lastIndexOf(","));
	  				  jo.put("checkers", s);
	  			  }
		  		  jo.put("id", inc.getId());
		  		  jo.put("checkType", inc.getCheckType());
		  		  jo.put("checkerFrom", inc.getCheckerFrom());
		  		  jo.put("inconformityItemSn", inc.getInconformityItemSn());
		  		  if(inc.getCheckDateTime()!=null){
		  			  jo.put("checkDateTime", inc.getCheckDateTime().toString());
		  		  }
		  		  jo.put("checkLocation", inc.getCheckLocation());
		  		  if(inc.getCheckedDepartment()!=null){
		  			  jo.put("checkedDepartment", inc.getCheckedDepartment().getDepartmentName());
		  			  jo.put("checkedDepartmentImplType", inc.getCheckedDepartment().getImplDepartmentName());
		  		  }else{
		  			  jo.put("checkedDepartment", inc.getCheckedDepartment());
		  		  }
		  		  if(inc.getSpeciality()!=null){
		  			  jo.put("speciality", inc.getSpeciality().getSpecialityName());
		  		  }
		  		  if(inc.getViolator()!=null){
		  			  jo.put("violator", inc.getViolator().getPersonName());
		  		  }
		  		  jo.put("actDescription", inc.getActDescription());
		  		  jo.put("unsafeActMark", inc.getUnsafeActMark());
		  		  if(inc.getUnsafeActStandard()!=null){
		  			  jo.put("unsafeActStandard", inc.getUnsafeActStandard().getStandardDescription());
		  			  jo.put("UnsafeActLevel", inc.getUnsafeActStandard().getUnsafeActLevel());
		  		  }
		  		  if(inc.getSystemAudit()!=null){
		  			  jo.put("systemAudit",inc.getSystemAudit().getSystemAuditType());
		  		  }
			  	  if(inc.getEditor()!=null){
					  jo.put("editor", inc.getEditor().getPersonName());
					  jo.put("editorId", inc.getEditor().getPersonId());
				  }else{
					  jo.put("editor", "无");
					  jo.put("editorId", "无");
				  }
		  		  jo.put("attachments", inc.getAttachments().size());
		  		  jsonArray.add(jo);
		  	  }
		  	  return jsonArray;
		}
		/**
		 * 拼接hql
		 * @param checkDeptSn
		 * @param departmentSn
		 * @param str
		 * @param specialitySn
		 * @param unsafeActStandardSn
		 * @param checkerFromSn
		 * @param checkTypeSn
		 * @param unsafeActLevelSn
		 * @param timeData
		 * @param beginTime
		 * @param endTime
		 * @param checkers
		 * @return
		 */
		public String hql(String checkDeptSn, String departmentSn,String str,String specialitySn,
				String unsafeActStandardSn,String checkerFromSn,String checkTypeSn,String unsafeActLevelSn,
				String timeData,Timestamp beginTime,Timestamp endTime,String checkers){
			      String ksTime="";
			      String jsTime="";
				  //拼接hql语句
				  StringBuffer hqll = new StringBuffer();
				  hqll.append("select distinct i from UnsafeAct i where i.deleted=false");
				  //判断部门类型是否为空来判断是否选择部门类型
				  if(str !=null && !"".equals(str)){
				  	hqll.append(" AND ("+str+")");
				  		
				  }else{
				  	hqll.append(" AND i.checkedDepartment.departmentSn like '"+departmentSn+"%'");
				  }
				  //专业
			  	  if(specialitySn !=null && !"".equals(specialitySn)){
			  		  String asum="(";
			  		  String[] array = specialitySn.split("##");
			  		  for(String a:array){
			  			  asum=asum+"'"+a+"',";
			  		  }
			  		  asum=asum.substring(0, asum.length()-1);
			  		  asum+=")";
			  		  hqll.append(" AND i.speciality.specialitySn in "+asum);
			  	  }
			  	  //不安全行为标准
			  	  if(unsafeActStandardSn!=null && !"".equals(unsafeActStandardSn)){
			  		  hqll.append(" AND i.unsafeActStandard.standardSn='"+unsafeActStandardSn+"'");
			  	  }
		    	  //根据检查部门
		    	  if(checkDeptSn != null && !"".equals(checkDeptSn)){
		    		  hqll.replace(hqll.indexOf("UnsafeAct"), hqll.indexOf("where"), " UnsafeAct i LEFT JOIN i.checkers c ");
		    		  hqll.append(" AND c.department.departmentSn like '" + checkDeptSn + "%'");// AND c.deleted = false  OR i.editor.department.departmentSn like '" + checkDeptSn + "%')
		    	  }
			  	  //检查人来自
			  	  if(checkerFromSn!=null && !"".equals(checkerFromSn)){
					int checkerFromNum=Integer.parseInt(checkerFromSn);
					hqll.append(" AND i.checkerFrom='"+checkerFromNum+"'");
			  	  }
			  	  //检查类型
			  	  if(checkTypeSn!=null && !"".equals(checkTypeSn)){
			  		  int checkTypeNum=Integer.parseInt(checkTypeSn);
			  		  hqll.append(" AND i.checkType='"+checkTypeNum+"'");
			  	  }
			  	  //不安全行为等级
			  	  if(unsafeActLevelSn!=null && !"".equals(unsafeActLevelSn)){
			  		  int unsafeActLevel=Integer.parseInt(unsafeActLevelSn);
			  		  hqll.append(" AND i.unsafeActStandard.unsafeActLevel='"+unsafeActLevel+"'");
			  	  }
			  	  //时间按钮的数据
			  	if(timeData!=null&&timeData.trim().length()>0){
		  		  
		  		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  		  Calendar cal =Calendar.getInstance();
		  		  Calendar cal2 =Calendar.getInstance();
		  		  
		  		  if(timeData.equals("today")){
		  			  
		  			  ksTime=df.format(new Date());//当前日期
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+ksTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("week")){
		  			  
		  			  String now=df.format(new Date());						//获取当前时间赋值给now
		  			  cal2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);		//获取本周日时间（外国周日）
		  			  String now2=df.format(cal2.getTime());				//周日赋值给now2
		  			  
		  			  if(now.equals(now2)){									//判断今天是否是周日
		  				  
		  				  cal.add(Calendar.WEEK_OF_YEAR, -1);				//是周日，则周数减一
		  				  cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);	//获取上周的周一
		  				  ksTime=df.format(cal.getTime());				//开始时间
		  				  jsTime=now2;									//今天时间=结束时间
		  				  
		  			  }else{												//不是周日
		  				  
		  				  cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 	//获取本周一的日期
		  				  ksTime=df.format(cal.getTime());
		  				  
		  				  cal2.add(Calendar.WEEK_OF_YEAR, 1);				//增加一个星期，周日的日期
		  				  jsTime=df.format(cal2.getTime());
		  			  }
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("xun")){
		  			  
		  			  int day=cal.get(Calendar.DATE);			//获取当前天数
		  			  cal.add(Calendar.MONTH, 0);
		  			  
		  			  if(day<=10){
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH,1);		//设置为1号,当前日期既为本月第一天
		  				  ksTime=df.format(cal.getTime());
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH,10);	//设置为10号,当前日期既为本月上旬
		  				  jsTime=df.format(cal.getTime());
		  				  
		  			  }else if(day<=20){
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH,11);	//设置为11号,当前日期既为本月中旬第一天
		  				  ksTime=df.format(cal.getTime());
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH,20);	//设置为20号,当前日期既为本月中旬最后一天
		  				  jsTime=df.format(cal.getTime());
		  				  
		  			  }else{
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH,21);	//设置为21号,当前日期既为本月下旬第一天
		  				  ksTime=df.format(cal.getTime());
		  				  
		  				  cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		  				  jsTime=df.format(cal.getTime());
		  				  
		  			  }
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("month")){
		  			  //cal.add(Calendar.MONTH, 0);
		  			  
		  			  cal.set(Calendar.DAY_OF_MONTH,1);		//设置为1号,当前日期既为本月第一天
		  			  ksTime=df.format(cal.getTime());
		  			  
		  			  cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
		  			  jsTime=df.format(cal.getTime());
		  			  
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("quarter")){
		  			  
		  			  int month = cal.get(Calendar.MONTH) + 1;	//当前月份
		  			  try {  
		  				  
		  				  if (month >= 1 && month <= 3){  
		  					  
		  					  cal.set(Calendar.MONTH, 0); 
		  					  cal2.set(Calendar.MONTH, 2);
		  					  cal2.set(Calendar.DATE, 31);
		  					  
		  				  }else if (month >= 4 && month <= 6){
		  					  
		  					  cal.set(Calendar.MONTH, 3);
		  					  cal2.set(Calendar.MONTH, 5);
		  					  cal2.set(Calendar.DATE, 30);
		  					  
		  				  }else if (month >= 7 && month <= 9){
		  					  
		  					  cal.set(Calendar.MONTH, 6); 
		  					  cal2.set(Calendar.MONTH, 8);
		  					  cal2.set(Calendar.DATE, 30);
		  					  
		  				  }else if (month >= 10 && month <= 12){
		  					  
		  					  cal.set(Calendar.MONTH, 9); 
		  					  cal2.set(Calendar.MONTH, 11);
		  					  cal2.set(Calendar.DATE, 31);
		  					  
		  				  }
		  				  cal.set(Calendar.DATE, 1); 
		  				  ksTime=df.format(cal.getTime());
		  				  jsTime=df.format(cal2.getTime());
		  				  //System.out.println(df.format(cal.getTime())); 
		  			  } catch (Exception e) {  
		  				  e.printStackTrace();  
		  			  }
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("banyear")){
		  			  
		  			  int month = cal.get(Calendar.MONTH) + 1;	//当前月份
		  			  
		  			  try {  
		  				  
		  				  if (month >= 1 && month <= 6){  
		  					  
		  					  cal.set(Calendar.MONTH, 0); 
		  					  cal2.set(Calendar.MONTH, 5);
		  					  cal2.set(Calendar.DATE, 30);
		  					  
		  				  }else{
		  					  
		  					  cal.set(Calendar.MONTH, 6);
		  					  cal2.set(Calendar.MONTH, 11);
		  					  cal2.set(Calendar.DATE, 31);
		  					  
		  				  }
		  				  cal.set(Calendar.DATE, 1); 
		  				  ksTime=df.format(cal.getTime());
		  				  jsTime=df.format(cal2.getTime());
		  				  
		  			  } catch (Exception e) {  
		  				  e.printStackTrace();  
		  			  }
		  			  
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  			  
		  		  }else if(timeData.equals("year")){
		  			  
		  			  cal.set(Calendar.MONTH, 0);
		  			  cal.set(Calendar.DATE, 1); 
		  			  cal2.set(Calendar.MONTH, 11);
		  			  cal2.set(Calendar.DATE, 31);
		  			  ksTime=df.format(cal.getTime());
		  			  jsTime=df.format(cal2.getTime());
		  			  
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  		  }
		  	  }else{
		  		  if(beginTime!=null && !"".equals(beginTime) && endTime!=null && !"".equals(endTime)){
		  			  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  			  ksTime=df.format(beginTime);
		  			  jsTime=df.format(endTime);
		  			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59'");
		  		  }
		  	  }
			  if(checkers!=null && !"".equals(checkers)){
		  		  hqll.append(" AND i.inconformityItemSn in "+checkers);
		  	  }
			  hqll.append(" order by i.checkDateTime desc");
	  	  
		  	  //查询数据的hql语句
		  	  String hql=hqll.toString();
		  	  return hql;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<UnsafeAct> findByHql(String hql) {
			return getSessionFactory().getCurrentSession()
					.createQuery(hql)
					.list();
		}
	
}
