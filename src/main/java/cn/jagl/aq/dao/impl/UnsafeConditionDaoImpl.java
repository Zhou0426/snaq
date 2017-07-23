package cn.jagl.aq.dao.impl;

import java.math.BigInteger;
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

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.UnsafeConditionDao;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.RiskLevel;
import cn.jagl.aq.domain.UnsafeCondition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @author mahui
 *
 * @date 2016年7月8日下午3:03:59
 */
@SuppressWarnings("unchecked")
@Repository("unsafeConditionDao")
public class UnsafeConditionDaoImpl extends BaseDaoHibernate5<UnsafeCondition> implements UnsafeConditionDao {
	@Override
	public UnsafeCondition getByInconformityItemSn(String inconformityItemSn) {
		Query query=getSessionFactory().getCurrentSession()
				.createQuery("select i from UnsafeCondition i where inconformityItemSn=:inconformityItemSn")
				.setString("inconformityItemSn", inconformityItemSn);
		return (UnsafeCondition)query.uniqueResult();
	}

	@Override
	public long count(String hql) {
		return((BigInteger)getSessionFactory().getCurrentSession().createSQLQuery(hql)
				.uniqueResult()).longValue();
	}
	//hql查询总数
	@Override
	public long countHql(String hql) {
		return(Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}
	@Override
	public void deleteByIds(String ids) {
		String hql="update UnsafeCondition i SET i.deleted=true,i.periodicalCheck=null,i.specialCheck=null,i.systemAudit=null WHERE i.id in("+ids+")";
		getSessionFactory().getCurrentSession()
		.createQuery(hql).executeUpdate();
	}
	
	@Override
	public List<UnsafeCondition> query(String sql, int page, int rows) {
		Query query=getSessionFactory().getCurrentSession()
				.createSQLQuery(sql).addEntity(UnsafeCondition.class)
				.setFirstResult((page-1) * rows)
				.setMaxResults(rows);
		return (List<UnsafeCondition>)query.list();		
	}
	//根据条件查询隐患数目
	@Override
	public long query(String departmentSn,String departmentTypeSn, String specialitySnIndex,String inconformityLevelSnIndex,String begin, String end) {		
		Query query;
		StringBuffer hqls = new StringBuffer();		
	    hqls.append("select COUNT(*) FROM UnsafeCondition i WHERE i.deleted=false and i.checkedDepartment.departmentSn like '"+departmentSn+"%' and i.checkDateTime>'"+begin+"' and i.checkDateTime<'"+end+"'");
	    if(departmentTypeSn!=null && departmentTypeSn.length()>0){
		   hqls.append(" AND i.checkedDepartment.departmentType.departmentTypeSn in "+departmentTypeSn);
	    }
	    if(specialitySnIndex!=null&&specialitySnIndex.length()>0){
			hqls.append(" AND i.speciality.specialitySn='"+specialitySnIndex+"'");	
		 }	
		if(inconformityLevelSnIndex!=null&&inconformityLevelSnIndex.length()>0){			  
			hqls.append(" AND i.inconformityLevel='"+inconformityLevelSnIndex+"'");	
		}			
		String hql=hqls.toString();
		query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (Long)query.uniqueResult();
	}
	@Override
	public List<?> getBySql(String sql) {
		Query query=getSessionFactory().getCurrentSession().createSQLQuery(sql);
		//System.out.println(query.list());
		return query.list();
	}

	@Override
	public UnsafeCondition getById(int id) {
		String hql="select u from UnsafeCondition u where u.id=:id";
		return (UnsafeCondition) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setParameter("id", id)
				.uniqueResult();
	}

	@Override
	public void computeNowRiskLevel() {
		String hql="select u from UnsafeCondition u where u.deleted !=true and u.hasCorrectFinished !=true";
		List<UnsafeCondition> unsafeConditions=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
													.createQuery(hql).list();
		UnsafeCondition unsafeCondition=null;
		for(UnsafeCondition un:unsafeConditions){
			unsafeCondition=new UnsafeCondition();
			unsafeCondition=this.checkUnsafeCondition(un);
			this.update(unsafeCondition);
		}
	}
	//根据隐患判断是否超期
	public UnsafeCondition checkUnsafeCondition(UnsafeCondition un){
		Date nowTime=new Date();
		Long oldTime = null;
		if(un.getCorrectDeadline()!=null && !"".equals(un.getCorrectDeadline())){
			oldTime=un.getCorrectDeadline().getTime();
		}else{
			oldTime=un.getCheckDateTime().getTime()+8*60*60*1000;
		}
		Long time=(nowTime.getTime()-oldTime)/(60*60*1000);
		if(un.getInconformityLevel()!=null){
			if(un.getInconformityLevel().toString()=="一般不符合项"){
				if(time<=8){
					un.setCurrentRiskLevel(RiskLevel.中等风险);
				}else{
					un.setCurrentRiskLevel(RiskLevel.重大风险);
				}
			}else if(un.getInconformityLevel().toString()=="严重不符合项"){
				un.setCurrentRiskLevel(RiskLevel.重大风险);
			}else{
				if(time<8){
					un.setCurrentRiskLevel(RiskLevel.一般风险);
				}else if(time>=8 && time<=24){
					un.setCurrentRiskLevel(RiskLevel.中等风险);
				}else{
					un.setCurrentRiskLevel(RiskLevel.重大风险);
				}
			}
		}else{
			if(time<8){
				un.setCurrentRiskLevel(RiskLevel.一般风险);
			}else if(time>=8 && time<=24){
				un.setCurrentRiskLevel(RiskLevel.中等风险);
			}else{
				un.setCurrentRiskLevel(RiskLevel.重大风险);
			}
		}
		return un;
	}

	@Override
	public Map<String, Object> showData(String checkDeptSn,String departmentSn,String str,String indexSn,
				String standardSn,String inconformityLevel,String qSpecialitySn,
				String riskLevel,String checkType,String checkerFrom,String inconformityItemNature,String correctPrincipal,
				String hasCorrectConfirmed,String hasReviewed,String hasCorrectFinished,String timeData,String checkers,
				String pag,int page,int rows,Timestamp beginTime,Timestamp endTime, boolean checked) {
			JSONArray jsonArray=new JSONArray();
			Long total=0l;
			String hql=this.hql(checkDeptSn,departmentSn, str, indexSn, standardSn, inconformityLevel,
					  qSpecialitySn, riskLevel, checkType, checkerFrom, inconformityItemNature,
					  correctPrincipal, hasCorrectConfirmed, hasReviewed, hasCorrectFinished,
					  timeData, checkers, pag, page, rows, beginTime, endTime, checked);
			//查询总数的hql语句
			String countHql = hql.replaceFirst("distinct i", "count(distinct i.inconformityItemSn)");
			  
			List<UnsafeCondition> lists=new ArrayList<UnsafeCondition>();
			if(page != 0 && !"".equals(page) && rows != 0 && !"".equals(rows)){
				lists=this.findByPage(hql, page, rows);
			}else{
				lists=this.find(hql);
			}
			total=this.countHql(countHql);
			jsonArray = this.listToJSONArray(lists);
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", total);// total键 存放总记录数，必须的
	        json.put("rows", jsonArray);// rows键 存放每页记录 list
	        return json;
	}
	@Override
	public Map<String, Object> myUnsafeCondition(String personId, int page, int rows) {
		String hql = "SELECT DISTINCT u FROM UnsafeCondition u LEFT JOIN u.checkers c"
				+ " WHERE u.deleted!=true AND (u.editor.personId = '" + personId +"'"
				+ " OR u.correctPrincipal.personId = '" + personId +"' OR c.personId = '" + personId +"')"
				+ " ORDER BY u.checkDateTime desc";
		String countHql = hql.replaceFirst("DISTINCT u", "COUNT(DISTINCT u.inconformityItemSn)");
		
		Long total = this.countHql(countHql);
		
		List<UnsafeCondition> lists = this.findByPage(hql, page, rows);
		JSONArray jsonArray = this.listToJSONArray(lists);
		
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", total);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
		
		return json;
	}
	/**
	 * 将list转化为jsonArray
	 * @param lists
	 * @return
	 */
	public JSONArray listToJSONArray(List<UnsafeCondition> lists){
			JSONArray jsonArray = new JSONArray();
			for(UnsafeCondition inc:lists){
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
	  			  }else{
	  				  jo.put("checkers", "");
	  			  }
	  		  jo.put("id", inc.getId());
	  		  jo.put("checkType", inc.getCheckType());
	  		  jo.put("checkerFrom", inc.getCheckerFrom());
	  		  jo.put("inconformityItemSn", inc.getInconformityItemSn());
	  		  if(inc.getCheckDateTime()!=null){
	  			  jo.put("checkDateTime", inc.getCheckDateTime().toString());
	  		  }else{
	  			  jo.put("checkDateTime", inc.getCheckDateTime());
	  		  }
	  		  if(inc.getEditorDateTime() != null){
	  			  jo.put("editorDateTime", inc.getEditorDateTime().toString());
	  		  }else{
	  			  jo.put("editorDateTime", inc.getEditorDateTime());
	  		  }
	  		  if(inc.getConfirmTime() != null){
	  			  jo.put("confirmTime", inc.getConfirmTime().toString());
	  		  }else{
	  			  jo.put("confirmTime", inc.getConfirmTime());
	  		  }
	  		  jo.put("checkLocation", inc.getCheckLocation());
	  		  jo.put("inconformityItemNature", inc.getInconformityItemNature());
	  		  if(inc.getMachine()!=null){
	  			  jo.put("machine", inc.getMachine().getManageObjectName());
	  		  }else{
	  			  jo.put("machine", inc.getMachine());
	  		  }
	  		  if(inc.getCheckedDepartment()!=null){
	  			  jo.put("checkedDepartment", inc.getCheckedDepartment().getDepartmentName());
	  			  jo.put("checkedDepartmentImplType", inc.getCheckedDepartment().getImplDepartmentName());
	  		  }else{
	  			  jo.put("checkedDepartment", inc.getCheckedDepartment());
	  			  jo.put("checkedDepartmentImplType", "");
	  		  }
	  		  if(inc.getStandardIndex()!=null){
	  			  jo.put("standardIndex", inc.getStandardIndex().getIndexName());
	  		  }else{
	  			  jo.put("standardIndex", inc.getStandardIndex());
	  		  }
	  		  if(inc.getSystemAudit()!=null){
	  			  jo.put("systemAudit",inc.getSystemAudit().getSystemAuditType());
	  		  }else{
	  			  jo.put("systemAudit","");
	  		  }
	  		  jo.put("problemDescription", inc.getProblemDescription());
	  		  jo.put("deductPoints", inc.getDeductPoints());
	  		  jo.put("correctType", inc.getCorrectType());
	  		  if(inc.getCorrectDeadline()!=null){
	  			  jo.put("correctDeadline", inc.getCorrectDeadline().toString());
	  		  }else{
	  			  jo.put("correctDeadline", inc.getCorrectDeadline());
	  		  }
	  		  if(inc.getHazrd()!=null){
	  			  jo.put("hazrd", inc.getHazrd().getHazardDescription());
	  		  }else{
	  			  jo.put("hazrd", inc.getHazrd());
	  		  }
	  		  if(inc.getSpeciality()!=null){
	  			  jo.put("speciality", inc.getSpeciality().getSpecialityName());
	  		  }else{
	  			  jo.put("speciality", inc.getSpeciality());
	  		  }
	  		  jo.put("inconformityLevel", inc.getInconformityLevel());
	  		  if(inc.getCurrentRiskLevel()!=null){
	  			  jo.put("riskLevel", inc.getCurrentRiskLevel().toString());
	  		  }else{
	  			  jo.put("riskLevel", "");
	  		  }
	  		  if(inc.getCorrectPrincipal()!=null){
	  			  jo.put("correctPrincipal", inc.getCorrectPrincipal().getPersonName());
	  		  }else{
	  			  jo.put("correctPrincipal", inc.getCorrectPrincipal());
	  		  }
	  		  jo.put("correctProposal", inc.getCorrectProposal());
	  		  if(inc.getCurrentRiskLevel()!=null){
	  			  jo.put("currentRiskLevel", inc.getCurrentRiskLevel().toString());
	  		  }else{
	  			  jo.put("currentRiskLevel", "");
	  		  }
	  		  jo.put("attachments", inc.getAttachments().size());
	  		  if(inc.getEditor()!=null){
				  jo.put("editor", inc.getEditor().getPersonName());
				  jo.put("editorId", inc.getEditor().getPersonId());
			  }else{
				  jo.put("editor", "无");
				  jo.put("editorId", "无");
			  }
	  		  if(inc.getHasCorrectConfirmed()!=null){
	  			  if(inc.getHasCorrectConfirmed()!=true){
		    			  jo.put("hasCorrectConfirmed", "未整改确认");
		    		  }else{
		    			  jo.put("hasCorrectConfirmed", "已整改确认");
		    		  }
	  		  }else{
	  			  jo.put("hasCorrectConfirmed", "未整改确认");
	  		  }
	  		  if(inc.getHasReviewed()!=null){
	  			  if(inc.getHasReviewed()!=true){
		    			  jo.put("hasReviewed","未复查");
		    		  }else{
		    			  jo.put("hasReviewed","已复查");
		    		  }
	  		  }else{
	  			  jo.put("hasReviewed","未复查");
	  		  }
	  		  if(inc.getHasCorrectFinished()!=null){
	  			  if(inc.getHasCorrectFinished()!=true){
		    			  jo.put("hasCorrectFinished", "未整改完成");
		    		  }else{
		    			  jo.put("hasCorrectFinished", "已整改完成");
		    		  }
	  		  }else{
	  			  jo.put("hasCorrectFinished", "未整改完成");
	  		  }
	  		  jsonArray.add(jo);
	  	  }
		  return jsonArray;
	}
	public String hql(String checkDeptSn, String departmentSn, String str, String indexSn, String standardSn,
			String inconformityLevel, String qSpecialitySn, String riskLevel, String checkType, String checkerFrom,
			String inconformityItemNature, String correctPrincipal, String hasCorrectConfirmed, String hasReviewed,
			String hasCorrectFinished, String timeData, String checkers, String pag,int page,int rows,
			Timestamp beginTime, Timestamp endTime, boolean checked){
		  String ksTime="";
	      String jsTime="";
    	  //拼接hql语句
    	  StringBuffer hqll = new StringBuffer();
    	  //隐患查询与展示个人的区别
		  hqll.append("select distinct i from UnsafeCondition i where i.deleted=false");
    	  //判断部门类型是否为空来判断是否选择部门类型
    	  if(str!=null && !"".equals(str)){
    		  hqll.append(" AND ("+str+")");
    	  }else{
    		  hqll.append(" AND i.checkedDepartment.departmentSn like '"+departmentSn+"%'");
    	  }
    	  //根据A.*找出下面所有的指标and符合标准编号
    	  if(indexSn !=null && !"".equals(indexSn) && standardSn!=null && !"".equals(standardSn)){
    		  hqll.append(" AND (i.standardIndex.indexSn like '"+indexSn+".%' or i.standardIndex.indexSn ='"+indexSn+"') and i.standardIndex.standard.standardSn='"+standardSn+"'");
    		  if( checked ) hqll.append(" AND i.standardIndex.isKeyIndex = true");
    	  }
    	  //根据检查部门
    	  if(checkDeptSn != null && !"".equals(checkDeptSn)){
    		  hqll.replace(hqll.indexOf("UnsafeCondition"), hqll.indexOf("where"), " UnsafeCondition i LEFT JOIN i.checkers c ");
    		  hqll.append(" AND c.department.departmentSn like '" + checkDeptSn + "%'");// AND c.deleted = false  OR i.editor.department.departmentSn like '" + checkDeptSn + "%')
    	  }
    	  //不符合项等级
    	  if(inconformityLevel !=null && !"".equals(inconformityLevel)){
    		  int incon=Integer.parseInt(inconformityLevel);
    		  hqll.append(" AND i.inconformityLevel='"+incon+"'");
    	  }
    	  //专业
    	  if(qSpecialitySn !=null && !"".equals(qSpecialitySn)){
    		  String asum="(";
    		  String[] array = qSpecialitySn.split("##");
    		  for(String a:array){
    			  asum=asum+"'"+a+"',";
    		  }
    		  asum=asum.substring(0, asum.length()-1);
    		  asum+=")";
    		  hqll.append(" AND i.speciality.specialitySn in "+asum);
    	  }
    	  //现风险等级
    	  if(riskLevel !=null && !"".equals(riskLevel)){
    		  int risk=Integer.parseInt(riskLevel);
    		  hqll.append(" AND i.currentRiskLevel='"+risk+"'");
    	  }
    	  //检查类型  
    	  if(checkType !=null && !"".equals(checkType)){
    		  int checktype=Integer.parseInt(checkType);
    		  hqll.append(" AND i.checkType='"+checktype+"'");
    	  }
    	  //检查人来自
    	  if(checkerFrom !=null && !"".equals(checkerFrom)){
    		  int checkerfrom=Integer.parseInt(checkerFrom);
    		  hqll.append(" AND i.checkerFrom='"+checkerfrom+"'");
    	  }
    	  //不符合项性质
    	  if(inconformityItemNature !=null && !"".equals(inconformityItemNature)){
    		  int inconformityitemNature=Integer.parseInt(inconformityItemNature);
    		  hqll.append(" AND i.inconformityItemNature='"+inconformityitemNature+"'");
    	  }
    	  //整改负责人 correctPrincipal
    	  if(correctPrincipal!=null && !"".equals(correctPrincipal)){
    		  hqll.append(" AND i.correctPrincipal.personId='"+correctPrincipal+"'");
    	  }
    	  //已整改确认
    	  if(hasCorrectConfirmed!=null && !"".equals(hasCorrectConfirmed)){
    		  hqll.append(" AND i.hasCorrectConfirmed="+hasCorrectConfirmed);
    	  }
    	  //已复查
    	  if(hasReviewed!=null && !"".equals(hasReviewed)){
    		  hqll.append(" AND i.hasReviewed="+hasReviewed);
    	  }
    	  //已整改完成
    	  if(hasCorrectFinished!=null && !"".equals(hasCorrectFinished)){
    		  hqll.append(" AND i.hasCorrectFinished="+hasCorrectFinished);
    	  }
    	  //时间
    	  if(timeData!=null&&timeData.trim().length()>0){
    		  
    		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		  Calendar cal =Calendar.getInstance();
    		  Calendar cal2 =Calendar.getInstance();
    		  
    		  if(timeData.equals("today")){
    			  
    			  ksTime=df.format(new Date());//当前日期
    			  hqll.append(" AND i.checkDateTime between '"+ksTime+" 00:00:00' and '"+ksTime+" 23:59:59'");
    			  
    		  }else if(timeData.equals("week")){
    			  //周日到周六
//	    	  cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);	 	//这种输出的是周日的日期，因为外国把周日当成第一天
//            ksTime=df.format(cal.getTime());
//            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);		//周六的日期
//		      jsTime=df.format(cal.getTime());
    			  
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
    		  String stra="1!=1";
    		  List<?> unsafeConditionList=this.getBySql("select inconformity_item_sn from inconformity_item_checker where person_id='"+checkers+"'");
    		  if(unsafeConditionList.size()>0){
    			  stra="(";
    			  for(Object un:unsafeConditionList){
    				  stra=stra+"'"+un+"',";
    			  }
    			  stra=stra.substring(0, stra.length()-1);
    			  stra+=")";
    			  hqll.append(" AND i.inconformityItemSn in "+stra);
    		  }else{
    			  hqll.append(" AND "+stra);
    		  }
    	  }
    	  //隐患查询与预警查询的区别
    	  if(pag!=null){
    		  hqll.append(" AND i.hasCorrectFinished!="+true);
    	  }
    	  hqll.append(" order by i.checkDateTime desc");
    	  
    	  //查询数据的hql语句
    	  String hql=hqll.toString();
    	  return hql;
	}

	@Override
	public List<UnsafeCondition> findByHql(String hql) {
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return query.list();
	}

	@Override
	public List<InconformityItem> findInconformityItemByHql(String hql) {
		return getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
	}

	@Override
	public List<InconformityItem> findInconformityItemByPage(String hql, int page, int rows) {
		return getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setFirstResult((page - 1) * rows)//设置每页起始的记录编号
				.setMaxResults(rows)//设置需要查询的最大结果集
				.list();
	}

	
}
