package cn.jagl.aq.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.stereotype.Repository;

import cn.jagl.aq.common.dao.impl.BaseDaoHibernate5;
import cn.jagl.aq.dao.DepartmentDao;
import cn.jagl.aq.dao.StandardIndexAuditMethodDao;
import cn.jagl.aq.dao.StandardIndexDao;
import cn.jagl.aq.dao.SystemAuditDao;
import cn.jagl.aq.dao.UnitAppraisalsDao;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.Person;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.aq.domain.SystemAudit;
import cn.jagl.aq.domain.UnitAppraisals;
import cn.jagl.aq.domain.UnsafeCondition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mahui
 * @method 
 * @date 2016��8��17������6:14:54
 */
@Repository("unitAppraisalsDao")
@SuppressWarnings("unchecked")
public class UnitAppraisalsDaoImpl extends BaseDaoHibernate5<UnitAppraisals> implements UnitAppraisalsDao {
	@Resource(name="standardIndexAuditMethodDao")
	private StandardIndexAuditMethodDao standardIndexAuditMethodDao;
	@Resource(name="standardIndexDao")
	private StandardIndexDao standardIndexDao;
	@Resource(name="departmentDao")
	private DepartmentDao departmentDao;
	@Resource(name="systemAuditDao")
	private SystemAuditDao systemAuditDao;
	
	/**
	 * @method ��ȫ������������
	 * @author mahui
	 * @return String
	 */
	@Override
	public InputStream exportUnsafeCondition(String departmentSn, LocalDate localdate) {
		String hql="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and (date(checkDateTime)='"+localdate+"' OR (date(checkDateTime)<'"+localdate+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+localdate+"')))";
		List<UnsafeCondition> list=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		
		HSSFWorkbook wb=new HSSFWorkbook();
		HSSFSheet sheet=wb.createSheet("����");
		sheet.setDefaultColumnWidth(17);
		sheet.setColumnWidth(0, 70*70);
		sheet.setColumnWidth(5, 90*90);
		sheet.setColumnWidth(8, 70*70);
		sheet.setColumnWidth(9, 75*75);
		sheet.setColumnWidth(13, 90*90);
		//��ʽһ�����⣩
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("����");
        font1.setFontHeightInPoints((short) 12);
        style1.setFont(font1);
        font1.setBold(true);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style1.setWrapText(true);
        
        //��ʽ�������ݣ�
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("����");
        font2.setFontHeightInPoints((short) 11);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        
        //��ʽ������
        HSSFDataFormat format = wb.createDataFormat();
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font2);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setWrapText(true);
        style3.setDataFormat(format.getFormat("yyyy-MM-dd hh:mm"));
        
        //���ɱ�ͷ
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("���ʱ��");  
        cell.setCellStyle(style1);  
        cell = row.createCell(1);  
        cell.setCellValue("���ص�");  
        cell.setCellStyle(style1);  
        cell = row.createCell(2);  
        cell.setCellValue("������������");  
        cell.setCellStyle(style1);
        cell = row.createCell(3);
        cell.setCellValue("��");  
        cell.setCellStyle(style1); 
        cell = row.createCell(4);
        cell.setCellValue("���첿��");  
        cell.setCellStyle(style1); 
        cell = row.createCell(5);
        cell.setCellValue("��������");  
        cell.setCellStyle(style1);
        cell = row.createCell(6);  
        cell.setCellValue("�۷�");  
        cell.setCellStyle(style1); 
        cell = row.createCell(7);  
        cell.setCellValue("��������ȼ�");  
        cell.setCellStyle(style1);
        cell = row.createCell(8);
        cell.setCellValue("��������");  
        cell.setCellStyle(style1);
        cell = row.createCell(9);
        cell.setCellValue("���Ľ���");  
        cell.setCellStyle(style1);
        cell = row.createCell(10);
        cell.setCellValue("����ȷ��");  
        cell.setCellStyle(style1);
        cell = row.createCell(11);
        cell.setCellValue("�Ѹ���");  
        cell.setCellStyle(style1);
        cell = row.createCell(12);
        cell.setCellValue("�������");  
        cell.setCellStyle(style1);
        cell = row.createCell(13);
        cell.setCellValue("�۷�ָ��");  
        cell.setCellStyle(style1);
        cell = row.createCell(14);
        cell.setCellValue("Σ��Դ");  
        cell.setCellStyle(style1);
        cell = row.createCell(15);
        cell.setCellValue("�۷���");  
        cell.setCellStyle(style1);
        cell = row.createCell(16);
        cell.setCellValue("רҵ");  
        cell.setCellStyle(style1);
        cell = row.createCell(17);
        cell.setCellValue("�����");  
        cell.setCellStyle(style1);
        cell = row.createCell(18);
        cell.setCellValue("���ĸ�����");  
        cell.setCellStyle(style1);
        cell = row.createCell(19);
        cell.setCellValue("¼����");  
        cell.setCellStyle(style1);
        
        int rownum=1;
		for(UnsafeCondition u:list){
			row=sheet.createRow(rownum);
			rownum++;
			//���ʱ��
	        cell=row.createCell(0);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCheckDateTime()!=null){
	        	cell.setCellValue(u.getCheckDateTime());  
	        }
	        
	        cell.setCellStyle(style3);  
	        
	        //���ص�
	        cell = row.createCell(1);  
	        cell.setCellValue(u.getCheckLocation());  
	        cell.setCellStyle(style2); 
	        
	        //������������
	        cell = row.createCell(2);  
	        cell.setCellValue(u.getInconformityItemNature()==null? "":u.getInconformityItemNature().toString());  
	        cell.setCellStyle(style2);
	        
	        //��
	        cell = row.createCell(3);
	        if(u.getMachine()!=null){
	        	cell.setCellValue(u.getMachine().getManageObjectName()); 
	        } 
	        cell.setCellStyle(style2);
	        
	        //���첿��
	        cell = row.createCell(4);
	        if(u.getCheckedDepartment()!=null){
	        	cell.setCellValue(u.getCheckedDepartment().getDepartmentName());  	
	        }
	        cell.setCellStyle(style2); 
	        
	        //��������
	        cell = row.createCell(5);
	        cell.setCellValue(u.getProblemDescription());  
	        cell.setCellStyle(style2);
	        
	        //�۷�
	        cell = row.createCell(6);  
	        cell.setCellValue(u.getDeductPoints());  
	        cell.setCellStyle(style2); 
	        
	        //��������ȼ�
	        cell = row.createCell(7);  
	        cell.setCellValue(u.getInconformityLevel()==null ?"":u.getInconformityLevel().toString());  
	        cell.setCellStyle(style2);
	        
