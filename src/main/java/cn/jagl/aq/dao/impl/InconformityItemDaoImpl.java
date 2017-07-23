package cn.jagl.aq.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.InconformityItemDao;
import cn.jagl.aq.dao.StandardIndexAuditMethodDao;
import cn.jagl.aq.dao.UnsafeActDao;
import cn.jagl.aq.dao.UnsafeConditionDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.InconformityItem;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.UnsafeAct;
import cn.jagl.aq.domain.UnsafeCondition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Repository("inconformityItemDao")
public class InconformityItemDaoImpl extends BaseDaoHibernate5<InconformityItem> implements InconformityItemDao {

	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	@Resource(name="unsafeConditionDao")
	private UnsafeConditionDao unsafeConditionDao;
	@Resource(name="unsafeActDao")
	private UnsafeActDao unsafeActDao;
	@Resource(name="standardIndexAuditMethodDao")
	private StandardIndexAuditMethodDao standardIndexAuditMethodDao;
	
	/**
	 * @method 隐患导出
	 * @param list
	 * @author mahui
	 * @return InputStream
	 */
	private InputStream exportUnsafeCondition(List<UnsafeCondition> list) {
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("隐患");
		sheet.setDefaultColumnWidth(17);
		sheet.setColumnWidth(0, 70*70);
		sheet.setColumnWidth(5, 90*90);
		sheet.setColumnWidth(8, 70*70);
		sheet.setColumnWidth(9, 75*75);
		sheet.setColumnWidth(13, 110*110);
		//样式一（标题）
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 12);
        style1.setFont(font1);
        font1.setBold(true);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setWrapText(true);
        
