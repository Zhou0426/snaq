
package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.domain.SystemAuditScore;
import cn.jagl.aq.domain.SystemAuditType;
import cn.jagl.aq.domain.UnsafeCondition;

/**
 * @author mahui
 * @method 
 * @date 2016年7月25日下午1:14:13
 */
public class SystemAuditAction extends BaseAction<SystemAudit> {
	private static final long serialVersionUID = 1L;
	private int id;
	private String departmentSn;
	private String personId;
    private Date startDate;
    private Date endDate;
    private SystemAuditType systemAuditType;
	private String editorId;
	private int type;
	private String year;
	private String month;
	private String standardSn;
	private String departmentTypeSn;
	private Map<String,Object> map;
	private InputStream excelStream; 
    private String excelFileName;
	private File excel;
	private String personIds;
	private String personNames;
	private String remark;//备注
	private Float amendScore;//修正分
	private String indexSn;
    private String auditSn;
    private String resourceSn;
    private String strAuditTeamLeader;//审核组长：文本
	private String strCheckers;
	private String excelContentType;
	private String departmentName;
	private String order;
	private String sort;
	
	/**
	 * @method 体系审核隐患导出
	 * @author mahui
	 * @return String
	 */
	public String exportUnsafeCondition(){
		excelStream = systemAuditService.exportUnsafeCondition(auditSn);              	    
        try {
			excelFileName=URLEncoder.encode("隐患导出表.xls","UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "DI";
	}
	//设置不参与打分的项
	public String addOrRemove(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		String hql="select s from StandardIndex s WHERE s.deleted=false and s.standard.standardSn='"+standardSn+"' and (s.indexSn='"+indexSn+"' or s.indexSn like '"+indexSn+".%') and s.integerScore is not null";
		try{
			List<StandardIndex> list=new ArrayList<StandardIndex>();
			list=standardindexService.getPart(hql);
			SystemAudit systemAudit=systemAuditService.getBySn(auditSn);
			if(systemAudit!=null){
				if(type==0){
					for(StandardIndex index:list){
						systemAudit.getNotScoredIndex().add(index);
					}
				}else if(type==1){
					for(StandardIndex index:list){
						systemAudit.getNotScoredIndex().remove(index);
					}
				}
				systemAuditService.update(systemAudit);
			}
		}catch(Exception e){
			map.put("status", "nook");
		}
		return SUCCESS;
	}
	//输出评审打分表
	public String exportS(){
        try {
        	excelStream = systemAuditService.export(standardSn, auditSn);
			excelFileName = URLEncoder.encode("审核打分表-"+departmentName+".xls", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "DI";
	}
	//输出各矿得分
	public String exportSummary(){
		try {
        	excelStream = systemAuditService.exportSummary(year, month, type, standardSn, departmentTypeSn);
			excelFileName = URLEncoder.encode("各矿打分表.xls", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "DI";
	}
	//评审打分
	public String score(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		String hql1="select s from StandardIndex s WHERE s.deleted=false AND parent is null AND s.standard.standardSn='"+standardSn+"'";
		//总分
		int allScore=0;
		for(StandardIndex index:standardindexService.getPart(hql1)){
			if(index.getIntegerScore()!=null){
				allScore+=index.getIntegerScore();
			}
		}
		allScore=allScore-systemAuditService.countNotScore(auditSn);
		//扣分
		float deduction=0;
		//系统计算得分
		float computedScore=100;
		for(SystemAuditScore systemAuditScore:systemAuditScoreService.queryByAuditSn(auditSn)){
			StandardIndex index=systemAuditScore.getStandardIndex();
			
			if(index!=null&&index.getParent()!=null&&systemAuditService.isScore(auditSn, index.getParent().getIndexSn())){
				int partScore=index.getParent().getIntegerScore();
				int com=100-systemAuditScore.getConformDegree();
				String str=index.getIndexSn();
				str=str.substring(str.length()-1);
				switch(str){
				case "P":
					deduction+=partScore*0.2*com*0.01;
					break;
				case "C":
					deduction+=partScore*0.2*com*0.01;
					break;
				case "D":
					deduction+=partScore*0.5*com*0.01;
					break;
				case "A":
					deduction+=partScore*0.1*com*0.01;
					break;
				}
			}
		}
		//开始打分
		computedScore=((allScore-deduction)/allScore)*100;
		SystemAudit systemAudit=systemAuditService.getById(id);
		systemAudit.setComputedScore(computedScore);
		systemAudit.setDesignPoints((float) allScore);
		systemAudit.setRealScore(allScore-deduction);
		if(systemAudit.getAmendScore()!=null){
			if(systemAudit.getAmendScore()+computedScore>100){
				systemAudit.setAmendScore(null);
				systemAudit.setFinalScore(computedScore);
				map.put("status","over");
			}else{
				systemAudit.setFinalScore(systemAudit.getAmendScore()+computedScore);
			}
			
		}else{
			systemAudit.setFinalScore(computedScore);
		}
		try{
			systemAuditService.update(systemAudit);
		}catch(Exception e){
			map.put("status", "nook");
		}
		return SUCCESS;
	}
	//查询
	public void query() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out;  
        out = response.getWriter();
        
		String hql="select a FROM SystemAudit a LEFT JOIN a.standard t WHERE a.deleted=false AND a.systemAuditType="+type;
		
		if(type==1){
			if(month!=null&&month.length()>0&&month.equals("10,11,12")){
				hql="select a FROM SystemAudit a LEFT JOIN a.standard t WHERE a.deleted=false AND ((a.systemAuditType=2"
						+ " AND year(a.startDate) ="+year+" AND month(a.startDate) in (10,11,12)) or ("
						+ "a.systemAuditType=1 AND year(a.checkQuarter) ="+year+" AND month(a.checkQuarter) in (10,11,12)))";
	        }else{
	        	if(year!=null&&year.length()>0){
					hql+=" AND year(a.checkQuarter) ="+year;
				}
				if(month!=null&&month.length()>0){
					hql+=" AND month(a.checkQuarter) in ("+month+")";
				}
	        }
		}else{
			if(year!=null&&year.length()>0){
				hql+=" AND year(a.startDate) ="+year;
			}
			if(month!=null&&month.length()>0){
				hql+=" AND month(a.startDate) in ("+month+")";
			}
		}
		
		
		if(standardSn!=null&&standardSn.length()>0){
			hql+=" AND t.standardSn like '"+standardSn+"'";
		}else{
			if(departmentTypeSn!=null&&departmentTypeSn.length()>0){
				hql+=" AND t.departmentType.departmentTypeSn like '"+departmentTypeSn+"'";
			}
		}
		if(sort!=null && sort.trim().length()>0){
			hql+=" order by a."+sort+" "+order;
		}
		//
		if(type==0){
			List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
			Department department;
	    	if(departmentTypes.size()>0){
	    		department=departmentService.getByDepartmentSn(departmentSn);
	    	}else{
	    		department=departmentService.getUpNearestImplDepartment(departmentSn);
	    	}
	    	hql+=" AND a.auditedDepartment.departmentSn like '"+department.getDepartmentSn()+"%'";
		}
		jsonList=systemAuditService.query(hql, page, rows);
		JSONArray array=new JSONArray();
		for(SystemAudit systemAudit:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", systemAudit.getId());
			jo.put("auditSn", systemAudit.getAuditSn());
			jo.put("systemAuditType", systemAudit.getSystemAuditType());
			jo.put("startDate", systemAudit.getStartDate());
			jo.put("endDate",systemAudit.getEndDate());
			jo.put("computedScore",systemAudit.getComputedScore());
			jo.put("amendScore", systemAudit.getAmendScore());
			jo.put("finalScore", systemAudit.getFinalScore());
			jo.put("remark", systemAudit.getRemark());
			jo.put("inconformityItemes", systemAudit.getInconformityItemes().size());
			jo.put("checkTable", systemAudit.getCheckTables().size());
			jo.put("attachment", systemAuditAttachmentService.count(systemAudit.getAuditSn()));
			if(systemAudit.getStandard()!=null){
				jo.put("standardSn", systemAudit.getStandard().getStandardSn());
			}else{
				jo.put("standardSn","");
			}
			//被审单位
			JSONObject cd=new JSONObject();
			Department department=systemAudit.getAuditedDepartment();
			if(department!=null){
				cd.put("isnull", false);
				cd.put("departmentSn",department.getDepartmentSn());
				cd.put("departmentName", department.getDepartmentName());
				cd.put("departmentTypeSn", department.getDepartmentType().getDepartmentTypeSn());
			}else{
				cd.put("isnull", true);
			}
			jo.put("auditedDepartment", cd);
			//审核组长
			JSONObject al=new JSONObject();
			Person auditTeamLeader=systemAudit.getAuditTeamLeader();
			if(auditTeamLeader!=null){
				al.put("isnull", false);
				al.put("personId", auditTeamLeader.getPersonId());
				al.put("personName",auditTeamLeader.getPersonName());
			}else{
				al.put("isnull",true);
				al.put("personId", "");
				al.put("personName","");
			}
			jo.put("auditTeamLeader", al);
			//录入人
			JSONObject et=new JSONObject();
			Person editor=systemAudit.getEditor();
			if(editor!=null){
				et.put("isnull",false);
				et.put("personId",editor.getPersonId());
				et.put("personName",editor.getPersonName());
			}else{
				et.put("isnull",true);
				et.put("personId","");
				et.put("personName","");
			}
			jo.put("editor",et);
			//审核成员
			JSONObject cjo=new JSONObject();
			int i=0;
			String name="";
			String pIds="";
			String genders="";
			String Ids="";
			for(Person person:systemAudit.getAuditors()){
				i++;
				pIds+=person.getPersonId()+",";
				name+=person.getPersonName()+",";
				genders+=person.getGender()+",";
				Ids+=person.getId()+",";
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
				pIds=pIds.substring(0,pIds.length()-1);
				genders=genders.substring(0,genders.length()-1);
				Ids=Ids.substring(0,Ids.length()-1);
				cjo.put("personNames", name);
				cjo.put("personIds",pIds);
				cjo.put("Ids",Ids);
				cjo.put("genders", genders);
			}else{
				cjo.put("personIds","");				
			}
			cjo.put("num",i);
			jo.put("auditors",cjo);
			//审核组长：文本
			jo.put("strAuditTeamLeader",systemAudit.getStrAuditTeamLeader());
			//检查人员：文本
			jo.put("strCheckers", systemAudit.getStrCheckers());
			array.put(jo);
		}
		map=new HashMap<String,Object>();
		map.put("total", systemAuditService.count(hql.replaceFirst("a", "count(a)")));
		map.put("rows",array);
		String str= JSONObject.valueToString(map);
		out.print(str);
        out.flush(); 
        out.close();
	}
	//获取评审指南
	public void getStandardIndex() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        List<StandardIndex> jsonLoad=null;
        PrintWriter out;  
        out = response.getWriter();
        String hql="";
        JSONArray tree=new JSONArray();
		if(indexSn!=null){
			hql="select s FROM StandardIndex s left join s.standard d where s.deleted=false and s.parent.indexSn='"+indexSn+"' AND d.standardSn='"+standardSn+"' order by s.showSequence asc";
			jsonLoad=standardindexService.getPart(hql);

		}else{
			hql="select s FROM StandardIndex s LEFT JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn ='"+standardSn+"' order by s.showSequence asc";
			jsonLoad=standardindexService.getPart(hql);
		}
		for(StandardIndex standardIndex:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", standardIndex.getId());
			jo.put("indexSn", standardIndex.getIndexSn());
			if(standardIndex.getIndexName()!=null){
				jo.put("indexName", standardIndex.getIndexName());
			}else{
				jo.put("indexName", "");
			}
			jo.put("auditKeyPoints",standardIndex.getAuditKeyPoints());
			//定义该指标的总分
			int integerScore=-1;
			if(standardIndex.getIntegerScore()!=null){
				integerScore=standardIndex.getIntegerScore()-systemAuditService.countNotScoreBySn(auditSn, standardIndex.getIndexSn());
				jo.put("integerScore",integerScore);
			}	
			SystemAuditScore systemAuditScore=systemAuditScoreService.getByMany(auditSn,standardIndex.getIndexSn());
			if(systemAuditScore!=null){
				jo.put("conformDegree", systemAuditScore.getConformDegree());
			}else if(standardIndex.getChildren().size()>0){
				if(standardIndex.getIntegerScore()!=null){
					List<SystemAuditScore> list=systemAuditScoreService.queryByMany(auditSn, standardIndex.getIndexSn());
					if(list.size()>0){
						float deduction=0;
						for(SystemAuditScore systemAuditScore2:list){
							StandardIndex index=systemAuditScore2.getStandardIndex();
							if(index!=null&&index.getParent()!=null&&systemAuditService.isScore(auditSn, index.getParent().getIndexSn())){
								int partScore=index.getParent().getIntegerScore();
								int com=100-systemAuditScore2.getConformDegree();
								switch (index.getIndexSn().substring(index.getIndexSn().trim().length()-1)){
								case "P":
									deduction+=partScore*0.2*com*0.01;
									break;
								case "C":
									deduction+=partScore*0.2*com*0.01;
									break;
								case "D":
									deduction+=partScore*0.5*com*0.01;
									break;
								case "A":
									deduction+=partScore*0.1*com*0.01;
									break;
								}
							}
						}
						if(integerScore!=-1){
							float f1;
							if(integerScore!=0){
								float f = (integerScore-deduction)/integerScore*100; 
								BigDecimal b = new BigDecimal(f); 
								f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
							}else{
								f1=0;
							}
							 
							jo.put("conformDegree", f1);
							jo.put("actualScore",integerScore-deduction);
						}else{
							jo.put("conformDegree", 100);
						}
					}else{
						jo.put("conformDegree", 100);
						if(integerScore!=-1){
							jo.put("actualScore", integerScore);
						}
					}

					//判断是否为四个环节的父级
					String hqlcount="select count(s) from StandardIndex s where s.deleted=false and s.integerScore is not null and s.parent.indexSn='"+standardIndex.getIndexSn()+"'";
					if(standardindexService.count(hqlcount)==0){
						jo.put("isParent", true);
					}
				}else{
					jo.put("conformDegree", 100);
				}
			}
			String sql="select count(*) from inconformity_item where deleted=false and (index_sn like '"+standardIndex.getIndexSn()+".%' or index_sn='"+standardIndex.getIndexSn()+"') and audit_sn="+auditSn;
			jo.put("unsafecondition", unsafeConditionService.count(sql));
			if(standardIndex.getChildren().size()>0){
				if(systemAuditService.isChecked(auditSn, standardIndex.getIndexSn())){
					jo.put("checked", true);
				}
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			tree.put(jo);
		}
		out.print(tree.toString());
        out.flush(); 
        out.close();
	}
	//修改
	public String update(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		try{
			SystemAudit systemAudit=systemAuditService.getById(id);
			//处理时间
			LocalDate localDate1 = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate localDate2 = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			//进行判断
			int nowyear=localDate1.getYear();
			 int nowmonth=localDate1.getMonthValue();
			 String hql="select count(*) FROM SystemAudit s WHERE s.deleted=false AND s.auditedDepartment.departmentSn="+systemAudit.getAuditedDepartment().getDepartmentSn()+" AND s.id !="+systemAudit.getId();
			 switch(systemAuditType){
			 case 单位审核:
				 hql+=" AND year(s.startDate) ="+nowyear+" AND s.systemAuditType=0 AND month(s.startDate)="+nowmonth;
				 if(systemAuditService.count(hql)>0){
					 map.put("status", "month");
					 return SUCCESS;
				 }
				 break;
			 case 季度审核:
//				 LocalDate checkQuarter=systemAudit.getCheckQuarter();
//				 if(checkQuarter!=null){
//					 String season="";
//					 switch(checkQuarter.getMonthValue()){
//					 case 1:
//					 case 2:
//					 case 3:
//						 season="1,2,3";
//						 break;
//					 case 4:
//					 case 5:
//					 case 6:
//						 season="4,5,6";
//						 break;
//					 case 7:
//					 case 8:
//					 case 9:
//						 season="7,8,9";
//						 break;
//					 case 10:
//					 case 11:
//					 case 12:
//						 season="10,11,12";
//						 break;
//					 }
//					 hql+=" AND year(s.checkQuarter) ="+checkQuarter.getYear()+"s.systemAuditType=1 AND month(s.checkQuarter) in("+season+")";
//					 if(systemAuditService.count(hql)>0){
//						 map.put("status", "season");
//						 return SUCCESS;
//					 }
//				 }else{
//					 String season="";
//					 switch(nowmonth){
//					 case 1:
//					 case 2:
//					 case 3:
//						 season="1,2,3";
//						 break;
//					 case 4:
//					 case 5:
//					 case 6:
//						 season="4,5,6";
//						 break;
//					 case 7:
//					 case 8:
//					 case 9:
//						 season="7,8,9";
//						 break;
//					 case 10:
//					 case 11:
//					 case 12:
//						 season="10,11,12";
//						 break;
//					 }
//					 hql+=" AND year(s.startDate) ="+nowyear+"s.systemAuditType=1 AND month(s.startDate) in("+season+")";
//					 if(systemAuditService.count(hql)>0){
//						 map.put("status", "season");
//						 return SUCCESS;
//					 }
//				 }
				 break;
			 case 神华审核:
				 hql+=" AND year(s.startDate) ="+nowyear+" AND s.systemAuditType=2";
				 if(systemAuditService.count(hql)>0){
					 map.put("status", "year");
					 return SUCCESS;
				 }
				 break;
			 }	 
			systemAudit.setStartDate(localDate1);
			systemAudit.setEndDate(localDate2);
			systemAudit.setAuditTeamLeader(personService.getByPersonId(personId));
			systemAudit.setStrCheckers(strCheckers);
			systemAudit.setStrAuditTeamLeader(strAuditTeamLeader);
			if(ids!=null && ids.length()>0){
				 systemAudit.setAuditors(new HashSet<Person>(personService.getByPersonIds(ids)));
			}else{
				systemAudit.setAuditors(null);
			}
			if(departmentSn!=null){
				systemAudit.setAuditedDepartment(departmentService.getByDepartmentSn(departmentSn));
			}
			systemAuditService.update(systemAudit);
		}catch(Exception e){
			map.put("status", "nook");
		}		
		return SUCCESS;
	}
	//添加
	public String save(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		 try{
			 //处理时间
			 SystemAudit  systemAudit=new  SystemAudit();
			 LocalDate localDate1 = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			 LocalDate localDate2 = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			 
			 //进行判断
			 int nowyear=localDate1.getYear();
			 int nowmonth=localDate1.getMonthValue();
			 String hql="select count(*) FROM SystemAudit s WHERE s.deleted=false AND s.auditedDepartment.departmentSn='"+departmentSn+"'";
			 switch(systemAuditType){
			 case 单位审核:
				 hql+=" AND year(s.startDate) ="+nowyear+" AND s.systemAuditType=0 AND month(s.startDate)="+nowmonth;
				 if(systemAuditService.count(hql)>0){
					 map.put("status", "month");
					 return SUCCESS;
				 }
				 break;
			 case 季度审核:
				 hql+=" AND year(s.checkQuarter) ="+year+" AND s.systemAuditType=1 AND month(s.checkQuarter) in("+month+")";
				 if(systemAuditService.count(hql)>0){
					 map.put("status", "season");
					 return SUCCESS;
				 }
				 break;
			 case 神华审核:
				 hql+=" AND year(s.startDate) ="+nowyear+" AND s.systemAuditType=2";
				 if(systemAuditService.count(hql)>0){
					 map.put("status", "year");
					 return SUCCESS;
				 }
				 break;
			 }
			 //处理编号
			 String auditSn=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
			 systemAudit.setAuditSn(auditSn); 
			 systemAudit.setStartDate(localDate1);
			 systemAudit.setEndDate(localDate2);
			 systemAudit.setSystemAuditType(systemAuditType);
			 systemAudit.setAuditedDepartment(departmentService.getByDepartmentSn(departmentSn));
			 systemAudit.setEditor(personService.getByPersonId(editorId));
			 systemAudit.setAuditTeamLeader(personService.getByPersonId(personId));
			 systemAudit.setDeleted(false);
			 systemAudit.setStandard(standardService.getByStandardSn(standardSn));
			 
			 if(month!=null && month.trim().length()>0){
				 int quarterMonth=0;
				 switch(month){
				 case "1,2,3":
					 quarterMonth=1;
					 break;
				 case "4,5,6":
					 quarterMonth=4;
					 break;
				 case "7,8,9":
					 quarterMonth=7;
					 break;
				 case "10,11,12":
					 quarterMonth=11;
				 }
				 LocalDate checkQuarter=LocalDate.of(Integer.valueOf(year), quarterMonth, 1);
				 systemAudit.setCheckQuarter(checkQuarter);
			 }
			 
			 if(ids!=null&&ids.length()>0){
				 systemAudit.setAuditors(new HashSet<Person>(personService.getByPersonIds(ids)));
			 }
			 systemAudit.setStrCheckers(strCheckers);
			 systemAudit.setStrAuditTeamLeader(strAuditTeamLeader);
			 systemAuditService.save(systemAudit);
		 }catch(Exception e){
			 map.put("status", "nook");
		 }
		 return SUCCESS;
	}
	//删除
	public String delete(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		try{
			systemAuditService.deleteByIds(ids);
		}catch(Exception e){
			map.put("status", "nook");
		}
		return SUCCESS;
	}
	//行内编辑修改
	public String lineedit(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		try{
			SystemAudit systemAudit=systemAuditService.getById(id);
			systemAudit.setRemark(remark);
			if(amendScore!=null){
				if(systemAudit.getComputedScore()!=null){
					if(systemAudit.getComputedScore()+amendScore>100){
						map.put("status", "over");
					}else{
						systemAudit.setAmendScore(amendScore);
						systemAudit.setFinalScore(systemAudit.getComputedScore()+amendScore);
					}
				}else{
					systemAudit.setAmendScore(amendScore);
				}
			}else{
				systemAudit.setAmendScore(null);
			}
			systemAuditService.update(systemAudit);
		}catch(Exception e){
			map.put("status", "nook");
		}		
		return SUCCESS;
	}
	 
	//审核人员模板
	public String personTemplate(){  
        HSSFWorkbook wb = new HSSFWorkbook();   
        HSSFSheet sheet = wb.createSheet("审核人员");

        sheet.setColumnWidth(1,70*70);
        HSSFRow row;  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        HSSFCell cell;
        row=sheet.createRow(0);
        cell= row.createCell(0);
        cell.setCellValue("人员编号");  
        cell.setCellStyle(style);  
        cell = row.createCell(1);  
        cell.setCellValue("人员姓名");  
        cell.setCellStyle(style);
        for(int i=1;i<200;i++){
        	row=sheet.createRow(i);
        	cell = row.createCell(0);
        	cell.setCellStyle(style);
        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        	cell = row.createCell(1);
        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        	cell.setCellStyle(style);
        }
        try  
        {  
        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
  
            excelStream = is;               
            excelFileName ="persons.xls"; 	      
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
        return "DI";
	}
	//审核人员导入
	public void importP() throws IOException{
		List<Person> jsonLoad=new ArrayList<Person>();
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(FileUtils.openInputStream(excel));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		HSSFSheet sheet=wb.getSheetAt(0);
		String value="";
		String error="";
		int errorNum=0;
		for(int i=1;i<sheet.getLastRowNum();i++){
			HSSFRow row=sheet.getRow(i);
			HSSFCell cell = row.getCell(0);
			try{
				if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
					HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
				    value = dataFormatter.formatCellValue(cell); 
				}else{
					value = cell.getRichStringCellValue().toString();
				}
			}catch(Exception e){
				continue;
			}
	        if(value.length()>0){
	        	Person person=personService.getByPersonId(value);
		        if(person!=null){
		        	if(departmentSn!=null&&departmentSn.trim().length()>0){
		        		if(person.getDepartment().getDepartmentSn().contains(departmentSn)){
			        		jsonLoad.add(person);
			        	}else{
			        		errorNum++;
			        		error+="第"+i+"行："+person.getPersonName()+"不属于本单位，无法添加！";
			        	}
		        	}else{
		        		jsonLoad.add(person);
		        	}		        	
		        }else{
		        	errorNum++;
		        	error+="第"+i+"行：人员编号不存在!";
		        }
	        }
		}
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		JSONArray array=new JSONArray();
		JSONArray array2=new JSONArray();
		//JSONArray array3=new JSONArray();
		for(Person p:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", p.getId());
			jo.put("personId", p.getPersonId());
			jo.put("personName", p.getPersonName());
			jo.put("gender",p.getGender());
			array.put(jo);
		}
		JSONObject jo=new JSONObject();
		jo.put("error",error);
		jo.put("errornum",errorNum);
		array2.put(jo);
		JSONObject alljo=new JSONObject();
		alljo.put("rows",array);
		alljo.put("error", array2);
		out.print(alljo.toString());
        out.flush(); 
        out.close();
	}
	//审核人员导出
	public String exportP() throws IOException{
		 HSSFWorkbook wb = new HSSFWorkbook();   
	        HSSFSheet sheet = wb.createSheet("审核人员");
	        sheet.setColumnWidth(1,70*70);
	        HSSFRow row;  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFDataFormat format = wb.createDataFormat();
	        style.setDataFormat(format.getFormat("@"));
	        HSSFCell cell;
	        row=sheet.createRow(0);
	        cell= row.createCell(0);
	        cell.setCellValue("人员编号");  
	        cell.setCellStyle(style);  
	        cell = row.createCell(1);  
	        cell.setCellValue("人员姓名");  
	        cell.setCellStyle(style);
	        for(int i=0;i<personIds.split(",").length;i++){
	        	row=sheet.createRow(i+1);
	        	cell = row.createCell(0);
	        	cell.setCellStyle(style);
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        	cell.setCellValue(personIds.split(",")[i]);
	        	cell = row.createCell(1);
	        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        	cell.setCellStyle(style);
	        	cell.setCellValue(personNames.split(",")[i]);
	        }
	        try  
	        {  
	        	ByteArrayOutputStream fout = new ByteArrayOutputStream();  
	            wb.write(fout);
	            wb.close();
	            fout.close();
	            byte[] fileContent = fout.toByteArray();  
	            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);  
	  
	            excelStream = is;               
	            excelFileName ="persons.xls"; 	      
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }
	        return "DI";
	}
	//获取最近的部门
	public String getNearDeaprtment(){
		map=new HashMap<String,Object>();
		int roleType=(int) session.get("roleType");
		Department department;
		if(roleType==1){
			String departmentSn=(String) session.get("departmentSn");
			department=departmentService.getUpNerestFgs(departmentSn);
		}else{
			List<DepartmentType> departmentTypes=departmentTypeService.getImplDepartmentTypes(departmentSn);
			
	    	if(departmentTypes.size()>0){
	    		department=departmentService.getByDepartmentSn(departmentSn);
	    	}else{
	    		department=departmentService.getUpNearestImplDepartment(departmentSn);
	    	}
		}
		
    	map.put("departmentSn", department.getDepartmentSn());
    	map.put("departmentName", department.getDepartmentName());
		return SUCCESS;
	}
	//获取隐患
	public void queryJoinUnsafeCondition() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
		PrintWriter out;  
        out = response.getWriter();
		JSONArray array=new JSONArray();
		SystemAudit systemAudit=systemAuditService.getById(id);
		for(InconformityItem inconformityItem:systemAudit.getInconformityItemes()){
			if(inconformityItem instanceof UnsafeCondition ){
				JSONObject jo=new JSONObject();
				jo.put("id", inconformityItem.getId());//id
				jo.put("inconformityItemSn",inconformityItem.getInconformityItemSn());//编号
				jo.put("checkDateTime", inconformityItem.getCheckDateTime());//检查时间
				jo.put("checkLocation", inconformityItem.getCheckLocation());//检查地
				jo.put("inconformityItemNature",inconformityItem.getInconformityItemNature());//不符合项性质
				jo.put("hasReviewed", ((UnsafeCondition)inconformityItem).getHasReviewed());
				jo.put("hasCorrectConfirmed", ((UnsafeCondition)inconformityItem).getHasCorrectConfirmed());
				jo.put("hasCorrectFinished", ((UnsafeCondition)inconformityItem).getHasCorrectFinished());
				//机
				JSONObject mjo=new JSONObject();
				if(((UnsafeCondition)inconformityItem).getMachine()!=null){
					mjo.put("isnull",false);
					mjo.put("manageObjectSn", ((UnsafeCondition)inconformityItem).getMachine().getManageObjectSn());
					mjo.put("manageObjectName", ((UnsafeCondition)inconformityItem).getMachine().getManageObjectName());
				}else{
					mjo.put("isnull", true);
				}
				jo.put("machine", mjo);
				//被检部门
				JSONObject chdjo=new JSONObject();
				if(inconformityItem.getCheckedDepartment()!=null){
					chdjo.put("isnull", false);
					chdjo.put("departmentSn", inconformityItem.getCheckedDepartment().getDepartmentSn());
					chdjo.put("departmentName",  inconformityItem.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("isnull", true);
				}
				jo.put("checkedDepartment", chdjo);
				//指标
				JSONObject sjo=new JSONObject();
				if(((UnsafeCondition)inconformityItem).getStandardIndex()!=null){
					sjo.put("isnull", false);
					sjo.put("id",((UnsafeCondition)inconformityItem).getStandardIndex().getId());
					sjo.put("indexName", ((UnsafeCondition)inconformityItem).getStandardIndex().getIndexName());
				}else{
					sjo.put("isnull",true);
				}
				jo.put("standardIndex", sjo);
				jo.put("problemDescription",((UnsafeCondition)inconformityItem).getProblemDescription());//问题描述
				jo.put("deductPoints", ((UnsafeCondition)inconformityItem).getDeductPoints());//扣分
				jo.put("correctType", ((UnsafeCondition)inconformityItem).getCorrectType());
				jo.put("correctDeadline", ((UnsafeCondition)inconformityItem).getCorrectDeadline());//整改期限
				//对应的危险源
				JSONObject hjo=new JSONObject();
				if(((UnsafeCondition)inconformityItem).getHazrd()!=null){
					hjo.put("isnull",false);
					hjo.put("hazardSn",((UnsafeCondition)inconformityItem).getHazrd().getHazardSn());
					hjo.put("hazardDescription", ((UnsafeCondition)inconformityItem).getHazrd().getHazardDescription());
				}else{
					hjo.put("isnull", true);
				}
				jo.put("hazard",hjo);
				//所属专业
				JSONObject spjo=new JSONObject();
				if(inconformityItem.getSpeciality()!=null){
					spjo.put("isnull", false);
					spjo.put("specialitySn", inconformityItem.getSpeciality().getSpecialitySn());
					spjo.put("specialityName", inconformityItem.getSpeciality().getSpecialityName());
				}else{
					spjo.put("isnull", true);
				}
				jo.put("speciality",spjo);
				jo.put("inconformityLevel",((UnsafeCondition)inconformityItem).getInconformityLevel());//不符合项等级
				//整改负责人
				JSONObject cpjo=new JSONObject();
				if(((UnsafeCondition)inconformityItem).getCorrectPrincipal()!=null){
					cpjo.put("isnull",false);
					cpjo.put("personId",((UnsafeCondition)inconformityItem).getCorrectPrincipal().getPersonId());
					cpjo.put("personName",((UnsafeCondition)inconformityItem).getCorrectPrincipal().getPersonName());
				}else{
					cpjo.put("isnull",true);
				}
				jo.put("correctPrincipal", cpjo);
				jo.put("correctProposal",((UnsafeCondition)inconformityItem).getCorrectProposal());//整改建议
				//检查人员
				JSONObject cjo=new JSONObject();
				int i=0;
				String name="";
				String pIds="";
				for(Person person:inconformityItem.getCheckers()){
					i++;
					pIds+=person.getId()+",";
					name+=person.getPersonName()+",";
				}
				if(i>0){
					name=name.substring(0,name.length()-1);
					pIds=pIds.substring(0,pIds.length()-1);
					cjo.put("personNames", name);
					cjo.put("personIds",pIds);
				}
				cjo.put("num",i);
				jo.put("checkers",cjo);
				jo.put("attachment",inconformityItem.getAttachments().size());
				//审核方法
				JSONObject amjo=new JSONObject();
				String count="";
				String method="";
				String methodId="";
				int g=0;
				//inconformityItem.getAuditMethods().entrySet();
				for(Entry<String,Integer> am:inconformityItem.getAuditMethods().entrySet()){
					StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodService.getBySn(am.getKey());
					g++;
					//方法名
					method+=standardIndexAuditMethod.getAuditMethodContent()+",";
					//方法Id
					methodId+=standardIndexAuditMethod.getId()+",";
					//次数
					count+=am.getValue()+",";
					standardIndexAuditMethod.getId();
				}
				if(g>0){
					method=method.substring(0, method.length()-1);
					methodId=methodId.substring(0,methodId.length()-1);
					count=count.substring(0,count.length()-1);
					amjo.put("method", method);
					amjo.put("count", count);
					amjo.put("methodId", methodId);
				}
				amjo.put("num", g);
				jo.put("auditMethod", amjo);
				array.put(jo);
			}
		}
		out.print(array.toString());
        out.flush(); 
        out.close();
	}
	//获取部门
	public void getDepartment() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        List<Department> jsonLoad=null;
 
        String hql="FROM Department p where p.deleted=false and p.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
        jsonLoad=departmentService.findByPage(hql, 1, 10000);        
		JSONArray tree=new JSONArray();
		for(Department department:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("departmentSn",department.getDepartmentSn());
			jo.put("departmentName",department.getDepartmentName());				
			tree.put(jo);
		}
		out.println(tree.toString()); 
        out.flush(); 
        out.close();  
	}	
	public String getStrAuditTeamLeader() {
		return strAuditTeamLeader;
	}
	public void setStrAuditTeamLeader(String strAuditTeamLeader) {
		this.strAuditTeamLeader = strAuditTeamLeader;
	}
	public String getStrCheckers() {
		return strCheckers;
	}
	public void setStrCheckers(String strCheckers) {
		this.strCheckers = strCheckers;
	}
	public String getResourceSn() {
		return resourceSn;
	}
	public void setResourceSn(String resourceSn) {
		this.resourceSn = resourceSn;
	}
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Float getAmendScore() {
		return amendScore;
	}
	public void setAmendScore(Float amendScore) {
		this.amendScore = amendScore;
	}
	public String getPersonIds() {
		return personIds;
	}
	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}
	public String getPersonNames() {
		return personNames;
	}
	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}
	public File getExcel() {
		return excel;
	}
	public void setExcel(File excel) {
		this.excel = excel;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getExcelFileName() {
		return excelFileName;
	}
	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public SystemAuditType getSystemAuditType() {
		return systemAuditType;
	}
	public void setSystemAuditType(SystemAuditType systemAuditType) {
		this.systemAuditType = systemAuditType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getEditorId() {
		return editorId;
	}
	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getExcelContentType() {
		return excelContentType;
	}
	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