	        //��������
	        cell = row.createCell(8);
	        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        if(u.getCorrectDeadline()!=null){
	        	cell.setCellValue(u.getCorrectDeadline());  
	        }
	        cell.setCellStyle(style3);
	        
	        //���Ľ���
	        cell = row.createCell(9);
	        cell.setCellValue(u.getCorrectProposal());  
	        cell.setCellStyle(style2);
	        
	        //����ȷ��
	        cell = row.createCell(10);
	        cell.setCellValue((u.getHasCorrectConfirmed()!=null && u.getHasCorrectConfirmed()==true)?"��":"��");  
	        cell.setCellStyle(style2);
	        
	        //�Ѹ���
	        cell = row.createCell(11);
	        cell.setCellValue(u.getHasReviewed()!=null && u.getHasReviewed()==true ?"��":"��");  
	        cell.setCellStyle(style2);
	        
	        //�������
	        cell = row.createCell(12);
	        cell.setCellValue(u.getHasCorrectFinished()!=null && u.getHasCorrectFinished()==true?"��":"��");  
	        cell.setCellStyle(style2);
	        
	        //�۷�ָ��
	        cell = row.createCell(13);
	        cell.setCellValue(u.getStandardIndex()!=null?u.getStandardIndex().getIndexName():"");  
	        cell.setCellStyle(style2);
	        
	        //Σ��Դ
	        cell = row.createCell(14);
	        cell.setCellValue(u.getHazrd()!=null ? u.getHazrd().getHazardDescription():"");  
	        cell.setCellStyle(style2);
	        
	        //�۷���
			String method="";
			int g=0;
			for(Entry<String,Integer> am:u.getAuditMethods().entrySet()){
				StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodDao.getBySn(am.getKey());
				if( standardIndexAuditMethod != null ){
					g++;
					//������
					method+=standardIndexAuditMethod.getAuditMethodContent()+",";
				}
			}
			if(g>0){
				method=method.substring(0, method.length()-1);
			}
	        cell = row.createCell(15);
	        cell.setCellValue(method);  
	        cell.setCellStyle(style2);
	        
	        //רҵ
	        cell = row.createCell(16);
	        cell.setCellValue(u.getSpeciality()!=null ? u.getSpeciality().getSpecialityName():"");
	        cell.setCellStyle(style2);
	        
	        //�����
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
	        
	        //���ĸ�����
	        cell = row.createCell(18);
	        cell.setCellValue(u.getCorrectPrincipal()!=null ? u.getCorrectPrincipal().getPersonName():"");  
	        cell.setCellStyle(style2);
	        