        //样式二（内容）
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 11);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        
        //样式三日期
        HSSFDataFormat format = wb.createDataFormat();
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font2);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setWrapText(true);
        style3.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm"));
        
        //生成表头
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("检查时间");  
        cell.setCellStyle(style1);  
        cell = row.createCell(1);  
        cell.setCellValue("检查地点");  
        cell.setCellStyle(style1);  
        cell = row.createCell(2);  
        cell.setCellValue("不符合项性质");  
        cell.setCellStyle(style1);
        cell = row.createCell(3);
        cell.setCellValue("机");  
        cell.setCellStyle(style1); 
        cell = row.createCell(4);
        cell.setCellValue("被检部门");  
        cell.setCellStyle(style1); 
        cell = row.createCell(5);
        cell.setCellValue("问题描述");  
        cell.setCellStyle(style1);
        cell = row.createCell(6);  
        cell.setCellValue("扣分");  
        cell.setCellStyle(style1); 
        cell = row.createCell(7);  
        cell.setCellValue("不符合项等级");  
        cell.setCellStyle(style1);
        cell = row.createCell(8);
        cell.setCellValue("整改期限");  
        cell.setCellStyle(style1);
        cell = row.createCell(9);
        cell.setCellValue("整改建议");  
        cell.setCellStyle(style1);
        cell = row.createCell(10);
        cell.setCellValue("整改确认");  
        cell.setCellStyle(style1);
        cell = row.createCell(11);
        cell.setCellValue("已复查");  
        cell.setCellStyle(style1);
        cell = row.createCell(12);
        cell.setCellValue("整改完成");  
        cell.setCellStyle(style1);
        cell = row.createCell(13);
        cell.setCellValue("扣分指标");  
        cell.setCellStyle(style1);
        cell = row.createCell(14);
        cell.setCellValue("危险源");  
        cell.setCellStyle(style1);
        cell = row.createCell(15);
        cell.setCellValue("扣分项");  
        cell.setCellStyle(style1);
        cell = row.createCell(16);
        cell.setCellValue("专业");  
        cell.setCellStyle(style1);
        cell = row.createCell(17);
        cell.setCellValue("检查人");  
        cell.setCellStyle(style1);
        cell = row.createCell(18);
        cell.setCellValue("整改负责人");  
        cell.setCellStyle(style1);
        cell = row.createCell(19);
        cell.setCellValue("录入人");  
        cell.setCellStyle(style1);
        
        int rownum=1;
		for(UnsafeCondition u:list){
			row=sheet.createRow(rownum);
			rownum++;
			//检查时间
	        cell=row.createCell(0);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCheckDateTime()!=null){
	        	cell.setCellValue(u.getCheckDateTime());  
	        }
	        
	        cell.setCellStyle(style3);  
	        
	        //检查地点
	        cell = row.createCell(1);  
	        cell.setCellValue(u.getCheckLocation());  
	        cell.setCellStyle(style2); 
	        
	        //不符合项性质
	        cell = row.createCell(2);  
	        cell.setCellValue(u.getInconformityItemNature()==null? "":u.getInconformityItemNature().toString());  
	        cell.setCellStyle(style2);
	        
	        //机
	        cell = row.createCell(3);
	        if(u.getMachine()!=null){
	        	cell.setCellValue(u.getMachine().getManageObjectName()); 
	        } 
	        cell.setCellStyle(style2);
	        
	        //被检部门
	        cell = row.createCell(4);
	        if(u.getCheckedDepartment()!=null){
	        	cell.setCellValue(u.getCheckedDepartment().getDepartmentName());  	
	        }
	        cell.setCellStyle(style2); 
	        
	        //问题描述
	        cell = row.createCell(5);
	        cell.setCellValue(u.getProblemDescription());  
	        cell.setCellStyle(style2);
	        
	        //扣分
	        cell = row.createCell(6);  
	        cell.setCellValue(u.getDeductPoints());  
	        cell.setCellStyle(style2); 
	        
	        //不符合项等级
	        cell = row.createCell(7);  
	        cell.setCellValue(u.getInconformityLevel()==null ?"":u.getInconformityLevel().toString());  
	        cell.setCellStyle(style2);
	        
	        //整改期限
	        cell = row.createCell(8);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCorrectDeadline()!=null){
	        	cell.setCellValue(u.getCorrectDeadline());  
	        }
	        cell.setCellStyle(style3);
	        
	        //整改建议
	        cell = row.createCell(9);
	        cell.setCellValue(u.getCorrectProposal());  
	        cell.setCellStyle(style2);
	        
	        //整改确认
	        cell = row.createCell(10);
	        cell.setCellValue((u.getHasCorrectConfirmed()!=null && u.getHasCorrectConfirmed()==true)?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //已复查
	        cell = row.createCell(11);
	        cell.setCellValue(u.getHasReviewed()!=null && u.getHasReviewed()==true ?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //整改完成
	        cell = row.createCell(12);
	        cell.setCellValue(u.getHasCorrectFinished()!=null && u.getHasCorrectFinished()==true?"是":"否");  
	        cell.setCellStyle(style2);
	        
	        //扣分指标
	        cell = row.createCell(13);
	        cell.setCellValue(u.getStandardIndex()!=null?u.getStandardIndex().getIndexName():"");  
	        cell.setCellStyle(style2);
	        
	        //危险源
	        cell = row.createCell(14);
	        cell.setCellValue(u.getHazrd()!=null ? u.getHazrd().getHazardDescription():"");  
	        cell.setCellStyle(style2);
	        
	        //扣分项
			String method="";
			int g=0;
			for(Entry<String,Integer> am:u.getAuditMethods().entrySet()){
				StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodDao.getBySn(am.getKey());
				if( standardIndexAuditMethod != null ){
					g++;
					//方法名
					method+=standardIndexAuditMethod.getAuditMethodContent()+",";
				}
			}
			if(g>0){
				method=method.substring(0, method.length()-1);
			}
	        cell = row.createCell(15);
	        cell.setCellValue(method);  
	        cell.setCellStyle(style2);
	        
	        //专业
	        cell = row.createCell(16);
	        cell.setCellValue(u.getSpeciality()!=null ? u.getSpeciality().getSpecialityName():"");
	        cell.setCellStyle(style2);
	        
	        //检查人
	        int i=0;
			String name="";
			for(Person person:u.getCheckers()){
				i++;
				name+=person.getPersonName()+",";
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
			}
	        cell = row.createCell(17);
	        cell.setCellValue(name);  
	        cell.setCellStyle(style2);
	        
	        //整改负责人
	        cell = row.createCell(18);
	        cell.setCellValue(u.getCorrectPrincipal()!=null ? u.getCorrectPrincipal().getPersonName():"");  
	        cell.setCellStyle(style2);
	        
	        //录入人
	        cell = row.createCell(19);
	        cell.setCellValue(u.getEditor()!=null ? u.getEditor().getPersonName():"");  
	        cell.setCellStyle(style2);
		}
		try{
			ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            InputStream is = new ByteArrayInputStream(fileContent);
	    	return is;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @method 不安全行为导出
	 * @param list
	 * @author mahui
	 * @return InputStream
	 */
	private InputStream exportUnsafeAct(List<UnsafeAct> list) {
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("不安全行为");
		sheet.setDefaultColumnWidth(19);
		sheet.setColumnWidth(2, 110*110);
		sheet.setColumnWidth(9, 110*110);
		
		//样式一（标题）
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 12);
        style1.setFont(font1);
        font1.setBold(true);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setWrapText(true);
        
        //样式二（内容）
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 11);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        
        //样式三日期
        HSSFDataFormat format = wb.createDataFormat();
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font2);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setWrapText(true);
        style3.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm"));
        
        String titles="被检部门,不安全行为人员,行为描述,检查成员,检查时间,检查地点,录入人,所属专业,不安全行为痕迹,不安全行为标准,不安全行为等级";
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        for(int i=0;i<titles.split(",").length;i++){
        	cell = row.createCell(i);
        	cell.setCellValue(titles.split(",")[i]);//列名
        	cell.setCellStyle(style1);
        }
        
        int rownum=1;
		for(UnsafeAct u:list){
			row=sheet.createRow(rownum);
			rownum++;
			
			//被检部门
			cell = row.createCell(0);
	        if(u.getCheckedDepartment()!=null){
	        	cell.setCellValue(u.getCheckedDepartment().getDepartmentName());  	
	        }
	        cell.setCellStyle(style2);
	        
	        //不安全行为人员
	        cell=row.createCell(1);
	        cell.setCellValue(u.getViolator()!=null ? u.getViolator().getPersonName():"");  	        
	        cell.setCellStyle(style2);
	        
	        //行为描述
	        cell=row.createCell(2);
	        cell.setCellValue(u.getActDescription()!=null ? u.getActDescription():"");  	        
	        cell.setCellStyle(style2);
	        
	        //检查成员
	        int i=0;
			String name="";
			for(Person person:u.getCheckers()){
				i++;
				name+=person.getPersonName()+",";
			}
			if(i>0){
				name=name.substring(0,name.length()-1);
			}
	        cell = row.createCell(3);
	        cell.setCellValue(name);  
	        cell.setCellStyle(style2);
	        
	        //检查时间
	        cell=row.createCell(4);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCheckDateTime()!=null){
	        	cell.setCellValue(u.getCheckDateTime());  
	        }
	        
	        cell.setCellStyle(style3);  
	        
	        //检查地点
	        cell = row.createCell(5);  
	        cell.setCellValue(u.getCheckLocation());  
	        cell.setCellStyle(style2); 
	        
	        //录入人
	        cell = row.createCell(6);
	        cell.setCellValue(u.getEditor()!=null ? u.getEditor().getPersonName():"");  
	        cell.setCellStyle(style2);
	        
	        //专业
	        cell = row.createCell(7);
	        cell.setCellValue(u.getSpeciality()!=null ? u.getSpeciality().getSpecialityName():"");
	        cell.setCellStyle(style2);
	        
	        //不安全行为痕迹
	        cell = row.createCell(8);
	        cell.setCellValue(u.getUnsafeActMark()!=null?u.getUnsafeActMark().toString():"");
	        cell.setCellStyle(style2);
	        
	        //不安全行为标准
	        cell = row.createCell(9);
	        cell.setCellValue(u.getUnsafeActStandard()!=null?u.getUnsafeActStandard().getStandardDescription():"");
	        cell.setCellStyle(style2);
	        
	        //不安全行为等级
	        cell = row.createCell(10);
	        cell.setCellValue(u.getInconformityLevel()!=null?u.getInconformityLevel().toString():"");
	        cell.setCellStyle(style2);
		}
		try{
			ByteArrayOutputStream fout = new ByteArrayOutputStream();  
            wb.write(fout);
            wb.close();
            fout.close();
            byte[] fileContent = fout.toByteArray();  
            InputStream is = new ByteArrayInputStream(fileContent);
	    	return is;
		}catch(Exception e){
			return null;
		}
	}
	//区队考核查询贯标单位下的所有类型
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> queryAllDepartmentType(String departmentSn) {
		String hql="SELECT DISTINCT d.departmentType.departmentTypeSn as departmentTypeSn,d.departmentType.departmentTypeName as departmentTypeName FROM Department d where d.departmentSn like '"+departmentSn+"%'";
		return getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
	}

	//区队考核查询个数
	@Override
	public JSONArray query(String departmentSn, String departmentTypeSn, String year, String month) {
		String hql="";
		JSONArray array=new JSONArray();
		for(Department department:departmentDao.getDepartments(departmentSn, departmentTypeSn)){
			JSONObject jo=new JSONObject();
			jo.put("departmentSn", department.getDepartmentSn());
			jo.put("departmentName", department.getDepartmentName());
			//查询隐患个数
			for(int i=0;i<3;i++){
				hql="select count(u) from UnsafeCondition u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.inconformityLevel="+i+" and u.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%'";
				jo.put(i+"s1", unsafeConditionDao.countHql(hql));
				String hql2=hql+" and ((u.hasCorrectConfirmed=false and u.correctDeadline<'"+LocalDateTime.now()+"') OR (u.hasCorrectConfirmed=true and u.correctDeadline<u.confirmTime))";
				jo.put(i+"s2", unsafeConditionDao.countHql(hql2));
				String hql3=hql+" and 1<(select count(s) from UnsafeConditionCorrectConfirm s where s.inconformityItem.inconformityItemSn =u.inconformityItemSn)";
				jo.put(i+"s3", unsafeConditionDao.countHql(hql3));
			}
			//隐患合计
			jo.put("3s1", jo.getInt("0s1")+jo.getInt("1s1")+jo.getInt("2s1"));
			jo.put("3s2", jo.getInt("0s2")+jo.getInt("1s2")+jo.getInt("2s2"));
			jo.put("3s3", jo.getInt("0s3")+jo.getInt("1s3")+jo.getInt("2s3"));
			//查询不安全行为个数
			for(int i=0;i<3;i++){
				hql="select count(u) from UnsafeAct u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.unsafeActStandard.unsafeActLevel="+i+" and u.checkedDepartment.departmentSn like '"+department.getDepartmentSn()+"%'";
				jo.put(i, unsafeActDao.countByHql(hql));
			}
			jo.put("3", jo.getInt("0")+jo.getInt("1")+jo.getInt("2"));
			array.add(jo);
		}
		return array;
	}
	//区队考核查询隐患详情
	@Override
	public JSONObject queryUnsafeCondition(String departmentSn, String year, String month, String type, String value,
			int page, int rows) {
		String hql="select u from UnsafeCondition u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.checkedDepartment.departmentSn like '"+departmentSn+"%'";;
		JSONArray array=new JSONArray();
		List<UnsafeCondition> list=new ArrayList<UnsafeCondition>();
		if(value.equals("3")){
			if(type.equals("over")){
				hql+=" and ((u.hasCorrectConfirmed=false and u.correctDeadline<'"+LocalDateTime.now()+"') OR (u.hasCorrectConfirmed=true and u.correctDeadline<u.confirmTime))";
			}else if(type.equals("cover")){
				hql+=" and 1<(select count(s) from UnsafeConditionCorrectConfirm s where s.inconformityItem.inconformityItemSn =u.inconformityItemSn)";
			}
		}else{
			hql+=" and u.inconformityLevel="+value;
			if(type.equals("over")){
				hql+=" and ((u.hasCorrectConfirmed=false and u.correctDeadline<'"+LocalDateTime.now()+"') OR (u.hasCorrectConfirmed=true and u.correctDeadline<u.confirmTime))";
			}else if(type.equals("cover")){
				hql+=" and 1<(select count(s) from UnsafeConditionCorrectConfirm s where s.inconformityItem.inconformityItemSn =u.inconformityItemSn)";
			}
		}
		list=unsafeConditionDao.findByPage(hql, page, rows);
		for(UnsafeCondition unsafeCondition:list){
			JSONObject jo=new JSONObject();
			jo.put("id", unsafeCondition.getId());//id
			jo.put("inconformityItemSn",unsafeCondition.getInconformityItemSn());//编号
			if(unsafeCondition.getCheckDateTime()!=null){
				jo.put("checkDateTime", unsafeCondition.getCheckDateTime().toString());//检查时间
			}
			jo.put("checkLocation", unsafeCondition.getCheckLocation());//检查地
			jo.put("inconformityItemNature",unsafeCondition.getInconformityItemNature());//不符合项性质
			jo.put("deductPoints",unsafeCondition.getDeductPoints());
			jo.put("hasReviewed", unsafeCondition.getHasReviewed());
			jo.put("hasCorrectConfirmed", unsafeCondition.getHasCorrectConfirmed());
			jo.put("hasCorrectFinished", unsafeCondition.getHasCorrectFinished());
			jo.put("checkType", unsafeCondition.getCheckType());
			jo.put("strCheckers", unsafeCondition.getStrCheckers());
			jo.put("checkerFrom",unsafeCondition.getCheckerFrom());
			//机
			JSONObject mjo=new JSONObject();
			if(unsafeCondition.getMachine()!=null){
				mjo.put("isnull",false);
				mjo.put("manageObjectSn", unsafeCondition.getMachine().getManageObjectSn());
				mjo.put("manageObjectName", unsafeCondition.getMachine().getManageObjectName());
			}else{
				mjo.put("isnull", true);
			}
			jo.put("machine", mjo);
			//被检部门
			JSONObject chdjo=new JSONObject();
			if(unsafeCondition.getCheckedDepartment()!=null){
				chdjo.put("isnull", false);
				chdjo.put("departmentSn", unsafeCondition.getCheckedDepartment().getDepartmentSn());
				if(unsafeCondition.getCheckedDepartment().getImplDepartmentName()!=null&&!unsafeCondition.getCheckedDepartment().getImplDepartmentName().equals(unsafeCondition.getCheckedDepartment().getDepartmentName())){
					chdjo.put("departmentName", unsafeCondition.getCheckedDepartment().getImplDepartmentName()+unsafeCondition.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("departmentName",unsafeCondition.getCheckedDepartment().getDepartmentName());
				}
				
			}else{
				chdjo.put("isnull", true);
			}
			jo.put("checkedDepartment", chdjo);
			//指标
			JSONObject sjo=new JSONObject();
			if(unsafeCondition.getStandardIndex()!=null){
				sjo.put("isnull", false);
				sjo.put("id",unsafeCondition.getStandardIndex().getId());
				sjo.put("indexName", unsafeCondition.getStandardIndex().getIndexName());
			}else{
				sjo.put("isnull",true);
			}
			jo.put("standardIndex", sjo);
			jo.put("problemDescription",unsafeCondition.getProblemDescription());//问题描述
			jo.put("deductPoints", unsafeCondition.getDeductPoints());//扣分
			if(unsafeCondition.getCorrectType()!=null){
				jo.put("correctType", unsafeCondition.getCorrectType());
			}else{
				jo.put("correctType", "");
			}	
			if(unsafeCondition.getCorrectDeadline()!=null){
				jo.put("correctDeadline", unsafeCondition.getCorrectDeadline().toString());//整改期限
			}
			//对应的危险源
			JSONObject hjo=new JSONObject();
			if(unsafeCondition.getHazrd()!=null){
				hjo.put("isnull",false);
				hjo.put("hazardSn",unsafeCondition.getHazrd().getHazardSn());
				hjo.put("hazardDescription", unsafeCondition.getHazrd().getHazardDescription());
			}else{
				hjo.put("isnull", true);
			}
			jo.put("hazard",hjo);
			//所属专业
			JSONObject spjo=new JSONObject();
			if(unsafeCondition.getSpeciality()!=null){
				spjo.put("isnull", false);
				spjo.put("specialitySn", unsafeCondition.getSpeciality().getSpecialitySn());
				spjo.put("specialityName", unsafeCondition.getSpeciality().getSpecialityName());
			}else{
				spjo.put("isnull", true);
			}
			jo.put("speciality",spjo);
			jo.put("inconformityLevel",unsafeCondition.getInconformityLevel());//不符合项等级
			//录入人
			JSONObject ejo=new JSONObject();
			if(unsafeCondition.getEditor()!=null){
				ejo.put("isnull",false);
				ejo.put("personId",unsafeCondition.getEditor().getPersonId());
				ejo.put("personName",unsafeCondition.getEditor().getPersonName());
			}else{
				ejo.put("isnull",true);
			}
			jo.put("editor", ejo);
			//整改负责人
			JSONObject cpjo=new JSONObject();
			if(unsafeCondition.getCorrectPrincipal()!=null){
				cpjo.put("isnull",false);
				cpjo.put("personId",unsafeCondition.getCorrectPrincipal().getPersonId());
				cpjo.put("personName",unsafeCondition.getCorrectPrincipal().getPersonName());
			}else{
				cpjo.put("isnull",true);
			}
			jo.put("correctPrincipal", cpjo);
			jo.put("correctProposal",unsafeCondition.getCorrectProposal());//整改建议
			//检查人员
			JSONObject cjo=new JSONObject();
			int i=0;
			String name="";
			String pIds="";
			for(Person person:unsafeCondition.getCheckers()){
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
			jo.put("attachment",unsafeCondition.getAttachments().size());
			
			//审核方法
			JSONObject amjo=new JSONObject();
			String count="";
			String method="";
			String methodId="";
			int g=0;
			for(Entry<String,Integer> am:unsafeCondition.getAuditMethods().entrySet()){
				StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodDao.getBySn(am.getKey());
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
			array.add(jo);
		}
		hql=hql.replaceFirst("u", "count(u)");
		JSONObject jo=new JSONObject();
		jo.put("total", unsafeConditionDao.countHql(hql));
		jo.put("rows", array);
		return jo;
	}
	//区队考核查询不安全行为详情
	@Override
	public JSONObject queryUnsafeAct(String departmentSn, String year, String month, String value, int page, int rows) {
		JSONArray array=new JSONArray();
		String hql="select u from UnsafeAct u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.checkedDepartment.departmentSn like '"+departmentSn+"%'";
		if(!value.equals("3")){
			hql+=" and u.unsafeActStandard.unsafeActLevel="+value;
		}
		for(UnsafeAct unsafeAct:unsafeActDao.findByPage(hql, page, rows)){
			JSONObject jo=new JSONObject();
			jo.put("id", unsafeAct.getId());//id
			jo.put("inconformityItemSn",unsafeAct.getInconformityItemSn());//编号
			if(unsafeAct.getCheckDateTime()!=null){
				jo.put("checkDateTime", unsafeAct.getCheckDateTime().toString());//检查时间
			}
			jo.put("checkLocation", unsafeAct.getCheckLocation());//检查地
			jo.put("inconformityItemNature",unsafeAct.getInconformityItemNature());//不符合项性质
			jo.put("checkType", unsafeAct.getCheckType());
			jo.put("checkerFrom", unsafeAct.getCheckerFrom());
			Set<Person> persons=unsafeAct.getCheckers();
			if(persons.size()>0){
				String name="";
				String pId="";
				for(Person per:persons){
					name+=per.getPersonName()+",";
					pId+=per.getId()+",";
				}
				name=name.substring(0, name.length()-1);
				pId=pId.substring(0, pId.length()-1);
				jo.put("checkersId", pId);
				jo.put("checkers", name);
			}else{
				jo.put("checkersId", "无");
				jo.put("checkers", "无");
			}
			if(unsafeAct.getSystemAudit()!=null){
				jo.put("systemAudit", unsafeAct.getSystemAudit().getSystemAuditType());
				jo.put("systemAuditSn", unsafeAct.getSystemAudit().getAuditSn());
			}
			if(unsafeAct.getCheckedDepartment()!=null){
				jo.put("checkedDepartmentSn",unsafeAct.getCheckedDepartment().getDepartmentSn());
				if(unsafeAct.getCheckedDepartment().getImplDepartmentName()!=null&&!unsafeAct.getCheckedDepartment().getImplDepartmentName().equals(unsafeAct.getCheckedDepartment().getDepartmentName())){
					jo.put("checkedDepartment",unsafeAct.getCheckedDepartment().getImplDepartmentName()+unsafeAct.getCheckedDepartment().getDepartmentName());
				}else{
					jo.put("checkedDepartment",unsafeAct.getCheckedDepartment().getDepartmentName());
				}
				
			}
			if(unsafeAct.getSpeciality()!=null){
				jo.put("specialitySn", unsafeAct.getSpeciality().getSpecialitySn());
				jo.put("speciality", unsafeAct.getSpeciality().getSpecialityName());
			}
			jo.put("inconformityLevel",unsafeAct.getInconformityLevel());//不符合项等级
			jo.put("attachment",unsafeAct.getAttachments().size());
			if(unsafeAct.getViolator()!=null){
				jo.put("violator", unsafeAct.getViolator().getPersonName());
				jo.put("violatorId", unsafeAct.getViolator().getPersonId());
			}
			jo.put("actDescription", unsafeAct.getActDescription());
			jo.put("unsafeActMark", unsafeAct.getUnsafeActMark());
			if(unsafeAct.getUnsafeActStandard()!=null){
				jo.put("unsafeActStandard", unsafeAct.getUnsafeActStandard().getStandardDescription());
				jo.put("unsafeActStandardSn", unsafeAct.getUnsafeActStandard().getStandardSn());
				jo.put("unsafeActLevel", unsafeAct.getUnsafeActStandard().getUnsafeActLevel());
			}
			if(unsafeAct.getEditor()!=null){
				jo.put("editor", unsafeAct.getEditor().getPersonName());
				jo.put("editorId", unsafeAct.getEditor().getPersonId());
			}else{
				jo.put("editor", "无");
				jo.put("editorId", "无");
			}
			array.add(jo);
		}
		JSONObject jo=new JSONObject();
		hql=hql.replaceFirst("u", "count(u)");
		jo.put("total", unsafeActDao.countByHql(hql));
		jo.put("rows", array);
		return jo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InputStream exportUnsafeCondition(String departmentSn, String year, String month, String type,
			String value) {
		String hql="select u from UnsafeCondition u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.checkedDepartment.departmentSn like '"+departmentSn+"%'";
		List<UnsafeCondition> list=new ArrayList<UnsafeCondition>();
		if(value.equals("3")){
			if(type.equals("over")){
				hql+=" and ((u.hasCorrectConfirmed=false and u.correctDeadline<'"+LocalDateTime.now()+"') OR (u.hasCorrectConfirmed=true and u.correctDeadline<u.confirmTime))";
			}else if(type.equals("cover")){
				hql+=" and 1<(select count(s) from UnsafeConditionCorrectConfirm s where s.inconformityItem.inconformityItemSn =u.inconformityItemSn)";
			}
		}else{
			hql+=" and u.inconformityLevel="+value;
			if(type.equals("over")){
				hql+=" and ((u.hasCorrectConfirmed=false and u.correctDeadline<'"+LocalDateTime.now()+"') OR (u.hasCorrectConfirmed=true and u.correctDeadline<u.confirmTime))";
			}else if(type.equals("cover")){
				hql+=" and 1<(select count(s) from UnsafeConditionCorrectConfirm s where s.inconformityItem.inconformityItemSn =u.inconformityItemSn)";
			}
		}
		
		list=getSessionFactory().getCurrentSession().createQuery(hql).list();
		return exportUnsafeCondition(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public InputStream exportUnsafeAct(String departmentSn, String year, String month, String value) {
		String hql="select u from UnsafeAct u where u.deleted=false and year(u.checkDateTime)="+year+" and month(u.checkDateTime)="+month+" and u.checkedDepartment.departmentSn like '"+departmentSn+"%'";
		if(!value.equals("3")){
			hql+=" and u.unsafeActStandard.unsafeActLevel="+value;
		}
		List<UnsafeAct> list=new ArrayList<UnsafeAct>();
		list=getSessionFactory().getCurrentSession().createQuery(hql).list();
		return exportUnsafeAct(list);
	}

}
