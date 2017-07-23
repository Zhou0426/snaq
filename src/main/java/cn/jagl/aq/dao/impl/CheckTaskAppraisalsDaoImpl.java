package cn.jagl.aq.dao.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.CheckTaskAppraisalsDao;
import cn.jagl.aq.domain.CheckTaskAppraisals;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.PersonRecord;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.UnsafeCondition;

@Repository("checkTaskAppraisalsDao")
public class CheckTaskAppraisalsDaoImpl extends BaseDaoHibernate5<CheckTaskAppraisals>
		implements CheckTaskAppraisalsDao {

	@Override
	public long countHql(String hql) {
		return (Long)getSessionFactory().getCurrentSession().createQuery(hql)
				.uniqueResult();
	}

	@Override
	public CheckTaskAppraisals getBycheckerSn(String checkerId, String departmentSn,String times) {
		String hql="select c from CheckTaskAppraisals c where c.checker.personId='" + checkerId + "' and c.department.departmentSn = '" + departmentSn + "' and c.yearMonth = '"+times+"'";
		Query query=getSessionFactory().getCurrentSession().createQuery(hql);
		return (CheckTaskAppraisals)query.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CheckTaskAppraisals> getByYear(String times, String departmentSn) {
		String hql = "select c from CheckTaskAppraisals c where year(yearMonth) = '" + times + "' and c.department.departmentSn = '" + departmentSn + "' and c.checker is not null";
		List<CheckTaskAppraisals> list = getSessionFactory().getCurrentSession()
											.createQuery(hql).list();
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		CheckTaskAppraisals cta = null;
		for(CheckTaskAppraisals checkTaskAppraisals:list){
			if(map.containsKey(checkTaskAppraisals.getYearMonth().getMonth().getValue())){
				cta = (CheckTaskAppraisals) map.get(checkTaskAppraisals.getYearMonth().getMonth().getValue());
				if(checkTaskAppraisals.getCheckTaskCount() != null){
					cta.setCheckTaskCount(cta.getCheckTaskCount() + checkTaskAppraisals.getCheckTaskCount());
				}
				if(checkTaskAppraisals.getRealCheckCount() != null){
					cta.setRealCheckCount(cta.getRealCheckCount() + checkTaskAppraisals.getRealCheckCount());
				}
				map.put(checkTaskAppraisals.getYearMonth().getMonth().getValue(), cta);
			}else{
				cta = new CheckTaskAppraisals();
				cta.setYearMonth(checkTaskAppraisals.getYearMonth());
				cta.setDepartment(checkTaskAppraisals.getDepartment());
				if(checkTaskAppraisals.getCheckTaskCount() != null){
					cta.setCheckTaskCount(checkTaskAppraisals.getCheckTaskCount());
				}else{
					cta.setCheckTaskCount(0);
				}
				if(checkTaskAppraisals.getRealCheckCount() != null){
					cta.setRealCheckCount(checkTaskAppraisals.getRealCheckCount());
				}else{
					cta.setRealCheckCount(0f);
				}
				map.put(checkTaskAppraisals.getYearMonth().getMonth().getValue(), cta);
			}
		}
		List<CheckTaskAppraisals> list2 = new ArrayList<CheckTaskAppraisals>();
		for (Entry<Integer, Object> entry : map.entrySet()) {
			list2.add((CheckTaskAppraisals) entry.getValue());
		}
		return list2;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CheckTaskAppraisals> getByHql(String hql) {
		return (List<CheckTaskAppraisals>)getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
	}
	//定时计算检查任务
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public void checkTask(){
		String ksTime="";
		String jsTime="";
		String hql="";
		//每天定时检查的时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		//String yesterday = df.format(cal.getTime());//昨天的日期
		cal.set(cal.DAY_OF_MONTH, 1);
		ksTime=df.format(cal.getTime());//昨天月份的一号
		cal.set(cal.DAY_OF_MONTH, cal.getActualMaximum(cal.DAY_OF_MONTH)); 
		jsTime=df.format(cal.getTime());//昨天月份的最后一号
		checkTaskImpl(ksTime,jsTime);
		//判断是否有为true的记录，然后再计算
		hql="select c from CheckTaskAppraisals c where c.needComputing=true";
		List<CheckTaskAppraisals> list=(List<CheckTaskAppraisals>) getSessionFactory().getCurrentSession()
													.createQuery(hql).list();
		if(list.size()>0){
			for(CheckTaskAppraisals check:list){
				ksTime=check.getYearMonth().toString();
				jsTime=check.getYearMonth().withDayOfMonth(check.getYearMonth().lengthOfMonth()).toString();
				checkTaskImpl(ksTime,jsTime);
			}
		}
	}
	//定时计算检查任务实现
	@SuppressWarnings({ "unchecked" })
	public void checkTaskImpl(String ksTime,String jsTime) {
		//查询部门
		String hql="select d from Department d where d.departmentType.departmentTypeSn='JTCS'";
		List<Department> departments=(List<Department>) getSessionFactory().getCurrentSession()
										.createQuery(hql).list();
		//遍历部门
		for(Department de:departments){
			//判断该部门是否有数据
			this.copyRecordByDepartmentSn(de.getDepartmentSn());
			//根据部门编号和时间段，去履历表中查询该时间段内在此部门的所有人员
			hql="select distinct i from Person i LEFT JOIN i.records p where p.department.departmentSn='"+de.getDepartmentSn()+"' AND NOT date(p.startDateTime)>'"+jsTime+"' AND (NOT date(p.endDateTime)<'"+ksTime+"' or p.endDateTime is null)";
			List<Person> persons=(List<Person>) getSessionFactory().getCurrentSession()
												.createQuery(hql).list();
			for(Person pe:persons){
				//根据人员，部门和时间查询在本时间段内该人员在此部门呆过的时间段记录
				hql="select p from PersonRecord p where p.person.personId='"+pe.getPersonId()+"' and p.department.departmentSn='"+de.getDepartmentSn()+"' AND NOT date(startDateTime)>'"+jsTime+"' AND ((NOT date(endDateTime)<'"+ksTime+"') OR endDateTime is null)";
				List<PersonRecord> personRecords=(List<PersonRecord>) getSessionFactory().getCurrentSession()
										.createQuery(hql).list();
				try {
					//比较起止时间与查询的时间段，并求出该时间段内的不符合项
					String[] str;
					hql="SELECT distinct u FROM UnsafeCondition u LEFT JOIN u.checkers c where u.deleted=false and c.personId='"+pe.getPersonId()+"' AND (";
					for(PersonRecord personRecord:personRecords){
						str=this.unionTime(personRecord.getStartDateTime(), personRecord.getEndDateTime()).split("#");
						hql=hql+" u.checkDateTime between '"+str[0]+" 00:00:00' and '"+str[1]+" 23:59:59' or";
					}
					hql=hql.substring(0, hql.length()-2)+")";
				}catch (ParseException e) {
					e.printStackTrace();
				}
				List<UnsafeCondition> list=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
												.createQuery(hql).list();
				//遍历不符合项
				Float num=0f;
				if(list.size()>0){
					for(UnsafeCondition un:list){
						//检查成员存在
						if(un.getCheckers().size()>1){
							num+=((float)1/(float)(un.getCheckers().size()));
						}else{
							num++;
						}
					}
					boolean judge = true;
					int i = 0;
					while(judge && i < 2){
						try{
							//根据查询时间，人员id和部门编号查询是否已有记录
							hql="select c from CheckTaskAppraisals c where c.checker.personId='"+pe.getPersonId()+"' and c.department.departmentSn='"+de.getDepartmentSn()+"' and c.yearMonth='"+ksTime+"'";
							CheckTaskAppraisals checkTaskAppraisals=(CheckTaskAppraisals)getSessionFactory().getCurrentSession().createQuery(hql).setMaxResults(1).uniqueResult();
							//如果已有数据，则更新数据
							if(checkTaskAppraisals!=null){
								checkTaskAppraisals.setNeedComputing(false);
								checkTaskAppraisals.setComputeDatetime(LocalDateTime.now());
//								if(checkTaskAppraisals.getRealCheckCount()!=null){
//									num=num+checkTaskAppraisals.getRealCheckCount();
//								}
								checkTaskAppraisals.setRealCheckCount(num);
								this.update(checkTaskAppraisals);
							}else{
								//根据数据查询该人员在上月的任务量
								Integer in=this.getCheckNum(ksTime, pe.getPersonId(), de.getDepartmentSn());
								if(in!=null){
									checkTaskAppraisals=new CheckTaskAppraisals();
									checkTaskAppraisals.setYearMonth(LocalDate.now().minusDays(1).withDayOfMonth(1));
									checkTaskAppraisals.setDepartment(de);
									checkTaskAppraisals.setChecker(pe);
									checkTaskAppraisals.setRealCheckCount(num);
									checkTaskAppraisals.setNeedComputing(false);
									checkTaskAppraisals.setComputeDatetime(LocalDateTime.now());
									checkTaskAppraisals.setCheckTaskCount(in);
									this.save(checkTaskAppraisals);
								}
							}
							judge = false;
							i++;
						}catch(Exception e){
							judge = true;
							i++;
						}
					}
				}
			}
			//本部门在本次履历表查询人员循环结束
			//查询本部门在本月的所有人员记录
			hql="select c from CheckTaskAppraisals c where c.department.departmentSn='"+de.getDepartmentSn()+"' and c.checker is not null and c.yearMonth='"+ksTime+"'";
			List<CheckTaskAppraisals> list2=(List<CheckTaskAppraisals>)getSessionFactory().getCurrentSession() 
												.createQuery(hql).list();
			Integer countNum=0;
			Float realNum=0f;
			for(CheckTaskAppraisals ch:list2){
				if(ch.getCheckTaskCount()!=null){
					countNum+=ch.getCheckTaskCount();
				}
				if(ch.getRealCheckCount()!=null){
					realNum+=ch.getRealCheckCount();
				}
			}
			boolean judge = true;
			int i = 0;
			while(judge && i < 2){
				try{
					//查询本部门在本月的总记录
					hql="select c from CheckTaskAppraisals c where c.department.departmentSn='"+de.getDepartmentSn()+"' and c.checker is null and c.yearMonth='"+ksTime+"'";
					CheckTaskAppraisals checkTaskAppraisalses=(CheckTaskAppraisals)getSessionFactory().getCurrentSession()
							.createQuery(hql).setMaxResults(1).uniqueResult();
					if(checkTaskAppraisalses==null){
						checkTaskAppraisalses=new CheckTaskAppraisals();
						checkTaskAppraisalses.setCheckTaskCount(countNum);
						checkTaskAppraisalses.setRealCheckCount(realNum);
						checkTaskAppraisalses.setDepartment(de);
						checkTaskAppraisalses.setNeedComputing(false);
						checkTaskAppraisalses.setYearMonth(LocalDate.now().minusDays(1).withDayOfMonth(1));
						checkTaskAppraisalses.setComputeDatetime(LocalDateTime.now());
						this.save(checkTaskAppraisalses);
					}else{
						checkTaskAppraisalses.setCheckTaskCount(countNum);
						checkTaskAppraisalses.setRealCheckCount(realNum);
						checkTaskAppraisalses.setNeedComputing(false);
						checkTaskAppraisalses.setComputeDatetime(LocalDateTime.now());
						this.update(checkTaskAppraisalses);
					}
					judge = false;
					i++;
				}catch(Exception e){
					judge = true;
					i++;
				}
			}
		}
	}
	//根据部门和当月的时间段，求出本月时间本部门所扣指标的分值
	@SuppressWarnings("unchecked")
	@Override
	public float countStandardIndexScore(String departmentSn,String ksTime,String jsTime){
		//总扣分
		float descore=0f;
		//存放往上扣分时的扣分指标、分数
		HashMap<StandardIndex,Float> map=new HashMap<StandardIndex,Float>();
		//存放联合指标（标识码、分数）
		HashMap<String,Float> jointMap=new HashMap<String,Float>();
		//除联合指标之外和关键指标下的扣分的其他指标
		HashMap<StandardIndex,Float> other=new HashMap<StandardIndex,Float>();
		//查询本次扣分所有的隐患
		//String hql2="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')";			
		String hql2="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and (u.checkDateTime between '"+ksTime+" 00:00:00' and '"+jsTime+" 23:59:59')";//u.hasCorrectConfirmed=false
		List<UnsafeCondition> list2=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
				.createQuery(hql2).list();
		
		/**
		 * 循环隐患
		 * 作用：
		 * 1、将所有的往上扣分指标以及所扣分数放入map中
		 * 2、筛选所有联合指标
		 */
		for(UnsafeCondition unsafeCondition:list2){
			StandardIndex index=unsafeCondition.getStandardIndex();
			if(index!=null){
				if(index.getIsKeyIndex()!=null && index.getIsKeyIndex()==true){
					for(StandardIndexAuditMethod standardIndexAuditMethod:index.getAuditMethods()){
						StandardIndex indexDeducted=standardIndexAuditMethod.getIndexDeducted();
						if(indexDeducted!=null){
							if(map.containsKey(indexDeducted)){
								float s=map.get(indexDeducted);
								if(indexDeducted.getPercentageScore()!=null&&unsafeCondition.getDeductPoints()!=null&&indexDeducted.getPercentageScore()<(unsafeCondition.getDeductPoints()+s)){
									map.put(indexDeducted,indexDeducted.getPercentageScore());
								}else{
									map.put(indexDeducted,unsafeCondition.getDeductPoints()+s);
								}
							}else{
								if(indexDeducted.getPercentageScore()!=null&&unsafeCondition.getDeductPoints()!=null&&indexDeducted.getPercentageScore()<unsafeCondition.getDeductPoints()){
									map.put(indexDeducted,indexDeducted.getPercentageScore());
								}else{
									map.put(indexDeducted,unsafeCondition.getDeductPoints());
								}
							}
						}
					}
				}else if(index.getJointIndexIdCode()!=null && index.getJointIndexIdCode().trim().length()>0){
					int count=0;
					for(Entry<String,Integer> am:unsafeCondition.getAuditMethods().entrySet()){
						//获取出现次数
						count+=am.getValue();
					}
					//分数存入map(已经判断是否超分)
					if(jointMap.containsKey(index.getJointIndexIdCode().trim())){
						float oldscore=jointMap.get(index.getJointIndexIdCode().trim());							
						if(index.getAnDeduction()!=null && index.getPercentageScore()!=null){
							if((oldscore+count*index.getAnDeduction())>index.getPercentageScore()){
								jointMap.put(index.getJointIndexIdCode().trim(), index.getPercentageScore());
							}else{
								jointMap.put(index.getJointIndexIdCode().trim(), oldscore+count*index.getAnDeduction());
							}								
						}
					}else{
						if(index.getAnDeduction()!=null){
							if(index.getZeroTimes()!=null){
								if(index.getZeroTimes()>count){
									jointMap.put(index.getJointIndexIdCode().trim(), count*index.getAnDeduction());
								}else if(index.getPercentageScore()!=null){
									jointMap.put(index.getJointIndexIdCode().trim(), index.getPercentageScore());
								}else{
									jointMap.put(index.getJointIndexIdCode().trim(), count*index.getAnDeduction());
								}
							}else if(index.getPercentageScore()!=null){
								if(count*index.getAnDeduction()>index.getPercentageScore()){
									jointMap.put(index.getJointIndexIdCode().trim(), index.getPercentageScore());
								}else{
									jointMap.put(index.getJointIndexIdCode().trim(), count*index.getAnDeduction());
								}
							}
						}else{
							jointMap.put(index.getJointIndexIdCode().trim(), 0f);
						}
						
					}
				}
			}
			
		}
		//判断共享指标是否属于往上扣分的关键指标,如果属于则并入map
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			for(Entry<String,Float> bm:jointMap.entrySet()){
				if(bm.getKey().contains(am.getKey().getIndexSn())){
					//如果包含
					if(am.getKey().getPercentageScore()!=null){
						if((am.getValue()+bm.getValue())>am.getKey().getPercentageScore()){
							am.setValue(am.getKey().getPercentageScore());
						}else{
							am.setValue(am.getValue()+bm.getValue());
						}
					}else{
						am.setValue(am.getValue()+bm.getValue());
					}
					
					bm.setValue(0f);
				}
			}
		}
		
		//针对关键指标及其同级打分开始
		for(UnsafeCondition unsafeCondition:list2){
			StandardIndex index=unsafeCondition.getStandardIndex();
			if(index!=null && index.getJointIndexIdCode()==null &&(index.getIsKeyIndex()==null || index.getIsKeyIndex()==false)){
				if(other.containsKey(index)){
					float old=other.get(index);
					float news=deduction(unsafeCondition,index)+old;
					if(index.getPercentageScore()!=null && news>index.getPercentageScore()){
						other.put(index, index.getPercentageScore());
					}else{
						other.put(index, news);
					}
				}else{
					other.put(index, deduction(unsafeCondition,index));
				}
				for(Entry<StandardIndex,Float> am:map.entrySet()){
					if(index.getIndexSn().contains(am.getKey().getIndexSn())){
						float samescore=am.getValue()+deduction(unsafeCondition,index);
						if(am.getKey().getPercentageScore()!=null&&samescore>am.getKey().getPercentageScore()){
							am.setValue(am.getKey().getPercentageScore());
						}else{
							am.setValue(samescore);
						}
						if(other.containsKey(index)){
							other.remove(index);
						}
					}
				}
			}
		}
		//计算map总扣分
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			descore+=am.getValue();
		}
		for(Entry<String,Float> bm:jointMap.entrySet()){
			descore+=bm.getValue();
		}
		for(Entry<StandardIndex,Float> cm:other.entrySet()){
			descore+=cm.getValue();
		}	
		return descore;
	}
	//计算扣分
		public float deduction(UnsafeCondition unsafeCondition,StandardIndex index){
			float deduction=0f;
			if(index.getAnDeduction()!=null){
				int i=0;
				for(Entry<String,Integer> am:unsafeCondition.getAuditMethods().entrySet()){
					i+=am.getValue();
				}
				if(index.getZeroTimes()!=null){
					if(index.getZeroTimes()>i){
						deduction=i*index.getAnDeduction();
					}else if(index.getPercentageScore()!=null){
						deduction=index.getPercentageScore();
					}else{
						deduction=i*index.getAnDeduction();
					}	
				}else if(index.getPercentageScore()!=null){
					if(i*index.getAnDeduction()>index.getPercentageScore()){
						deduction=index.getPercentageScore();
					}else{
						deduction=i*index.getAnDeduction();
					}
				}
			}				
			return deduction;
		}
	//根据人员的起始时间和终止时间，和当月时间的交集
	public String unionTime(LocalDateTime startTime,LocalDateTime endTime) throws ParseException{
		String re="";//返回值
		LocalDate now = LocalDate.now();
		LocalDate ksTime=now.minusDays(1).withDayOfMonth(1);//昨天月份的一号
		LocalDate jsTime=now.minusDays(1).withDayOfMonth(now.minusDays(1).lengthOfMonth());//昨天月份的最后一号
		if(startTime!=null && !"".equals(startTime)){
			if(startTime.toLocalDate().isBefore(ksTime)){
				re+=ksTime;
			}else{
				re+=startTime.toLocalDate();
			}
		}else{
			re+=ksTime;
		}
		if(endTime!=null && !"".equals(endTime)){
			if(endTime.toLocalDate().isBefore(jsTime)){
				re=re+"#"+endTime.toLocalDate();
			}else{
				re=re+"#"+jsTime;
			}
		}else{
			re=re+"#"+jsTime;
		}
		return re;
		
	}
	//根据当月时间和部门判断该月记录是否存在，如果存在则不操作，如果不存在，则复制上月记录，日期改为本月
	@SuppressWarnings("unchecked")
	public void copyRecordByDepartmentSn(String departmentSn){
		LocalDate localDate = LocalDate.now().withDayOfMonth(1);//本月一号
		//根据部门编号和时间查询数据是否存在
		String hql="select c from CheckTaskAppraisals c where c.department.departmentSn= '" + departmentSn + "' and c.yearMonth = '" + localDate + "'";
		List<CheckTaskAppraisals> list = (List<CheckTaskAppraisals>)getSessionFactory().getCurrentSession()
											.createQuery(hql).list();
		//如果不存在，则复制上月的记录至本月
		if(list.size() == 0){
			LocalDate localDate2 = localDate.minusMonths(1);//上月一号
			//根据部门编号和上月的时间数据查询所有记录
			hql = "select c from CheckTaskAppraisals c where c.department.departmentSn = '" + departmentSn + "' and c.yearMonth = '" + localDate2.toString() + "' and c.checker is not null";
			List<CheckTaskAppraisals> list2 = (List<CheckTaskAppraisals>)getSessionFactory().getCurrentSession()
												.createQuery(hql).list();
			CheckTaskAppraisals checkTaskAppraisals =null;
			//Map<String, String> record = new HashMap<String, String>();//记录人员是否重复
			for(CheckTaskAppraisals check:list2){
				//根据人员编号在履历表中查询数据，按起始时间排序取第一个，并且终止时间为空
				hql="select p from PersonRecord p where p.person.personId='"+check.getChecker().getPersonId()+"' order by p.startDateTime DESC";
				PersonRecord personRecord=(PersonRecord)getSessionFactory().getCurrentSession()
											.createQuery(hql).setMaxResults(1).uniqueResult();
				if(personRecord != null && personRecord.getEndDateTime() == null){
					//如果查询到的部门编号等于该部门编号，代表该人员还在本部门
					if(personRecord.getDepartment().getDepartmentSn().equals(departmentSn)){
						hql="select c from CheckTaskAppraisals c where c.department.departmentSn='"+departmentSn+"' and c.checker.personId='"+personRecord.getPerson().getPersonId()+"' and c.yearMonth='"+localDate.toString()+"'";
						List<CheckTaskAppraisals> list3 = (List<CheckTaskAppraisals>)getSessionFactory().getCurrentSession()
															.createQuery(hql).list();
						if(list3.size() == 0){
							checkTaskAppraisals =new CheckTaskAppraisals();
							checkTaskAppraisals.setChecker(check.getChecker());
							checkTaskAppraisals.setYearMonth(localDate);
							checkTaskAppraisals.setDepartment(check.getDepartment());
							checkTaskAppraisals.setCheckTaskCount(check.getCheckTaskCount());
							checkTaskAppraisals.setRealCheckCount(0f);
							checkTaskAppraisals.setNeedComputing(false);
							this.save(checkTaskAppraisals);
						}
					}
				}
			}
		}
	}
	//根据时间，人员id和所在部门查询上月的检查数量（人员刚刚进入部门查询结果为空）
	public Integer getCheckNum(String time,String personId,String departmentSn){
		LocalDate localDate=LocalDate.parse(time);
		localDate=localDate.minusMonths(1);
		String hql="select c from CheckTaskAppraisals c where c.checker.personId='"+personId+"' and c.department.departmentSn='"+departmentSn+"' and c.yearMonth='"+localDate.toString()+"'";
		CheckTaskAppraisals checkTaskAppraisals=(CheckTaskAppraisals)getSessionFactory().getCurrentSession()
													.createQuery(hql).uniqueResult();
		if(checkTaskAppraisals==null){
			return null;
		}
		return checkTaskAppraisals.getCheckTaskCount();
	}


}