	        //¼����
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
	//�鿴�۷�����
	@Override
	public JSONArray scoreDetail(String departmentSn, LocalDate localdate,String ksDate,String jsDate,Byte type) {
		JSONArray array=new JSONArray();
		//��ȡ��������
		String hql="";
		if(type!=null && type==1){
			if(localdate!=null && !"".equals(localdate)){
				hql="SELECT u FROM UnsafeCondition u where u.checkerFrom<2 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and (date(checkDateTime)='"+localdate+"' OR (date(checkDateTime)<'"+localdate+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+localdate+"')))";			
			}else{
				hql="SELECT u FROM UnsafeCondition u where u.checkerFrom<2 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and u.checkDateTime between '"+ksDate+" 00:00:00' and '"+jsDate+" 23:59:59'";
			}
		}else{
			if(localdate!=null && !"".equals(localdate)){
				hql="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and (date(checkDateTime)='"+localdate+"' OR (date(checkDateTime)<'"+localdate+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+localdate+"')))";			
			}else{
				hql="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+departmentSn+"%' and u.checkDateTime between '"+ksDate+" 00:00:00' and '"+jsDate+" 23:59:59'";
			}
		}
		List<UnsafeCondition> list=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		
		//ѭ������
		for(UnsafeCondition unsafeCondition:list){
			StandardIndex index=unsafeCondition.getStandardIndex();
			if(index!=null){
				JSONObject jo=new JSONObject();
				jo.put("sn", unsafeCondition.getInconformityItemSn());
				if(unsafeCondition.getCheckDateTime()!=null){
					jo.put("checkDateTime", unsafeCondition.getCheckDateTime().toString());//���ʱ��
				}
				jo.put("checkLocation", unsafeCondition.getCheckLocation());//����
				jo.put("inconformityItemNature",unsafeCondition.getInconformityItemNature());//������������
				jo.put("deductPoints",unsafeCondition.getDeductPoints());
				jo.put("hasReviewed", unsafeCondition.getHasReviewed());
				jo.put("hasCorrectConfirmed", unsafeCondition.getHasCorrectConfirmed());
				jo.put("hasCorrectFinished", unsafeCondition.getHasCorrectFinished());
				jo.put("checkType", unsafeCondition.getCheckType());
				jo.put("strCheckers", unsafeCondition.getStrCheckers());
				jo.put("checkerFrom",unsafeCondition.getCheckerFrom());
				//��
				JSONObject mjo=new JSONObject();
				if(unsafeCondition.getMachine()!=null){
					mjo.put("isnull",false);
					mjo.put("manageObjectSn", unsafeCondition.getMachine().getManageObjectSn());
					mjo.put("manageObjectName", unsafeCondition.getMachine().getManageObjectName());
				}else{
					mjo.put("isnull", true);
				}
				jo.put("machine", mjo);
				//���첿��
				JSONObject chdjo=new JSONObject();
				if(unsafeCondition.getCheckedDepartment()!=null){
					chdjo.put("isnull", false);
					chdjo.put("departmentSn", unsafeCondition.getCheckedDepartment().getDepartmentSn());
					chdjo.put("departmentName",  unsafeCondition.getCheckedDepartment().getDepartmentName());
				}else{
					chdjo.put("isnull", true);
				}
				jo.put("checkedDepartment", chdjo);
				//ָ��
				jo.put("indexSn", index.getIndexSn());
				jo.put("indexName", index.getIndexName());
				jo.put("percentageScore", index.getPercentageScore());
				jo.put("isKeyIndex", index.getIsKeyIndex());
				jo.put("jointIndexIdCode", index.getJointIndexIdCode());
				jo.put("problemDescription",unsafeCondition.getProblemDescription());//��������
				jo.put("deductPoints", unsafeCondition.getDeductPoints());//�۷�
				if(unsafeCondition.getCorrectType()!=null){
					jo.put("correctType", unsafeCondition.getCorrectType());
				}else{
					jo.put("correctType", "");
				}	
				if(unsafeCondition.getCorrectDeadline()!=null){
					jo.put("correctDeadline", unsafeCondition.getCorrectDeadline());//��������
				}
				//��Ӧ��Σ��Դ
				JSONObject hjo=new JSONObject();
				if(unsafeCondition.getHazrd()!=null){
					hjo.put("isnull",false);
					hjo.put("hazardSn",unsafeCondition.getHazrd().getHazardSn());
					hjo.put("hazardDescription", unsafeCondition.getHazrd().getHazardDescription());
				}else{
					hjo.put("isnull", true);
				}
				jo.put("hazard",hjo);
				//����רҵ
				JSONObject spjo=new JSONObject();
				if(unsafeCondition.getSpeciality()!=null){
					spjo.put("isnull", false);
					spjo.put("specialitySn", unsafeCondition.getSpeciality().getSpecialitySn());
					spjo.put("specialityName", unsafeCondition.getSpeciality().getSpecialityName());
				}else{
					spjo.put("isnull", true);
				}
				jo.put("speciality",spjo);
				jo.put("inconformityLevel",unsafeCondition.getInconformityLevel());//��������ȼ�
				//¼����
				JSONObject ejo=new JSONObject();
				if(unsafeCondition.getEditor()!=null){
					ejo.put("isnull",false);
					ejo.put("personId",unsafeCondition.getEditor().getPersonId());
					ejo.put("personName",unsafeCondition.getEditor().getPersonName());
				}else{
					ejo.put("isnull",true);
				}
				jo.put("editor", ejo);
				//���ĸ�����
				JSONObject cpjo=new JSONObject();
				if(unsafeCondition.getCorrectPrincipal()!=null){
					cpjo.put("isnull",false);
					cpjo.put("personId",unsafeCondition.getCorrectPrincipal().getPersonId());
					cpjo.put("personName",unsafeCondition.getCorrectPrincipal().getPersonName());
				}else{
					cpjo.put("isnull",true);
				}
				jo.put("correctPrincipal", cpjo);
				jo.put("correctProposal",unsafeCondition.getCorrectProposal());//���Ľ���
				//�����Ա
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
				
				//��˷���
				JSONObject amjo=new JSONObject();
				String count="";
				String method="";
				String methodId="";
				int g=0;
				for(Entry<String,Integer> am:unsafeCondition.getAuditMethods().entrySet()){
					StandardIndexAuditMethod standardIndexAuditMethod=standardIndexAuditMethodDao.getBySn(am.getKey());
					if(standardIndexAuditMethod!=null){
						g++;
						//������
						if(standardIndexAuditMethod.getAuditMethodContent()!=null){
							method+=standardIndexAuditMethod.getAuditMethodContent()+",";
						}else{
							method+="null"+",";
						}
						//����Id
						methodId+=standardIndexAuditMethod.getId()+",";
						//����
						count+=am.getValue()+",";
						standardIndexAuditMethod.getId();
					}
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
		}
		return array;
	}
	
	//jsonarray����
	private void sort(JSONArray rows,final String field, boolean isAsc){
		Collections.sort(rows, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject o1, JSONObject o2) {
				String[] f1 = o1.getString(field).split("\\.");
				String[] f2 = o2.getString(field).split("\\.");
				int result=Integer.parseInt(f1[1])-Integer.parseInt(f2[1]);
				if(result==0){
					result=Integer.parseInt(f1[2])-Integer.parseInt(f2[2]);
				}
				return result;
			}
		});
		if(!isAsc){
			Collections.reverse(rows);
		}
		//return rows;
	}
	//�۷�����
	private JSONObject scoreDetail(UnitAppraisals unitAppraisals){
		String hql="";
		if(unitAppraisals.getType()==0){
			hql="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+unitAppraisals.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')))";	
		}else{
			hql="SELECT u FROM UnsafeCondition u where u.checkerFrom<2 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+unitAppraisals.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')))";	
		}
		List<UnsafeCondition> list=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		//������ж���ָ��
		HashMap<String,ArrayList<UnsafeCondition>> second=new HashMap<String,ArrayList<UnsafeCondition>>();
		//��Ŷ���ָ���µ����п۷���
		HashMap<String,Set<Integer>> map=new HashMap<String,Set<Integer>>();
		int order=0;
		JSONArray rows=new JSONArray();
		for(UnsafeCondition u:list){
			StandardIndex index=u.getStandardIndex();
			if(index!=null){
				JSONObject jo=new JSONObject();
				jo.put("problem", u.getProblemDescription());
				jo.put("indexSn", index.getIndexSn());
				jo.put("dscore",u.getDeductPoints());
				if(index.getIsKeyIndex()!=null && index.getIsKeyIndex()==true){
					jo.put("isKey", "��");
				}else{
					jo.put("isKey", "��");
				}
				
				StandardIndex secondIndex=index;
				if(secondIndex!=null){
					while(secondIndex.getParent().getParent()!=null){
						secondIndex=secondIndex.getParent();
					}
				}
				jo.put("secondIndexSn", secondIndex.getIndexSn());
				jo.put("secondScore", secondIndex.getPercentageScore());
				
				//��Ŷ���ָ�����Ԫ��
				if(map.containsKey(secondIndex.getIndexSn())){
					Set<Integer> set=map.get(secondIndex.getIndexSn());
					ArrayList<UnsafeCondition> unlist=second.get(secondIndex.getIndexSn());
					set.add(order);
					unlist.add(u);
					map.put(secondIndex.getIndexSn(), set);
					second.put(secondIndex.getIndexSn(), unlist);
				}else{
					Set<Integer> set=new HashSet<Integer>();
					ArrayList<UnsafeCondition> unlist=new ArrayList<UnsafeCondition>();
					set.add(order);
					unlist.add(u);
					map.put(secondIndex.getIndexSn(), set);
					second.put(secondIndex.getIndexSn(), unlist);
				}
				
				rows.add(jo);
				order++;
			}
		}
		JSONArray newRow=new JSONArray();
		double allDescore=0;//��¼�ܿ۷�
		double allScore=0;//��¼�ܵ÷�
		double allPoint=0;//��¼�ܷ�
		for (Entry<String, Set<Integer>> entry : map.entrySet()) {
			float dscore=this.scoreSecond(second.get(entry.getKey()));
			allDescore+=dscore;
			double score=0;
			double secondScore=0;
			int j=0;
			for(int i:entry.getValue()){
				JSONObject jo=rows.getJSONObject(i);
				if(j==0){
					secondScore=jo.getDouble("secondScore");
					score=secondScore-dscore;
				}
				jo.put("score", score);
				newRow.add(jo);
				j++;
			}
			allScore+=score;
			allPoint+=secondScore;
			j=0;
		}
		this.sort(newRow, "secondIndexSn", true);
		JSONObject data=new JSONObject();
		data.put("rows", newRow);
		data.put("allDescore", allDescore);
		data.put("allScore", allScore);
		data.put("allPoint", allPoint);
		return data;
	}
	
