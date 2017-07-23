package cn.jagl.aq.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom2.JDOMException;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.DocumentTemplate;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.Pdca;
import cn.jagl.aq.domain.Speciality;
import cn.jagl.aq.domain.Standard;
import cn.jagl.aq.domain.StandardIndex;
import cn.jagl.aq.domain.StandardIndexAuditMethod;
import cn.jagl.util.ExportUtils;
import cn.jagl.util.FileModel;
import cn.jagl.util.ImportUtils;
public class StandardIndexAction extends BaseAction<StandardIndex> {
	private static final long serialVersionUID = 1L;
	private String documentTemplateSn;
	private int id;
	private String indexSn;
	private String indexName;
	private Pdca pdca;
	private Boolean deleted;
	private String auditKeyPoints;
	private Float percentageScore;
	private Integer integerScore;
	private Boolean isKeyIndex;
	private Float anDeduction;
	private int zeroTimes;
	private String	standardSn;
	private String q;
	private String className;
	private String methodName;
	private String titles;
	private String templateSn;
	private String templateName;
	private FileModel fileModel;
	private String parentindexSn;
	private String getTable;
	private String specialitySn;
	private int progress;
	private String message;
	private String title;
	private String hazardSn;
	private String departmentTypeSn;
	private InputStream excelStream;//�����
	private String excelFileName;//�����ļ���
	