	//����ָ���½��д��
	private float scoreSecond(List<UnsafeCondition> list){
		//float score=0;
		//�ܿ۷�
		float descore=0f;
		//������Ͽ۷�ʱ�Ŀ۷�ָ�ꡢ����
		HashMap<StandardIndex,Float> map=new HashMap<StandardIndex,Float>();
		//�������ָ�꣨��ʶ�롢������
		HashMap<String,Float> jointMap=new HashMap<String,Float>();
		//������ָ��֮��͹ؼ�ָ���µĿ۷ֵ�����ָ��
		HashMap<StandardIndex,Float> other=new HashMap<StandardIndex,Float>();
		//������ж���ָ��
		//Set<StandardIndex> second=new HashSet<StandardIndex>();
		
		/**
		 * ѭ������
		 * ���ã�
		 * 1�������е����Ͽ۷�ָ���Լ����۷�������map��
		 * 2��ɸѡ��������ָ��
		 */
		for(UnsafeCondition unsafeCondition:list){
			StandardIndex index=unsafeCondition.getStandardIndex();
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
					//��ȡ���ִ���
					count+=am.getValue();
				}
				//��������map(�Ѿ��ж��Ƿ񳬷�)
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
		
		//�жϹ���ָ���Ƿ��������Ͽ۷ֵĹؼ�ָ��,�����������map
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			for(Entry<String,Float> bm:jointMap.entrySet()){
				if(bm.getKey().contains(am.getKey().getIndexSn())){
					//�������
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
		
		//��Թؼ�ָ�꼰��ͬ����ֿ�ʼ
		for(UnsafeCondition unsafeCondition:list){
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
		JSONObject jo=new JSONObject();
		//����map�ܿ۷�
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			descore+=am.getValue();
		}
		for(Entry<String,Float> bm:jointMap.entrySet()){
			descore+=bm.getValue();
		}
		for(Entry<StandardIndex,Float> cm:other.entrySet()){
			descore+=cm.getValue();
		}
		jo.put("descore", descore);
		return descore;
	}
	//�۷�
	private JSONObject scoreSingle(UnitAppraisals unitAppraisals){
		//�ܷ�
		float score=0;
		//�ܿ۷�
		float descore=0f;
		//������Ͽ۷�ʱ�Ŀ۷�ָ�ꡢ����
		HashMap<StandardIndex,Float> map=new HashMap<StandardIndex,Float>();
		//�������ָ�꣨��ʶ�롢������
		HashMap<String,Float> jointMap=new HashMap<String,Float>();
		//������ָ��֮��͹ؼ�ָ���µĿ۷ֵ�����ָ��
		HashMap<StandardIndex,Float> other=new HashMap<StandardIndex,Float>();
		//������ж���ָ��
		Set<StandardIndex> second=new HashSet<StandardIndex>();
		//��ѯ���ο۷����е�����
		//String hql2="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')";			
		String hql="";
		if(unitAppraisals.getType()==0){
			hql="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+unitAppraisals.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')))";	
		}else{
			hql="SELECT u FROM UnsafeCondition u where u.checkerFrom<2 and u.deleted=false and u.checkedDepartment.departmentSn like '"+unitAppraisals.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+unitAppraisals.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+unitAppraisals.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+unitAppraisals.getAppraisalsDate()+"')))";
		}
		List<UnsafeCondition> list=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		
		JSONObject jo=new JSONObject();
		
		if(list==null || list.size()==0){
			jo.put("shouldDelete", true);
		}else{
			jo.put("shouldDelete", false);
		}
		/**
		 * ѭ������
		 * ���ã�
		 * 1�������е����Ͽ۷�ָ���Լ����۷�������map��
		 * 2��ɸѡ��������ָ��
		 */
		for(UnsafeCondition unsafeCondition:list){
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
						//��ȡ���ִ���
						count+=am.getValue();
					}
					//��������map(�Ѿ��ж��Ƿ񳬷�)
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
				
				//System.out.print(index.getParent());
				//StandardIndex second=index;
				if(index.getParent()!=null){
					while(index.getParent().getParent()!=null){
						index=index.getParent();
					}
				}
				second.add(index);
			}
			
		}
		for(StandardIndex index:second){
			if(index.getPercentageScore()!=null){
				score+=index.getPercentageScore();
			}
		}
		
		//�жϹ���ָ���Ƿ��������Ͽ۷ֵĹؼ�ָ��,�����������map
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			for(Entry<String,Float> bm:jointMap.entrySet()){
				if(bm.getKey().contains(am.getKey().getIndexSn())){
					//�������
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
		
		//��Թؼ�ָ�꼰��ͬ����ֿ�ʼ
		for(UnsafeCondition unsafeCondition:list){
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
		
		//����map�ܿ۷�
		for(Entry<StandardIndex,Float> am:map.entrySet()){
			descore+=am.getValue();
		}
		for(Entry<String,Float> bm:jointMap.entrySet()){
			descore+=bm.getValue();
		}
		for(Entry<StandardIndex,Float> cm:other.entrySet()){
			descore+=cm.getValue();
		}
		jo.put("descore", descore);
		jo.put("score", score);
		return jo;
	}
	//��ʱ����÷�
	@Override
	public void score() {
		String hql="select u from UnitAppraisals u where u.needComputing=true";
		List<UnitAppraisals> list=(List<UnitAppraisals>) getSessionFactory().getCurrentSession()
				.createQuery(hql).list();
		
		for(UnitAppraisals unitAppraisals:list){
			
			try{
				JSONObject jo=scoreSingle(unitAppraisals);
				
				if(jo.getBoolean("shouldDelete")){
					delete(unitAppraisals);
				}else{
					unitAppraisals.setNeedComputing(false);		
					//unitAppraisals.getAppraisalsDate();
					double score=jo.getDouble("score");
					double descore=jo.getDouble("descore");
					double firstscore=0;
					if(score>0){
						firstscore=((score-descore)/score*100);
					}else{
						firstscore=100;
					}
					unitAppraisals.setScore((float) firstscore);
					unitAppraisals.setComputeDatetime(LocalDateTime.now());
					update(unitAppraisals);
				}
			}catch(Exception e){
				continue;
			}
		}
	}
	//����۷�
	private float deduction(UnsafeCondition unsafeCondition,StandardIndex index){
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
	
	//���ݲ��ű�ź����ڲ�ѯʵ��
	@Override
	public UnitAppraisals queryByMany(String departmentSn, LocalDate localDate,Byte type) {
		String hql="select u from UnitAppraisals u WHERE u.department.departmentSn=:departmentSn AND u.appraisalsDate=:localDate and u.type=:type";		
		return (UnitAppraisals) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("departmentSn", departmentSn)
				.setParameter("localDate", localDate)
				.setByte("type", type)
				.uniqueResult();
	}
	//���ݲ��ű�ţ���ݷݻ�ȡ�ò���һ��������·�ƽ����
	@Override
	public JSONArray queryScore(String year, String departmentTypeSn,Byte type) {
		//String sql="SELECT d.department_name,AVG(u.score) as ave FROM unit_appraisals u inner JOIN department d on u.department_sn=d.department_sn where  year(appraisals_date)="+year+" and MONTH(appraisals_date) IN ("+month+") and d.department_type_sn='"+departmentTypeSn+"' GROUP BY u.department_sn ORDER BY avg(score) desc";			
		JSONArray array=new JSONArray();
		
		List<Department> departmentlist=new ArrayList<Department>();
		if(type==0){
			//String hql="select count(r) from Role r inner join r.persons p where p.personId=:personId and r.roleType<2";
			int roleType=(int) ServletActionContext.getRequest().getSession().getAttribute("roleType");
			
			if(roleType==0){
				departmentlist=departmentDao.getDepartments(departmentTypeSn);
			}else if(roleType==1){
				String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
				Department department=departmentDao.getUpNerestFgs(departmentSn);
				String hql="select d from Department d where d.deleted=false and d.departmentType.isImplDepartmentType=true and d.departmentSn like:departmentSn"
						+ " and d.departmentType.departmentTypeSn=:departmentTypeSn";
				departmentlist=(List<Department>) getSessionFactory().getCurrentSession()
						.createQuery(hql)
						.setString("departmentSn", department.getDepartmentSn()+"%")
						.setString("departmentTypeSn", departmentTypeSn)
						.list();
				if(departmentlist==null || departmentlist.size()==0) {
					departmentlist.add(department);
				}
				//departmentlist=departmentDao.getDepartmentsByParentDepartmentSnNotDeleted(department.getDepartmentSn());
			}else{
				String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
				Department department=departmentDao.getUpNearestImplDepartment(departmentSn);
				if(department!=null  && department.getDepartmentType().getDepartmentTypeSn().equals(departmentTypeSn)){
					departmentlist.add(department);
				}
			}
		}else{
			departmentlist=departmentDao.getDepartmentsByParentDepartmentSnNotDeleted(departmentTypeSn);
		}
		
		for(Department department:departmentlist){
			JSONObject jo=new JSONObject();
			jo.put("departmentSn", department.getDepartmentSn());
			jo.put("departmentName", department.getDepartmentName());
			String sql="SELECT MONTH(appraisals_date) as month,AVG(score) as ave FROM unit_appraisals where type="+type+" and year(appraisals_date)="+year+" and department_sn="+department.getDepartmentSn()+" GROUP BY MONTH(appraisals_date) ORDER BY MONTH(appraisals_date)";
			List<Object> list=getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			for(int i=0;i<list.size();i++){
				Map<Object, Object> m = (Map<Object, Object>)list.get(i);
				
				double ave=0;
				
				if(m.get("ave")!=null && Double.valueOf(m.get("ave").toString())>0){
					ave=Double.valueOf(m.get("ave").toString());
					final double temAve=ave;
					Session session = getSessionFactory().getCurrentSession();
					Double result=session.doReturningWork(new ReturningWork<Double>() {
						@Override
						public Double execute(java.sql.Connection connection) throws SQLException {
							CallableStatement cs = connection.prepareCall("call sp_compute_month(?,?,?,?,?,?)");
							cs.setInt(1, type);
							cs.setInt(2, Integer.valueOf(year));//(2, type);
							cs.setInt(3, Integer.valueOf(m.get("month").toString()));
							cs.setString(4, department.getDepartmentSn());
							cs.setDouble(5, temAve);
			                cs.registerOutParameter(6, Types.DOUBLE);
			                cs.executeUpdate();
			                return cs.getDouble(6);
						}
			        });
					
					if(result!=null){
						ave=result;
					}
				}
				jo.put(m.get("month").toString(), ave);
			}
			if(type==0){
				SystemAudit systemAudit;
				//��һ����
				float season1=0;
				int count=0;
				for(int i=1;i<4;i++){
					if(jo.get(String.valueOf(i))!=null){
						if(jo.getDouble(String.valueOf(i))>0) {
							count++;
						}
						season1+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				if(count>0) {
					season1=season1/count*0.6f;
					count=0;
				}
				systemAudit=systemAuditDao.queryScore(department.getDepartmentSn(), 1,year);			
				if(systemAudit!=null && systemAudit.getFinalScore()!=null){
					float quarter=systemAudit.getFinalScore();
					season1+=quarter*0.4;
					jo.put("audit1", systemAudit.getFinalScore());
				}else{
					jo.put("audit1", 0);
				}
							
				//�ڶ�����
				float season2=0;
				for(int i=4;i<7;i++){
					if(jo.get(String.valueOf(i))!=null){
						if(jo.getDouble(String.valueOf(i))>0) {
							count++;
						}
						season2+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				if(count>0) {
					season2=season2/count*0.6f;
					count=0;
				}
				systemAudit=systemAuditDao.queryScore(department.getDepartmentSn(), 2,year);
				if(systemAudit!=null && systemAudit.getFinalScore()!=null){
					float quarter=systemAudit.getFinalScore();
					season2+=quarter*0.4;
					jo.put("audit2", systemAudit.getFinalScore());
				}else{
					jo.put("audit2", 0);
				}
				
				//��������
				float season3=0;
				for(int i=7;i<10;i++){
					if(jo.get(String.valueOf(i))!=null){
						if(jo.getDouble(String.valueOf(i))>0) {
							count++;
						}
						season3+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				if(count>0) {
					season3=season3/count*0.6f;
					count=0;
				}
				systemAudit=systemAuditDao.queryScore(department.getDepartmentSn(), 3,year);
				if(systemAudit!=null && systemAudit.getFinalScore()!=null){
					float quarter=systemAudit.getFinalScore();
					season3+=quarter*0.4;
					jo.put("audit3", systemAudit.getFinalScore());
				}else{
					jo.put("audit3", 0);
				}
				
				//���ļ���
				float season4=0;
				for(int i=10;i<13;i++){
					if(jo.get(String.valueOf(i))!=null){
						if(jo.getDouble(String.valueOf(i))>0) {
							count++;
						}
						season4+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				if(count>0) {
					season4=season4/count*0.6f;
					count=0;
				}
				
				systemAudit=systemAuditDao.queryShScore(department.getDepartmentSn(), year);
				if(systemAudit!=null){
					if(systemAudit.getFinalScore()!=null){
						float quarter=systemAudit.getFinalScore();
						season4+=quarter*0.4;
						jo.put("audit4", systemAudit.getFinalScore());
					}else{
						jo.put("audit4", 0);
					}
				}else{
					systemAudit=systemAuditDao.queryScore(department.getDepartmentSn(), 4,year);
					if(systemAudit!=null && systemAudit.getFinalScore()!=null){
						float quarter=systemAudit.getFinalScore();
						season4+=quarter*0.4;
						jo.put("audit4", systemAudit.getFinalScore());
					}else{
						jo.put("audit4", 0);
					}
				}			
				//���
				float allyear=0;
				int num=0;
				if(season1>0){
					num++;
					allyear+=season1;
					jo.put("season1", season1);
				}else{
					jo.put("season1", 0);
				}
				if(season2>0){
					num++;
					allyear+=season2;
					jo.put("season2", season2);
				}else{
					jo.put("season2", 0);
				}
				if(season3>0){
					num++;
					allyear+=season3;
					jo.put("season3", season3);
				}else{
					jo.put("season3", 0);
				}
				if(season4>0){
					num++;
					allyear+=season4;
					jo.put("season4", season4);
				}else{
					jo.put("season4", 0);
				}
				if(allyear>0){
					allyear=allyear/num;
				}else{
					jo.put("year", 0);
				}
				jo.put("year", allyear);
			}else{
				//��һ����
				float season1=0;
				for(int i=1;i<4;i++){
					if(jo.get(String.valueOf(i))!=null){
						season1+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
							
				//�ڶ�����
				float season2=0;
				for(int i=4;i<7;i++){
					if(jo.get(String.valueOf(i))!=null){
						season2+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				//��������
				float season3=0;
				for(int i=7;i<10;i++){
					if(jo.get(String.valueOf(i))!=null){
						season3+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}
				//���ļ���
				float season4=0;
				for(int i=10;i<13;i++){
					if(jo.get(String.valueOf(i))!=null){
						season4+=jo.getDouble(String.valueOf(i));
					}else{
						jo.put(i, 0);
					}
				}	
				//���
				float allyear=0;
				int num=0;
				if(season1>0){
					num++;
					allyear+=season1;
					jo.put("season1", season1/3);
				}else{
					jo.put("season1", 0);
				}
				if(season2>0){
					num++;
					allyear+=season2;
					jo.put("season2", season2/3);
				}else{
					jo.put("season2", 0);
				}
				if(season3>0){
					num++;
					allyear+=season3;
					jo.put("season3", season3/3);
				}else{
					jo.put("season3", 0);
				}
				if(season4>0){
					num++;
					allyear+=season4;
					jo.put("season4", season4/3);
				}else{
					jo.put("season4", 0);
				}
				if(allyear>0){
					allyear=allyear/num;
				}else{
					jo.put("year", 0);
				}
				jo.put("year", allyear);
			}
			array.add(jo);
		}
		return array;
	}
	//������ݺ��·ݺͲ��ű�Ų�ѯ�ò��Ÿ��µĵ÷�
	@Override
	public List<Object> queryMonth(String year, String month, String departmentSn,Byte type) {
		String sql="SELECT DAY(appraisals_date) as days,score FROM unit_appraisals where type="+type+" and year(appraisals_date)="+year+" and MONTH(appraisals_date)="+month+" and department_sn="+departmentSn+" ORDER BY DAY(appraisals_date)";
		return getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	}
	@Override
	public List<UnitAppraisals> queryByDepartmentDate(String departmentSn, String ksDate, String jsDate) {
		String hql="select u from UnitAppraisals u WHERE u.department.departmentSn=:departmentSn AND u.appraisalsDate between :ksDate and :jsDate";		
		return (List<UnitAppraisals>) getSessionFactory().getCurrentSession()
				.createQuery(hql)
				.setString("departmentSn", departmentSn)
				.setString("ksDate", ksDate)
				.setString("jsDate", jsDate)
				.list();
	}

	//��ѯĳһ��۷�����
	@Override
	public JSONObject socoreByDay(String departmentSn, LocalDate local,Byte type) {
		JSONObject jo=new JSONObject();
		UnitAppraisals unitAppraisals=queryByMany(departmentSn, local,type);
		if(unitAppraisals!=null){
			jo=scoreSingle(unitAppraisals);
		}else{
			jo.put("score", 0);
			jo.put("descore", 0);
		}
		return jo;
	}

	
	//�������������µ�ĳ�¿��˴��˵��
	@Override
	public InputStream exportMonthByDepartmentTypeSn(String departmentTypeSn,String departmentTypeName,
			String year,String month,Byte type) {
		List<Department> departmentlist=new ArrayList<Department>();
		String hql="";
		if(type==0){
			hql="select count(r) from Role r inner join r.persons p where p.personId=:personId and r.roleType<2";
			String personId=(String) ServletActionContext.getRequest().getSession().getAttribute("personId");
			
			long role=(long) getSessionFactory().getCurrentSession().createQuery(hql)
					.setString("personId", personId)
					.uniqueResult();
			
			if(role>0){
				departmentlist=departmentDao.getDepartments(departmentTypeSn);
			}else{
				String departmentSn=(String) ServletActionContext.getRequest().getSession().getAttribute("departmentSn");
				Department department=departmentDao.getUpNearestImplDepartment(departmentSn);
				if(department!=null  && department.getDepartmentType().getDepartmentTypeSn().equals(departmentTypeSn)){
					departmentlist.add(department);
				}
				
			}
		}else{
			departmentlist=departmentDao.getDepartmentsByParentDepartmentSnNotDeleted(departmentTypeSn);
		}
		//����sheet
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("���˴��˵��");     
	    sheet.setDefaultColumnWidth(25);
	    sheet.setColumnWidth(3, 110*110);
	    sheet.setColumnWidth(4, 120*120);
	    //��ʽһ�����⣩
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("����");
        font1.setFontHeightInPoints((short) 18);
        font1.setBold(true);
        style1.setFont(font1);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        //��ʽ������ͷ��
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("����");
        font2.setFontHeightInPoints((short) 10);
        font2.setBold(true);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setWrapText(true);
        
        //��ʽ�������ݾ��ж��룩
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName("����_GB2312");
        font3.setFontHeightInPoints((short) 12);
        style3.setFont(font3);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setWrapText(true);
        
        //��ʽ�ģ���������룩
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font3);
        style4.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setWrapText(true);
        
        //����
        HSSFRow row = sheet.createRow(0);
        double rheight= 27*27 ; 
        row.setHeight((short)rheight);
        HSSFCell cell = row.createCell(0);
        String title=year+"��"+month+"��"+departmentTypeName+"�¶ȿ��˴��˵��";
        cell.setCellValue(title);
        cell.setCellStyle(style1);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        //��ͷ
        row = sheet.createRow(1);
        row.setHeight((short)(25*25));
        String[] titles={"��λ",
        		"���µ÷�",
        		"���μ�����",
        		"ÿ�μ��÷�",
        		"�۷���Ҫԭ��",
        		"��ע"
        		};
        int sn=0;
        for(String head:titles){
        	cell=row.createCell(sn);
        	cell.setCellValue(head);
        	cell.setCellStyle(style2);
        	sn++;
        }
        
        int rowNum=1;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
        DecimalFormat decimalFormat=new DecimalFormat(".00");
		for(Department department:departmentlist){
			rowNum++;
			row = sheet.createRow(rowNum);
			//��λ
			cell=row.createCell(0);
        	cell.setCellValue(department.getDepartmentName());
        	cell.setCellStyle(style3);
        	
        	hql="SELECT u FROM UnitAppraisals u where type="+type+" and year(appraisalsDate)="+year+" and month(appraisalsDate)="+month+"and u.department.departmentSn='"+department.getDepartmentSn()+"' ORDER BY appraisalsDate";
			
			List<UnitAppraisals> list=getSessionFactory().getCurrentSession().createQuery(hql).list();
			
			//������
			cell=row.createCell(2);
        	cell.setCellValue(list.size());
        	cell.setCellStyle(style3);
        	
			String counts="";
			String reasons="";
			Float allscore=0f;
			boolean consist=false;
			int count=0;
			for(UnitAppraisals u:list){
				if(u.getScore()!=null){
					allscore+=u.getScore();
					count++;
				}else{
					continue;
				}
				String reason="";
				String scoreStr=decimalFormat.format(u.getScore());
				counts+=scoreStr+"("+u.getAppraisalsDate().getDayOfMonth()+"��)��";
				
				LocalDate local=u.getAppraisalsDate();
				//�۷�ԭ��
				String hql2="";
				if(type==0){
					hql2="SELECT u FROM UnsafeCondition u where u.checkerFrom>1 and u.deleted=false and u.checkedDepartment.departmentSn like '"+u.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+u.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+u.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+u.getAppraisalsDate()+"')))";
				}else{
					hql2="SELECT u FROM UnsafeCondition u where u.checkerFrom<2 and u.deleted=false and u.checkedDepartment.departmentSn like '"+u.getDepartment().getDepartmentSn()+"%' and (date(checkDateTime)='"+u.getAppraisalsDate()+"' OR (date(checkDateTime)<'"+u.getAppraisalsDate()+"' and (u.hasCorrectConfirmed=false OR u.confirmTime >'"+u.getAppraisalsDate()+"')))";
				}
				List<UnsafeCondition> list2=(List<UnsafeCondition>) getSessionFactory().getCurrentSession()
						.createQuery(hql2).list();
				for(UnsafeCondition un:list2){
					StandardIndex index=un.getStandardIndex();
					if(index!=null && index.getIsKeyIndex()!=null && index.getIsKeyIndex()==true){
						reason+="��"+un.getProblemDescription()+"�����ڹؼ�ָ�꣬�Ӷ���ָ������п�"+un.getDeductPoints()+"�֡�";
					}
					String checkDate=fmt.format(un.getCheckDateTime()); 
					if(java.sql.Date.valueOf(local).after(java.sql.Date.valueOf(checkDate))){ 
						consist=true;
					}
				}
				if(reason.trim().length()>0){
					reason=month+"��"+local.getDayOfMonth()+"��"+reason;
				}
				reasons+=reason;
			}
				
			if(reasons.trim().length()==0){
				reasons="����δ�����ؼ�ָ�����⡣";
			}
			if(consist){
				reasons+="���´��ڲ������δ�������ģ�����ظ��۷֡�";
			}
			//���µ÷�
			cell=row.createCell(1);
        	cell.setCellStyle(style3);
        	if(count>0){
        		cell.setCellValue(decimalFormat.format(allscore/count));
        	}
			//ÿ�μ��÷�
			cell=row.createCell(3);
        	cell.setCellValue(counts);
        	cell.setCellStyle(style4);
        	
        	//�۷���Ҫԭ��
			cell=row.createCell(4);
        	cell.setCellValue(reasons);
        	cell.setCellStyle(style4);
        	
        	//��ע
			cell=row.createCell(5);
        	cell.setCellStyle(style4);
        	
		}
		ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        try {
			wb.write(fout);
			wb.close();
	        fout.close();
	        byte[] fileContent = fout.toByteArray();
	    	InputStream in = new ByteArrayInputStream(fileContent);
	    	return in;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}   	
	}

	
	//����ĳ�����µ�ĳ�¿��˴��˵��
	@Override
	public InputStream exportMonthByDepartmentSn(String departmentSn, String departmentName, String year,
			String month,Byte type) {
		String hql="SELECT u FROM UnitAppraisals u where u.type="+type+" and year(appraisalsDate)="+year+" and month(appraisalsDate)="+month+"and u.department.departmentSn='"+departmentSn+"' ORDER BY appraisalsDate";
		List<UnitAppraisals> list=getSessionFactory().getCurrentSession().createQuery(hql).list();
		DecimalFormat decimalFormat=new DecimalFormat("0.00");
		//����sheet
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("���˴��˵��"); 
		HSSFSheet sheet2 = wb.createSheet("�ϼƴ��");
		sheet2.setDefaultColumnWidth(20);
	    sheet.setDefaultColumnWidth(20);
	    sheet.setColumnWidth(1, 110*110);
	    
	    
	    //��ʽһ�����⣩
        HSSFCellStyle style1 = wb.createCellStyle();
        HSSFFont font1 = wb.createFont();
        font1.setFontName("����");
        font1.setFontHeightInPoints((short) 18);
        font1.setBold(true);
        style1.setFont(font1);
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        //��ʽ������ͷ��
        HSSFCellStyle style2 = wb.createCellStyle();
        HSSFFont font2 = wb.createFont();
        font2.setFontName("����");
        font2.setFontHeightInPoints((short) 12);
        font2.setBold(true);
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setWrapText(true);
        
        //��ʽ�������ݾ��ж��룩
        HSSFCellStyle style3 = wb.createCellStyle();
        HSSFFont font3 = wb.createFont();
        font3.setFontName("����_GB2312");
        font3.setFontHeightInPoints((short) 12);
        style3.setFont(font3);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setWrapText(true);
        
        //��ʽ�ģ���������룩
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font3);
        style4.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setWrapText(true);
        String title=departmentName+year+"��"+month+"�·ݼ���������";
        
        HSSFRow row;
        double rheight= 25*25; 
        HSSFCell cell;
        row=sheet2.createRow(0);
        row.setHeight((short)rheight);
        cell=row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style1);
        for(int i=1;i<list.size()+3;i++){
        	cell=row.createCell(i);
        	cell.setCellStyle(style1);
	    }
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, list.size()+2));
        
        //�ڶ���
        HSSFRow row2=sheet2.createRow(1);
        row2.setHeight((short)rheight);
        HSSFCell cell2=row2.createCell(0);
        cell2.setCellValue("�������");
        cell2.setCellStyle(style2);
        
        //������
        HSSFRow row3=sheet2.createRow(2);
        row3.setHeight((short)rheight);
        HSSFCell cell3=row3.createCell(0);
        cell3.setCellValue("������");
        cell3.setCellStyle(style2);
        
        String[] titles={"���",
        		"�������",
        		"ָ����",
        		"�۷�",
        		"�Ƿ�ؼ�ָ��",
        		"��Ʒ�ֵ",
        		"���ڶ���ָ����",
        		"�÷�",
        		"��ע"
        		};        
		int rowNum=0;
		int cellNum=0;
		float all=0;
		int day=0;
		for(UnitAppraisals u:list){
			cellNum++;
			float score=0;
			cell2=row2.createCell(cellNum);
			cell2.setCellValue(u.getAppraisalsDate().toString());
			cell2.setCellStyle(style2);
			cell3=row3.createCell(cellNum);
			if(u.getScore()!=null){
				score=u.getScore();
				cell3.setCellValue(decimalFormat.format(score));
				day++;
			}else{
				cell3.setCellValue("��δ���");
			}
			cell3.setCellStyle(style2);
			all+=score;
			
			//����
			row=sheet.createRow(rowNum);
			row.setHeight((short)rheight);
			cell = row.createCell(0);
			title=departmentName+u.getAppraisalsDate()+"����������";
	        cell.setCellValue(title);
	        cell.setCellStyle(style1);
	        for(int i=1;i<9;i++){
	        	cell = row.createCell(i);
		        cell.setCellStyle(style1);
	        }
	        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 8));
	        rowNum++;
	        //��ͷ
	        row = sheet.createRow(rowNum);
	        row.setHeight((short)rheight);
	        int sn=0;
	        for(String head:titles){
	        	cell=row.createCell(sn);
	        	cell.setCellValue(head);
	        	cell.setCellStyle(style2);
	        	sn++;
	        }
	        rowNum++;
			JSONObject data=this.scoreDetail(u);
			JSONArray rows=data.getJSONArray("rows");
			String secondIndexSn="";
			int count=1;
			
			for(int i=0;i<rows.size();i++){
				JSONObject jo=rows.getJSONObject(i);
				row=sheet.createRow(rowNum);
				//���
				cell = row.createCell(0);
				cell.setCellValue(i+1);
		        cell.setCellStyle(style3);
		        
		        //�������
		        cell = row.createCell(1);
				cell.setCellValue(jo.getString("problem"));
		        cell.setCellStyle(style4);
		        
		        //ָ����
		        cell = row.createCell(2);
				cell.setCellValue(jo.getString("indexSn"));
		        cell.setCellStyle(style3);
		        
		        //�۷�
		        cell = row.createCell(3);
				cell.setCellValue(jo.get("dscore")!=null?jo.getDouble("dscore"):0);
		        cell.setCellStyle(style3);
		        
		        //�ؼ�ָ��
		        cell = row.createCell(4);
				cell.setCellValue(jo.getString("isKey"));
		        cell.setCellStyle(style3);
		        
		        
		        if(Objects.equals(jo.getString("secondIndexSn"), secondIndexSn)){
		        	count++;
		        	//��Ʒ�ֵ
			        cell = row.createCell(5);
			        cell.setCellStyle(style3);
			        
			        //���ڶ���ָ��
			        cell = row.createCell(6);
			        cell.setCellStyle(style3);
			        
			        //�÷�
			        cell = row.createCell(7);
			        cell.setCellStyle(style3);
		        }else{
		        	if(count>1){
		        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 5, 5));
		        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 6, 6));
		        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 7, 7));
		        		count=1;
		        	}
		        	secondIndexSn=jo.getString("secondIndexSn");
		        	//��Ʒ�ֵ
			        cell = row.createCell(5);
					cell.setCellValue(jo.get("secondScore")!=null?jo.getDouble("secondScore"):0);
			        cell.setCellStyle(style3);
			        
			        //���ڶ���ָ��
			        cell = row.createCell(6);
					cell.setCellValue(secondIndexSn);
			        cell.setCellStyle(style3);
			        
			        //�÷�
			        cell = row.createCell(7);
					cell.setCellValue(jo.get("score")!=null?decimalFormat.format(jo.getDouble("score")):"0");
			        cell.setCellStyle(style3);
		        }
		    
		        //��ע
		        cell = row.createCell(8);
		        cell.setCellStyle(style3);
				rowNum++;
			}
			if(count>1){
        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 5, 5));
        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 6, 6));
        		sheet.addMergedRegion(new CellRangeAddress(rowNum-count, rowNum-1, 7, 7));
        		count=1;
        	}
			row=sheet.createRow(rowNum);
			for(int i=0;i<9;i++){
				cell = row.createCell(i);
				cell.setCellStyle(style3);
			}
			
			cell=row.getCell(0);
			cell.setCellValue("�ϼ�");
			cell=row.getCell(3);
			cell.setCellValue(decimalFormat.format(data.getDouble("allDescore")));
			cell=row.getCell(5);
			cell.setCellValue(decimalFormat.format(data.getDouble("allPoint")));
			cell=row.getCell(7);
			cell.setCellValue(decimalFormat.format(data.getDouble("allScore")));
			rowNum++;
			row=sheet.createRow(rowNum);
			row.setHeight((short)(20*20));
			title="����÷֣�"+u.getScore();
			cell = row.createCell(0);
	        cell.setCellValue(title);
	        cell.setCellStyle(style4);
	        for(int i=1;i<9;i++){
	        	cell = row.createCell(i);
		        cell.setCellStyle(style1);
	        }
	        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 8));
			rowNum++;
		}
		
		cell2=row2.createCell(cellNum+1);
		cell2.setCellValue("�ϼ�");
		cell2.setCellStyle(style2);
		cell3=row3.createCell(cellNum+1);
		cell3.setCellValue(decimalFormat.format(all));
		cell3.setCellStyle(style2);
		
		cell2=row2.createCell(cellNum+2);
		cell2.setCellValue("ƽ����");
		cell2.setCellStyle(style2);
		cell3=row3.createCell(cellNum+2);
		if(day>0){
			cell3.setCellValue(decimalFormat.format(all/day));
		}
		cell3.setCellStyle(style2);
		ByteArrayOutputStream fout = new ByteArrayOutputStream();  
        try {
			wb.write(fout);
			wb.close();
	        fout.close();
	        byte[] fileContent = fout.toByteArray();
	    	InputStream in = new ByteArrayInputStream(fileContent);
	    	return in;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}   
	}

		
}