	//���ָ���ֶ�
	public PrintWriter out()throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        return out;
	}
	//����ָ���Ų�ѯΣ��Դ
	public String queryHazard(){		
		String hql="select h from Hazard h LEFT JOIN h.standardIndexes s WHERE s.indexSn like '"+indexSn+"'";
		jsonLoad=hazardService.getHazardsByHql(hql);
		return "jsonLoad";
	}
	//�����ȡsession
	public String importSession(){
		progress=(int) session.get("progressValue");
		return "progressValue";
	}
	//������ȡsession
	public String exportSession(){
		if(session.get("status")==null){
			message="unknown,"+session.get("progressValue");
		}else{
			message=(String) session.get("status")+","+session.get("progressValue");
		}		
		return "message";
	}
	public String queryJoinDocumenttTemplate(){
		jsonList=standardindexService.queryJoinDocumentTemplate(documentTemplateSn);
		return SUCCESS;
	}
	//ɾ��
	@SuppressWarnings("unused")
	public void deleteById() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        String str="{\"status\":\"ok\",\"message\":\"ɾ���ɹ���\"}";
		StandardIndex standardIndex=standardindexService.getById(id);
		try{
			standardindexService.deleteById(standardIndex.getIndexSn());;
		}catch(Exception e){
			str="{\"status\":\"nook\",\"message\":\"ɾ��ʧ�ܣ�\"}";
		}
		out().print(str);
        out().flush(); 
        out().close();
	}	
	//����
	public void updateStandardIndex() throws IOException{
		String str="{\"status\":\"ok\",\"message\":\"�޸ĳɹ���\"}";
		StandardIndex standardIndex=standardindexService.getById(id);
		if(standardIndex.getIndexSn().equals(indexSn)){
			if(parentindexSn.length()>0&&parentindexSn!=null&&standardindexService.getByindexSn(parentindexSn)==null){
				str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�������Ų����ڣ�\"}";
			}else{
				standardIndex.setAnDeduction(anDeduction);
				standardIndex.setAuditKeyPoints(auditKeyPoints);
				standardIndex.setIndexName(indexName);
				standardIndex.setIndexSn(indexSn);
				standardIndex.setIntegerScore(integerScore);
				standardIndex.setIsKeyIndex(isKeyIndex);
				standardIndex.setPdca(pdca);
				standardIndex.setPercentageScore(percentageScore);
				standardIndex.setZeroTimes(zeroTimes);
				if(specialitySn!=null&&specialitySn.trim().length()>0){
					for(int i=0;i<specialitySn.split(",").length;i++){
						standardIndex.getSpecialities().add(specialityService.getBySpecialitySn(specialitySn.split(",")[i].trim()));
					}
				}else{
					standardIndex.setSpecialities(null);
				}
				
				if(parentindexSn!=null&&parentindexSn.length()>0){
					standardIndex.setParent(standardindexService.getByindexSn(parentindexSn));
				}else{
					standardIndex.setParent(null);
				}
				try{
					standardindexService.update(standardIndex);
				}catch(Exception e){
					str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
				}
			}
		}else{
			StandardIndex standardIndex1=standardindexService.getByindexSn(indexSn);
			if(standardIndex1!=null&&standardIndex1.getStandard()!=null&&standardIndex1.getStandard().getStandardSn().equals(standardSn)){
				str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�ָ�����Ѵ��ڣ�\"}";
			}else{
				if(parentindexSn.length()>0&&parentindexSn!=null&&standardindexService.getByindexSn(parentindexSn)==null){
					str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�������Ų����ڣ�\"}";
				}else{
					standardIndex.setAnDeduction(anDeduction);
					standardIndex.setAuditKeyPoints(auditKeyPoints);
					standardIndex.setIndexName(indexName);
					standardIndex.setIndexSn(indexSn);
					standardIndex.setIntegerScore(integerScore);
					standardIndex.setIsKeyIndex(isKeyIndex);
					standardIndex.setPdca(pdca);
					standardIndex.setPercentageScore(percentageScore);
					standardIndex.setZeroTimes(zeroTimes);
					if(specialitySn!=null&&specialitySn.trim().length()>0){
						for(int i=0;i<specialitySn.split(",").length;i++){
							standardIndex.getSpecialities().add(specialityService.getBySpecialitySn(specialitySn.split(",")[i].trim()));
						}
					}else{
						standardIndex.setSpecialities(null);
					}
					if(parentindexSn!=null&&parentindexSn.length()>0){
						standardIndex.setParent(standardindexService.getByindexSn(parentindexSn));
					}else{
						standardIndex.setParent(null);
					}
					try{
						standardindexService.update(standardIndex);
					}catch(Exception e){
						str="{\"status\":\"nook\",\"message\":\"�޸�ʧ�ܣ�\"}";
					}
				}
				
			}
		}
		out().print(str);
        out().flush(); 
        out().close();
	}
	//���
	public void save() throws IOException{
		String str="{\"status\":\"ok\",\"message\":\"��ӳɹ���\"}";
		StandardIndex standardIndex1=standardindexService.getByindexSn(indexSn);
		//StandardIndex standardIndex2=standardindexService.getByindexSn(parentindexSn);
		if(standardIndex1!=null&&standardIndex1.getStandard()!=null&&standardIndex1.getStandard().getStandardSn().equals(standardSn)){
			str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�ָ�����Ѿ����ڣ������ظ���ӣ�\"}";
		}else if(parentindexSn.length()>0&&parentindexSn!=null&&standardindexService.getByindexSn(parentindexSn)==null){
			str="{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�������Ų����ڣ�\"}";
		}else{
			StandardIndex standardIndex=new StandardIndex();
			standardIndex.setAnDeduction(anDeduction);
			standardIndex.setAuditKeyPoints(auditKeyPoints);
			standardIndex.setIndexName(indexName);
			standardIndex.setIndexSn(indexSn);
			standardIndex.setIntegerScore(integerScore);
			standardIndex.setIsKeyIndex(isKeyIndex);
			standardIndex.setPdca(pdca);
			standardIndex.setPercentageScore(percentageScore);
			standardIndex.setZeroTimes(zeroTimes);
			standardIndex.setDeleted(false);
			standardIndex.setStandard(standardService.getByStandardSn(standardSn));
			if(specialitySn!=null&&specialitySn.trim().length()>0){
				for(int i=0;i<specialitySn.split(",").length;i++){
					standardIndex.getSpecialities().add(specialityService.getBySpecialitySn(specialitySn.split(",")[i].trim()));
				}
			}
			if(parentindexSn!=null&&parentindexSn.length()>0){
				standardIndex.setParent(standardindexService.getByindexSn(parentindexSn));
			}
			try{
				standardindexService.addStandardIndex(standardIndex);
			}catch(Exception e){
				str = "{\"status\":\"nook\",\"message\":\"���ʧ�ܣ�\"}";
			}
		}
		out().print(str);
        out().flush(); 
        out().close();
	}
	//standardindex�첽����
	@SuppressWarnings("unused")
	public String queryPart() throws IOException{
        jsonList=null;
        String hql="";
        JSONArray tree=new JSONArray();
        String str="PDCA";
		if(indexSn!=null){
			hql="select s FROM StandardIndex s left join s.standard d where s.deleted=false and s.parent.indexSn like '"+indexSn+"' AND d.standardSn like '"+standardSn+"' order by s.showSequence asc";			
			jsonList=standardindexService.getPart(hql);

		}else{
			hql="select s FROM StandardIndex s LEFT JOIN s.standard d where s.parent is null and s.deleted=false and d.standardSn like '"+standardSn+"' order by s.showSequence asc";
			jsonList=standardindexService.getPart(hql);
		}
		for(StandardIndex standardIndex:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", standardIndex.getId());
			jo.put("indexSn", standardIndex.getIndexSn());
			if(standardIndex.getIndexName()!=null){
				jo.put("indexName", standardIndex.getIndexName());
			}else{
				jo.put("indexName", "");
			}
			
			jo.put("pdca", standardIndex.getPdca());
			jo.put("auditKeyPoints",standardIndex.getAuditKeyPoints());
			jo.put("auditMethod",standardIndex.getAuditMethods().size());
			jo.put("percentageScore", standardIndex.getPercentageScore());
			jo.put("integerScore",standardIndex.getIntegerScore());
			jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
			jo.put("anDeduction", standardIndex.getAnDeduction());
			jo.put("zeroTimes", standardIndex.getZeroTimes());
			jo.put("document", standardIndex.getDocumentTemplates().size());
			jo.put("hazard", standardIndex.getHazards().size());
			//רҵ
			JSONObject sp=new JSONObject();
			int num=0;
			String specialitySns="";
			String specialityNames="";
			for(Speciality speciality:standardIndex.getSpecialities()){
				num++;
				specialitySns+=speciality.getSpecialitySn()+",";
				specialityNames+=speciality.getSpecialityName()+",";
			}
			if(num>0){
				specialitySns=specialitySns.substring(0,specialitySns.length()-1);
				specialityNames=specialityNames.substring(0,specialityNames.length()-1);
				sp.put("specialityNames", specialityNames);
				sp.put("specialitySns", specialitySns);
				sp.put("num", num);
			}else{
				sp.put("num", num);
			}
			jo.put("speciality", sp);
			if(standardIndex.getParent()!=null){
				jo.put("parentindexSn", standardIndex.getParent().getIndexSn());
			}else{
				jo.put("parentindexSn", "");
			}
			
			if(standardIndex.getChildren().size()>0){
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			tree.put(jo);
		}
		out().print(tree.toString());
        out().flush(); 
        out().close();
		return "jsonList";
	}
	//��ѯ���ָ��
	public String queryByQ(){
		jsonList=new ArrayList<StandardIndex>();
		DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
		if(departmentType.getStandards().size()>0){
			List<Standard> list=new  ArrayList<Standard>(departmentType.getStandards());
			jsonList=standardindexService.queryByQ(q,list.get(0).getStandardSn(),"���ֱ�׼");
		}
		//jsonList=standardindexService.queryByQ(q,null,"���ֱ�׼");
		return SUCCESS;
	}
	//�鿴��׼ҳ������ģ��
	@SuppressWarnings("unused")
	public String queryDocumentTemplate() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
        JSONArray tree=new JSONArray();
		Set<DocumentTemplate> jsonList =standardindexService.getByindexSn(indexSn).getDocumentTemplates();
		for(DocumentTemplate documentTemplate:jsonList){
			JSONObject jo=new JSONObject();
			jo.put("id", documentTemplate.getId());
			jo.put("documentTemplateSn", documentTemplate.getDocumentTemplateSn());
			jo.put("documentTemplateName", documentTemplate.getDocumentTemplateName());
			tree.put(jo);
		}
		out().print(tree.toString());
        out().flush(); 
        out().close();
		return "jsonList";
	}
	//�������ģ��
	public String addDocumentTemplate(){
		message="ok";
		try{
			StandardIndex standardIndex=standardindexService.getByindexSn(indexSn);
			standardIndex.getDocumentTemplates().add(documentTemplateService.getByDocumentTemplateSn(documentTemplateSn));
			standardindexService.update(standardIndex);
		}catch(Exception e){
			message="nook";
		}		
		return "message";
	}
	//ɾ�����ģ��
	public String deleteDocumentTemplate(){
		message="ok";
		try{
			StandardIndex standardIndex=standardindexService.getByindexSn(indexSn);
			standardIndex.getDocumentTemplates().remove(documentTemplateService.getByDocumentTemplateSn(documentTemplateSn));
			standardindexService.update(standardIndex);
		}catch(Exception e){
			message="nook";
		}
		return "message";
	}
	//���Σ��Դ
	public String addHazard(){
		message="ok";
		try{
			StandardIndex standardIndex=standardindexService.getByindexSn(indexSn);		
			standardIndex.getHazards().add(hazardService.getByHazardSn(hazardSn));
			standardindexService.update(standardIndex);
		}catch(Exception e){
			message="nook";
		}		
		return "message";
	}
	//ɾ��Σ��Դ
	public String removeHazard(){
		message="ok";
		try{
			StandardIndex standardIndex=standardindexService.getByindexSn(indexSn);
			standardIndex.getHazards().remove(hazardService.getByHazardSn(hazardSn));
			standardindexService.update(standardIndex);
		}catch(Exception e){
			message="nook";
		}
		return "message";
	}
	/**
	 * ����ǰ̨�б�Ϊexcel�ļ�
	 * @author ���
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@SuppressWarnings("unused")
	public String export(){
		session.put("status","unknown");
		try{
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet sheet =wb.createSheet("sheet0");
				//��ȡ���������������б�
			if(getTable!=null){
				jsonList=standardindexService.queryJoinCheckTable(id, 1, 10000);
			}else{
				//String hql="select s FROM StandardIndex s WHERE s.deleted=false AND s.standard.standardSn='"+standardSn+"' order by id";
				//jsonList=standardindexService.getPart(hql);
				jsonList=standardindexService.exportByStandardSn(standardSn);
			}
						
			ExportUtils.setColumnWidth(wb, sheet, templateSn,title);
				HSSFRow row;
				HSSFCell cell;
				int headerSize=titles.split(",").length;
				int i=1;
				//���ݸ�ʽ
		        HSSFCellStyle style = wb.createCellStyle();
		        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);;
		        style.setWrapText(true);
				//ѭ��������
		        float m2=jsonList.size();
				for(StandardIndex standardIndex:jsonList){				
						i++;
						float m1=i;
						session.put("progressValue",(int)((m1/m2*20)+80));
						row=sheet.createRow(i);
						int num=0;
						String specialitySns="";
						String specialityNames="";
						for(Speciality speciality:standardIndex.getSpecialities()){
							num++;
							specialitySns+=speciality.getSpecialitySn()+"#";
							specialityNames+=speciality.getSpecialityName()+"#";
						}
						if(num>0){
							specialitySns=specialitySns.substring(0,specialitySns.length()-1);
							specialityNames=specialityNames.substring(0,specialityNames.length()-1);
						}
						//ѭ��������
						for(int j=0;j<headerSize;j++){
							switch(titles.split(",")[j]){
							case "רҵ���":							
								cell=row.createCell(j);
								cell.setCellStyle(style);
								if(specialitySns.trim().length()>0){
									cell.setCellValue(specialitySns);
								}
								break;
							case "רҵ����":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								if(specialityNames.trim().length()>0){
									cell.setCellValue(specialityNames);
								}
								break;
							case "ָ����":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								cell.setCellValue(standardIndex.getIndexSn().toString());
								break;
							case "ָ������":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								if(standardIndex.getIndexName()!=null){
									cell.setCellValue(standardIndex.getIndexName());
								}
								break;
							case "�����׶�":
								if(standardIndex.getPdca()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getPdca().toString());
								}							
								break;
							case "���Ҫ��":
								if(standardIndex.getAuditKeyPoints()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getAuditKeyPoints());
								}							
								break;
							case "�ٷ��Ʒ���":
								if(standardIndex.getPercentageScore()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getPercentageScore().floatValue());
								}
								
								break;
							case "��������":
								if(standardIndex.getIntegerScore()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getIntegerScore());
								}
								
								break;
							case "�Ƿ�ؼ�ָ��":
								if(standardIndex.getIsKeyIndex()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									if(standardIndex.getIsKeyIndex()==false){
										cell.setCellValue("��");
									}else{
										cell.setCellValue("��");
									}
									
								}
							
								break;
							case "���ο۷�":
								if(standardIndex.getAnDeduction()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getAnDeduction());
								}
								
								break;
							case "���ο���":
								if(standardIndex.getZeroTimes()!=null){
									cell=row.createCell(j);
									cell.setCellStyle(style);
									cell.setCellValue(standardIndex.getZeroTimes());
								}
								
								break;
							case "��˷���":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								String method="";
								int z=0;
								for(StandardIndexAuditMethod standardIndexAuditMethod:standardIndex.getAuditMethods()){
									z++;
									method+=standardIndexAuditMethod.getAuditMethodContent();
									if(standardIndex.getIsKeyIndex()!=null && standardIndex.getIsKeyIndex()==true){
										if(standardIndexAuditMethod.getDeduction()!=null && standardIndexAuditMethod.getIndexDeducted()!=null){
											method+="$"+standardIndexAuditMethod.getIndexDeducted().getIndexSn()+"$"+standardIndexAuditMethod.getDeduction();
										}
									}
									method+="#";
								}
								if(method.trim().length()>0){
									method=method.substring(0, method.length()-1);
									cell.setCellValue(method);
								}
								break;
							case "�������":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								if(standardIndex.getParent()!=null){
									cell.setCellValue(standardIndex.getParent().getIndexSn());
								}
								break;
							case "����ָ��":
								cell=row.createCell(j);
								cell.setCellStyle(style);
								cell.setCellValue(standardIndex.getJointIndexIdCode());
								break;
							}	
						}
				}
				session.put("progressValue",0);
				try {
					ByteArrayOutputStream fout = new ByteArrayOutputStream();  
		        	wb.write(fout);
		        	wb.close();
		            fout.close();
		            byte[] fileContent = fout.toByteArray();  
		            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
		            excelStream=is;
		            excelFileName=java.net.URLEncoder.encode(title, "UTF-8")+".xls";
					session.put("status","ok");
					session.put("progressValue",0);
				} catch (IOException e) {
					session.put("status","nook");
					session.put("progressValue",0);
				}
		}catch(Exception e){
			session.put("status","nook");
			session.put("progressValue",0);
		}
		return "export";
	}
	//���ֱ�׼���ݵ���
	public String importData() throws IOException{
		ImportUtils importtUtils=new ImportUtils();
		JSONArray array=importtUtils.upload(templateSn, fileModel,11);		
		HashMap<String ,Integer> record=new HashMap<String ,Integer>();
		String error="";
		String str="";
		int erNum=0;
		if(array.getJSONObject(0).has("error1")){
			for(int i=1;i<=(int)array.getJSONObject(1).get("erNum");i++){
				 error+="����"+i+":"+array.getJSONObject(0).get("error"+i).toString()+"";
			}
			str = "{\"status\":\"nook\",\"message\":\"������֤ʧ�ܣ�\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+array.getJSONObject(1).getInt("erNum")+"}";
			out().print(str);
	        out().flush(); 
	        out().close();
		}else{
			float m2=array.length();
			for(int i=0;i<array.length();i++){
				float m1=i;
				session.put("progressValue",(int)(m1/m2*100));
				try{					
					if(record.containsKey((String) array.getJSONObject(i).get("indexSn"))){
						erNum++;
						int j=i+3;
						error+="����"+erNum+"����"+j+"�����ݵ�����󣡱�����"+record.get((String) array.getJSONObject(i).get("indexSn"))+"�б���ظ���";
						continue;
					}else{
						record.put((String) array.getJSONObject(i).get("indexSn"), i+3);
					}
				
					StandardIndex standardIndex=new StandardIndex();
					standardIndex.setDeleted(false);
					standardIndex.setStandard(standardService.getByStandardSn(standardSn));
					if(array.getJSONObject(i).get("anDeduction")!="null" && (Float)array.getJSONObject(i).get("anDeduction")!=0f)
						standardIndex.setAnDeduction((Float) array.getJSONObject(i).get("anDeduction"));
					if(array.getJSONObject(i).get("indexName")!="null")
						standardIndex.setIndexName((String) array.getJSONObject(i).get("indexName"));
					if(array.getJSONObject(i).get("indexSn")!="null")
						standardIndex.setIndexSn(array.getJSONObject(i).get("indexSn").toString().trim());
					if(array.getJSONObject(i).get("isKeyIndex")!="null"){
						if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
							standardIndex.setIsKeyIndex(true);
						}else if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
							standardIndex.setIsKeyIndex(false);
						}
					}
					if(array.getJSONObject(i).get("jointIndexIdCode")!="null"){
						standardIndex.setJointIndexIdCode(array.getJSONObject(i).get("jointIndexIdCode").toString().trim());
					}
					//רҵ
					if(array.getJSONObject(i).get("specialitySn")!="null"&&array.getJSONObject(i).get("specialitySn").toString().trim().length()>0){
						String []specialitySn=((String) array.getJSONObject(i).get("specialitySn")).split("#");
						Speciality speciality;
						for(int s=0;s<specialitySn.length;s++){
							speciality=specialityService.getBySpecialitySn(specialitySn[s].trim());
							if(speciality!=null){
								standardIndex.getSpecialities().add(speciality);
							}else{
								erNum++;
								int j=i+3;
								error+="����"+erNum+"�����ݵ��벻�������ڵ�"+j+"��רҵ����쳣��";
							}
						}
					}
					if(array.getJSONObject(i).get("percentageScore")!="null"&&Float.parseFloat(array.getJSONObject(i).get("percentageScore").toString())!=0){
						float percentageScore=Float.parseFloat(array.getJSONObject(i).get("percentageScore").toString());
						standardIndex.setPercentageScore(percentageScore);
					}
						
					if(array.getJSONObject(i).get("zeroTimes")!="null"&&(Integer) array.getJSONObject(i).get("zeroTimes")!=0)
						standardIndex.setZeroTimes((int) array.getJSONObject(i).get("zeroTimes"));
					if(array.getJSONObject(i).get("parentindexSn")!="null"){
						if(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn"))!=null){
							standardIndex.setParent(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn").toString().trim()));
						}else{								
							erNum++;
							int j=i+3;
							error+="����"+erNum+"����"+j+"�����ݵ�����󣡸�����Ų����ڣ�";
							continue;
						}
					}					
					try{
						standardindexService.addStandardIndex(standardIndex);						
					}catch(ConstraintViolationException e){
						
						//ִ�и���
						String indexSn=(String) array.getJSONObject(i).get("indexSn");
						StandardIndex index=standardindexService.getByindexSn(indexSn.trim());
						if(index!=null){
							Standard standard2=standardService.getByStandardSn(standardSn);
							index.setStandard(standard2);
							index.setDeleted(false);
							if(array.getJSONObject(i).get("anDeduction")!="null" ){
								if((Float)array.getJSONObject(i).get("anDeduction")!=0f){
									index.setAnDeduction((Float) array.getJSONObject(i).get("anDeduction"));
								}else{
									index.setAnDeduction(null);
								}
							}else{
								index.setAnDeduction(null);
							}
								
							if(array.getJSONObject(i).get("indexName")!="null"){
								index.setIndexName((String) array.getJSONObject(i).get("indexName"));
							}else{
								index.setIndexName(null);
							}
								
							if(array.getJSONObject(i).get("isKeyIndex")!="null"){
								if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
									index.setIsKeyIndex(true);
								}else if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
									index.setIsKeyIndex(false);
								}else{
									index.setIsKeyIndex(null);
								}
							}else{
								index.setIsKeyIndex(null);
							}
							if(array.getJSONObject(i).get("jointIndexIdCode")!="null"){
								index.setJointIndexIdCode(array.getJSONObject(i).get("jointIndexIdCode").toString().trim());
							}else{
								index.setJointIndexIdCode(null);
							}
							index.getSpecialities().clear();
							//רҵ
							if(array.getJSONObject(i).get("specialitySn")!="null"&&array.getJSONObject(i).get("specialitySn").toString().trim().length()>0){
								String []specialitySn=((String) array.getJSONObject(i).get("specialitySn")).split("#");
								Speciality speciality;
								for(int s=0;s<specialitySn.length;s++){
									speciality=specialityService.getBySpecialitySn(specialitySn[s].trim());
									if(speciality!=null){
										index.getSpecialities().add(speciality);
									}else{
										erNum++;
										int j=i+3;
										error+="����"+erNum+"�����ݵ��벻�������ڵ�"+j+"��רҵ����쳣��"	;
									}
								}
							}

							if(array.getJSONObject(i).get("percentageScore")!="null"){
								if(Float.parseFloat(array.getJSONObject(i).get("percentageScore").toString())!=0){
									float percentageScore=Float.parseFloat(array.getJSONObject(i).get("percentageScore").toString());
									index.setPercentageScore(percentageScore);
								}else{
									index.setPercentageScore(null);
								}
							}else{
								index.setPercentageScore(null);
							}
							if(array.getJSONObject(i).get("zeroTimes")!="null"&&(Integer) array.getJSONObject(i).get("zeroTimes")!=0)
								index.setZeroTimes((int) array.getJSONObject(i).get("zeroTimes"));
							if(array.getJSONObject(i).get("parentindexSn")!="null"){
								if(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn"))!=null){
									index.setParent(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn").toString().trim()));
								}else{								
									erNum++;
									int j=i+3;
									error+="����"+erNum+"����"+j+"�����ݵ�����󣡸�����Ų����ڣ�";
									continue;
								}
							}else{
								index.setParent(null);
							}
							//ɾ��ԭ�з���
							for(StandardIndexAuditMethod standardIndexAuditMethod:index.getAuditMethods()){
								standardIndexAuditMethodService.delete(standardIndexAuditMethod.getId());
							}
							try{
								standardindexService.update(index);
								try{
									//��˷���
									if(array.getJSONObject(i).get("auditMethod")!="null" && array.getJSONObject(i).get("auditMethod").toString().trim().length()>0){
										String []auditMethod=((String) array.getJSONObject(i).get("auditMethod")).split("#");
										String auditMethodSn="";
										
										if(array.getJSONObject(i).get("isKeyIndex")!="null"){
											if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
												for(int x=0;x<auditMethod.length;x++){
													int y=x+1;
													if(auditMethod[x].split("[$]").length==3){
														StandardIndex index2=standardindexService.getByindexSn(auditMethod[x].split("[$]")[1].trim());
														if(index2!=null){
															auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
															StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
															try{
																standardIndexAuditMethod.setDeduction(Float.valueOf(auditMethod[x].split("[$]")[2].trim()));
															}catch(Exception e2){
																erNum++;
																int j=i+3;
																error+="����"+erNum+"����"+j+"����˷���������󣬹ؼ�ָ���ƶ����۷�����ʽ����";
																continue;
															}
															standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
															standardIndexAuditMethod.setIndexDeducted(index2);
															standardIndexAuditMethod.setAuditMethodContent(auditMethod[x].split("[$]")[0]);
															standardIndexAuditMethod.setStandardIndex(index);
															standardIndexAuditMethodService.save(standardIndexAuditMethod);
														}else{
															erNum++;
															int j=i+3;
															error+="����"+erNum+"����"+j+"����˷���������󣬹ؼ�ָ��ָ���۷�ָ�겻���ڣ�";
															continue;
														}
														
													}else{
														auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
														StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
														standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
														standardIndexAuditMethod.setAuditMethodContent(auditMethod[x]);
														standardIndexAuditMethod.setStandardIndex(index);
														standardIndexAuditMethodService.save(standardIndexAuditMethod);
													}										
												}
											}
										}else{
											for(int x=0;x<auditMethod.length;x++){
												int y=x+1;
												auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
												StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
												standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
												standardIndexAuditMethod.setAuditMethodContent(auditMethod[x]);
												standardIndexAuditMethod.setStandardIndex(index);
												standardIndexAuditMethodService.save(standardIndexAuditMethod);
											}
										}		
									}
								}catch(Exception e1){
									System.out.println(e1);
									erNum++;
									int j=i+3;
									error+="����"+erNum+"����"+j+"�����ݵ��벻��������˷������벻������";
									continue;
								}								
								continue;
							}catch(Exception e1){
								erNum++;
								int j=i+3;
								error+="����"+erNum+"����"+j+"�����ݵ������";
								continue;
							}
						}else{
							erNum++;
							int j=i+3;
							error+="����"+erNum+"����"+j+"�����ݵ������";
							continue;
						}
					}
					try{
						//��˷���
						if(array.getJSONObject(i).get("auditMethod")!="null" && array.getJSONObject(i).get("auditMethod").toString().trim().length()>0){
							String []auditMethod=((String) array.getJSONObject(i).get("auditMethod")).split("#");
							String auditMethodSn="";
							if(array.getJSONObject(i).get("isKeyIndex")!="null"){
								if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
									for(int x=0;x<auditMethod.length;x++){
										int y=x+1;
										if(auditMethod[x].split("[$]").length==3){
											StandardIndex index=standardindexService.getByindexSn(auditMethod[x].split("[$]")[1].trim());
											if(index!=null){
												auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
												StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
												try{
													standardIndexAuditMethod.setDeduction(Float.valueOf(auditMethod[x].split("[$]")[2].trim()));
												}catch(Exception e){
													erNum++;
													int j=i+3;
													error+="����"+erNum+"����"+j+"����˷���������󣬹ؼ�ָ���ƶ����۷�����ʽ����";
													continue;
												}
												standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
												standardIndexAuditMethod.setIndexDeducted(index);
												standardIndexAuditMethod.setAuditMethodContent(auditMethod[x].split("[$]")[0]);
												standardIndexAuditMethod.setStandardIndex(standardIndex);
												standardIndexAuditMethodService.save(standardIndexAuditMethod);
											}else{
												erNum++;
												int j=i+3;
												error+="����"+erNum+"����"+j+"����˷���������󣬹ؼ�ָ��ָ���۷�ָ�겻���ڣ�";
												continue;
											}
											
										}else{
											auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
											StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
											standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
											standardIndexAuditMethod.setAuditMethodContent(auditMethod[x]);
											standardIndexAuditMethod.setStandardIndex(standardIndex);
											standardIndexAuditMethodService.save(standardIndexAuditMethod);
										}										
									}
								}
							}else{
								for(int x=0;x<auditMethod.length;x++){
									int y=x+1;
									auditMethodSn=standardIndex.getStandard().getStandardSn()+"-"+standardIndex.getIndexSn()+"-"+y;
									StandardIndexAuditMethod standardIndexAuditMethod=new StandardIndexAuditMethod();
									standardIndexAuditMethod.setAuditMethodSn(auditMethodSn);
									standardIndexAuditMethod.setAuditMethodContent(auditMethod[x]);
									standardIndexAuditMethod.setStandardIndex(standardIndex);
									standardIndexAuditMethodService.save(standardIndexAuditMethod);
								}
							}
							
						}
					}catch(Exception e){
						System.out.println(e);
						erNum++;
						int j=i+3;
						error+="����"+erNum+"����"+j+"�����ݵ��벻��������˷������벻������";
						continue;
					}
					
				} catch(Exception e){
					erNum++;
					int j=i+3;
					error+="����"+erNum+"���ڵ�"+j+"�в�������쳣��";			
			}			
		}
			if(error==""||error==null){
				str = "{\"status\":\"ok\",\"message\":\"���ݵ���ɹ���\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+erNum+"}";
			}else{
				str = "{\"status\":\"nook\",\"message\":\"���ݵ���ʱ�����쳣��\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+erNum+"}";
			}
			out().print(str);
	        out().flush(); 
	        out().close();
		}
		session.put("progressValue",0);
		return "jsonList";
	}
	//���ָ�����ݵ���
	public String importData2() throws IOException{
		ImportUtils importtUtils=new ImportUtils();
		JSONArray array=importtUtils.upload(templateSn, fileModel,7);
		HashMap<String ,Integer> record=new HashMap<String ,Integer>();
		String error="";
		String str="";
		int erNum=0;
		if(array.getJSONObject(0).has("error1")){
			for(int i=1;i<=(int)array.getJSONObject(1).get("erNum");i++){
				 error+="����"+i+":"+array.getJSONObject(0).get("error"+i).toString()+"";
			}
			str = "{\"status\":\"nook\",\"message\":\"������֤ʧ�ܣ�\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+array.getJSONObject(1).getInt("erNum")+"}";
			out().print(str);
	        out().flush(); 
	        out().close();
		}else{
			float m2=array.length();
			for(int i=0;i<array.length();i++){
				float m1=i;
				session.put("progressValue",(int)(m1/m2*100));
				try{
					if(record.containsKey((String) array.getJSONObject(i).get("indexSn"))){
						erNum++;
						int j=i+3;
						error+="����"+erNum+"����"+j+"�����ݵ�����󣡱�����"+record.get((String) array.getJSONObject(i).get("indexSn"))+"�б���ظ���";
						continue;
					}else{
						record.put((String) array.getJSONObject(i).get("indexSn"), i+3);
					}
					StandardIndex standardIndex=new StandardIndex();
					standardIndex.setDeleted(false);
					standardIndex.setStandard(standardService.getByStandardSn(standardSn));					
					if(array.getJSONObject(i).get("isKeyIndex")!="null"){
						if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
							standardIndex.setIsKeyIndex(true);
						}else if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
							standardIndex.setIsKeyIndex(false);
						}
					}
					if(array.getJSONObject(i).get("auditKeyPoints")!="null")
						standardIndex.setAuditKeyPoints((String) array.getJSONObject(i).get("auditKeyPoints"));
					if(array.getJSONObject(i).get("indexName")!="null")
						standardIndex.setIndexName((String) array.getJSONObject(i).get("indexName"));
					if(array.getJSONObject(i).get("indexSn")!="null")
						standardIndex.setIndexSn(array.getJSONObject(i).get("indexSn").toString().trim());
					if(array.getJSONObject(i).get("integerScore")!="null"&& Double.parseDouble(array.getJSONObject(i).get("integerScore").toString())>0){
						int integerScore=(int)Double.parseDouble(array.getJSONObject(i).get("integerScore").toString());
						standardIndex.setIntegerScore(integerScore);
					}
					if(array.getJSONObject(i).get("pdca")!="null")
						standardIndex.setPdca(Pdca.valueOf((String) array.getJSONObject(i).get("pdca")));
					if(array.getJSONObject(i).get("parentindexSn")!="null"){
						if(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn"))!=null){
							standardIndex.setParent(standardindexService.getByindexSn(array.getJSONObject(i).get("parentindexSn").toString().trim()));
						}else{
								
							erNum++;
							int j=i+3;
							error+="����"+erNum+"����"+j+"�����ݵ�����󣡸�����Ų����ڣ�";
							continue;
						}
					}
					try{
						standardindexService.addStandardIndex(standardIndex);						
					}catch(ConstraintViolationException e){
						String indexSn=(String) array.getJSONObject(i).get("indexSn");
						StandardIndex index=standardindexService.getByindexSn(indexSn.trim());
						if(index!=null){
							//ִ�и���
							Standard standard2=standardService.getByStandardSn(standardSn);
							index.setStandard(standard2);
							index.setDeleted(false);
							if(array.getJSONObject(i).get("isKeyIndex")!="null"){
								if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
									index.setIsKeyIndex(true);
								}else if(array.getJSONObject(i).get("isKeyIndex").equals("��")){
									index.setIsKeyIndex(false);
								}
							}else{
								index.setIsKeyIndex(null);
							}
							if(array.getJSONObject(i).get("auditKeyPoints")!="null")
								index.setAuditKeyPoints((String) array.getJSONObject(i).get("auditKeyPoints"));
							if(array.getJSONObject(i).get("indexName")!="null"){
								index.setIndexName((String) array.getJSONObject(i).get("indexName"));
							}else{
								index.setIndexName(null);
							}
							if(array.getJSONObject(i).get("indexSn")!="null")
								index.setIndexSn((String) array.getJSONObject(i).get("indexSn"));
							if(array.getJSONObject(i).get("integerScore")!="null"){
								if(Double.parseDouble(array.getJSONObject(i).get("integerScore").toString())>0){
									int integerScore=(int)Double.parseDouble(array.getJSONObject(i).get("integerScore").toString());
									standardIndex.setIntegerScore(integerScore);
								}
							}else{
								standardIndex.setIntegerScore(null);
							}
							if(array.getJSONObject(i).get("pdca")!="null"){
								index.setPdca(Pdca.valueOf((String) array.getJSONObject(i).get("pdca")));
							}else{
								index.setPdca(null);
							}
								
							if(array.getJSONObject(i).get("parentindexSn")!="null"){
								if(standardindexService.getByindexSn((String)array.getJSONObject(i).get("parentindexSn"))!=null){
									index.setParent(standardindexService.getByindexSn(array.getJSONObject(i).get("parentindexSn").toString().trim()));
								}
							}else{
								index.setParent(null);
							}
							try{
								standardindexService.update(index);
							}catch(Exception e1){
								erNum++;
								int j=i+3;
								error+="����"+erNum+"���ڵ�"+j+"�в�������쳣��";
								continue;
							}
							
						}else{
							erNum++;
							int j=i+3;
							error+="����"+erNum+"���ڵ�"+j+"�в�������쳣��";
							continue;
						}
					}catch(Exception e){
						erNum++;
						int j=i+3;
						error+="����"+erNum+"���ڵ�"+j+"�в�������쳣��";
						continue;
					}					
						
				} catch(Exception e){
					erNum++;
					int j=i+3;
					error+="����"+erNum+"���ڵ�"+j+"�в�������쳣��"	;
					continue;
				}			
			}
			if(error==""||error==null){
				str = "{\"status\":\"ok\",\"message\":\"���ݵ���ɹ���\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+erNum+"}";
			}else{
				str = "{\"status\":\"nook\",\"message\":\"���ݵ���ʱ�����쳣��\"," + "\"error\":"+"\""+error+"\","+"\"erNum\":"+erNum+"}";
			}
			out().print(str);
	        out().flush(); 
	        out().close();
		}
		session.put("progressValue",0);
		return "jsonList";
	}
	//�����첽����
	@SuppressWarnings("unused")
	public String queryCheck() throws IOException{
        jsonList=null;
        String hql="";
        JSONArray tree=new JSONArray();
		if(indexSn!=null){
			hql="FROM StandardIndex s where indexSn LIKE '"+indexSn+"._' OR indexSn LIKE '"+indexSn+".__'";
			jsonList=standardindexService.getPart(hql);

		}else{
			hql="FROM StandardIndex where indexSn LIKE 'A._' OR indexSn LIKE 'A.__'";
			jsonList=standardindexService.getPart(hql);
		}
		Set<Speciality> list=null;
		for(StandardIndex standardIndex:jsonList){
			list= standardIndex.getSpecialities();
			boolean b=list.retainAll(specialityService.getByIds(ids));
			if(standardIndex.getStandard().getStandardSn().equals(standardSn)&&list.size()>0){				
			JSONObject jo=new JSONObject();
			jo.put("id", standardIndex.getId());
			jo.put("indexSn", standardIndex.getIndexSn());
			jo.put("indexName", standardIndex.getIndexName());
			jo.put("pdca", standardIndex.getPdca());
			jo.put("auditKeyPoints",standardIndex.getAuditKeyPoints());
			jo.put("percentageScore", standardIndex.getPercentageScore());
			jo.put("integerScore",standardIndex.getIntegerScore());
			jo.put("isKeyIndex", standardIndex.getIsKeyIndex());
			jo.put("anDeduction", standardIndex.getAnDeduction());
			jo.put("zeroTimes", standardIndex.getZeroTimes());
			jo.put("document", standardIndex.getDocumentTemplates().size());
			if(standardIndex.getAnDeduction()==null){
				jo.put("state","closed");
			}else{
				jo.put("state", "open");
			}
			tree.put(jo);
		}
		}
		out().print(tree.toString());
        out().flush(); 
        out().close();
		return "jsonList";
	}
	public String getExcelFileName() {
		return excelFileName;
	}
	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	public String getHazardSn() {
		return hazardSn;
	}
	public void setHazardSn(String hazardSn) {
		this.hazardSn = hazardSn;
	}
	private List<Hazard> jsonLoad;
	
	public List<Hazard> getJsonLoad() {
		return jsonLoad;
	}
	public void setJsonLoad(List<Hazard> jsonLoad) {
		this.jsonLoad = jsonLoad;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public String getSpecialitySn() {
		return specialitySn;
	}
	public void setSpecialitySn(String specialitySn) {
		this.specialitySn = specialitySn;
	}
	public String getGetTable() {
		return getTable;
	}
	public void setGetTable(String getTable) {
		this.getTable = getTable;
	}
	public String getParentindexSn() {
		return parentindexSn;
	}
	public void setParentindexSn(String parentindexSn) {
		this.parentindexSn = parentindexSn;
	}
	public FileModel getFileModel() {
		return fileModel;
	}
	public void setFileModel(FileModel fileModel) {
		this.fileModel = fileModel;
	}
	public String getTemplateSn() {
		return templateSn;
	}
	public void setTemplateSn(String templateSn) {
		this.templateSn = templateSn;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public Pdca getPdca() {
		return pdca;
	}
	public void setPdca(Pdca pdca) {
		this.pdca = pdca;
	}
	public String getAuditKeyPoints() {
		return auditKeyPoints;
	}
	public void setAuditKeyPoints(String auditKeyPoints) {
		this.auditKeyPoints = auditKeyPoints;
	}
	public Float getPercentageScore() {
		return percentageScore;
	}
	public void setPercentageScore(Float percentageScore) {
		this.percentageScore = percentageScore;
	}
	public Integer getIntegerScore() {
		return integerScore;
	}
	public void setIntegerScore(Integer integerScore) {
		this.integerScore = integerScore;
	}
	public Boolean getIsKeyIndex() {
		return isKeyIndex;
	}
	public void setIsKeyIndex(Boolean isKeyIndex) {
		this.isKeyIndex = isKeyIndex;
	}

	public Float getAnDeduction() {
		return anDeduction;
	}
	public void setAnDeduction(Float anDeduction) {
		this.anDeduction = anDeduction;
	}
	public int getZeroTimes() {
		return zeroTimes;
	}
	public void setZeroTimes(int zeroTimes) {
		this.zeroTimes = zeroTimes;
	}
	public String getDocumentTemplateSn() {
		return documentTemplateSn;
	}
	public void setDocumentTemplateSn(String documentTemplateSn) {
		this.documentTemplateSn = documentTemplateSn;
	}
	
}
