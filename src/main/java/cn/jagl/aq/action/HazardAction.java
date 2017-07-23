package cn.jagl.aq.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.jagl.aq.domain.AccidentType;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.Hazard;
import cn.jagl.aq.domain.ManageObject;
import cn.jagl.aq.domain.RiskLevel;
import cn.jagl.aq.domain.StandardIndex;

@SuppressWarnings("serial")
public class HazardAction extends BaseAction<Hazard> {
	
	private int id;//主键，自动增长
	private String departmentTypeSn;//所属部门类型
	private String hazardSn;//危险源（危害）编号
	private String hazardDescription;//危险源（危害）名称
	private String resultDescription;//危险源（危害）后果描述
	private String accidentTypeSn;//可能造成的事故类型
	private RiskLevel riskLevel;//风险等级
	private long total;//总记录数
	private String manageObjectSn;//管理对象编号
	private String pag;//输出字符
	private String q;//危险源列表输入字符查询参数
	private File uploadExcel;//文件名
	
	
	//存取数据的对象
	private Class<Hazard> hazard;
	
	public File getUploadExcel() {
		return uploadExcel;
	}

	public void setUploadExcel(File uploadExcel) {
		this.uploadExcel = uploadExcel;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	public String getPag() {
		return pag;
	}

	public void setPag(String pag) {
		this.pag = pag;
	}
	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public Class<Hazard> getHazard() {
		return hazard;
	}

	public void setHazard(Class<Hazard> hazard) {
		this.hazard = hazard;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getHazardDescription() {
		return hazardDescription;
	}

	public void setHazardDescription(String hazardDescription) {
		this.hazardDescription = hazardDescription;
	}

	public String getAccidentTypeSn() {
		return accidentTypeSn;
	}

	public void setAccidentTypeSn(String accidentTypeSn) {
		this.accidentTypeSn = accidentTypeSn;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	public String getManageObjectSn() {
		return manageObjectSn;
	}

	public void setManageObjectSn(String manageObjectSn) {
		this.manageObjectSn = manageObjectSn;
	}
	//危险源页面datagrid获取所有危险源
	public String showHazard() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out;  
        out = response.getWriter();
		Long a=0l;
		int b=0;
		String showList=null;
		String Str="";
		List<Hazard> lists=new ArrayList<Hazard>();
		JSONArray jsonArray=new JSONArray();
		if(manageObjectSn!=null&&manageObjectSn.trim().length()>0){
			List<ManageObject> list=manageObjectService.getByMoHuFind(manageObjectSn);
			for(ManageObject manageObject:list){
				Set<Hazard> set=manageObject.getHazards();
				if(set.size()>0){
					for(Iterator<Hazard> iter=set.iterator();iter.hasNext();){
						lists.add(iter.next());
						a++;
					}
				}
			}
				for(Hazard hazard:lists){
					b++;
					JSONObject jo=new JSONObject();
					jo.put("id", hazard.getId());
					jo.put("departmentTypeSn", hazard.getDepartmentType().getDepartmentTypeSn());
					jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
					jo.put("hazardSn", hazard.getHazardSn());
					jo.put("hazardDescription", hazard.getHazardDescription());
					jo.put("resultDescription", hazard.getResultDescription());
					jo.put("accidentTypeName", hazard.getAccidentType().getAccidentTypeName());
					jo.put("accidentTypeSn", hazard.getAccidentType().getAccidentTypeSn());
					jo.put("riskLevel", hazard.getRiskLevel());
					Set<ManageObject> Man=hazard.getManageObjects();
					if(Man.size()>0){
						Str="";
						for(Iterator<ManageObject> iter=Man.iterator();iter.hasNext();){
							Str=Str+iter.next().getManageObjectName()+",";
						}
						Str=Str.substring(0, Str.lastIndexOf(","));
						jo.put("manageObjectName",Str);
					}
					//Set<StandardIndex> standardIndexes=hazard.getStandardIndexes();
					jo.put("standardIndexNum",hazard.getStandardIndexes().size());
					if(b>(page-1)*rows&&b<=rows*page){
						jsonArray.put(jo);
					}
					if(b>page*rows){
						break;
					}
				}
		}else{
			List<Hazard> jsonLoad=null;
			String hql="select h from Hazard h where h.departmentType.departmentTypeSn='"+departmentTypeSn+"'";
			jsonLoad=hazardService.getByPage(hql, page, rows);
			a=hazardService.getCountByHazardSn(departmentTypeSn, manageObjectSn);
			for(Hazard hazard:jsonLoad){
				JSONObject jo=new JSONObject();
				jo.put("id", hazard.getId());
				jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
				jo.put("departmentTypeSn", hazard.getDepartmentType().getDepartmentTypeSn());
				jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
				jo.put("hazardSn", hazard.getHazardSn());
				jo.put("hazardDescription", hazard.getHazardDescription());
				jo.put("resultDescription", hazard.getResultDescription());
				jo.put("accidentTypeName", hazard.getAccidentType().getAccidentTypeName());
				jo.put("accidentTypeSn", hazard.getAccidentType().getAccidentTypeSn());
				jo.put("riskLevel", hazard.getRiskLevel());
				Set<ManageObject> Man=hazard.getManageObjects();
				if(Man.size()>0){
					Str="";
					for(Iterator<ManageObject> iter=Man.iterator();iter.hasNext();){
						Str=Str+iter.next().getManageObjectName()+",";
					}
					Str=Str.substring(0, Str.lastIndexOf(","));
					jo.put("manageObjectName",Str);
				}
				jo.put("standardIndexNum",hazard.getStandardIndexes().size());
				jsonArray.put(jo);
			}
		}
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("total", a);// total键 存放总记录数，必须的
        json.put("rows", jsonArray);// rows键 存放每页记录 list
        showList= JSONObject.valueToString(json);
		out.print(showList);
		out.flush();
		out.close();
		return "showList";
	}
	//危险源界面查找危险源
	public String showByHazardSn() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        JSONArray jsonArray=new JSONArray();
        List<Hazard> jsonLoad=hazardService.getByHazard(hazardSn, departmentTypeSn, page, rows);
        //写一个方法查询总数，上个方法也是
		Long total=hazardService.getCountByHazard(departmentTypeSn, hazardSn);
		for(Hazard hazard:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id", hazard.getId());
			jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
			jo.put("departmentTypeSn", hazard.getDepartmentType().getDepartmentTypeSn());
			jo.put("hazardSn", hazard.getHazardSn());
			jo.put("hazardDescription", hazard.getHazardDescription());
			jo.put("resultDescription", hazard.getResultDescription());
			jo.put("accidentTypeName", hazard.getAccidentType().getAccidentTypeName());
			jo.put("accidentTypeSn", hazard.getAccidentType().getAccidentTypeSn());
			jo.put("riskLevel", hazard.getRiskLevel());
			Set<ManageObject> Man=hazard.getManageObjects();
			if(Man.size()>0){
				String Str="";
				for(Iterator<ManageObject> iter=Man.iterator();iter.hasNext();){
					Str=Str+iter.next().getManageObjectName()+",";
				}
				Str=Str.substring(0, Str.lastIndexOf(","));
				jo.put("manageObjectName",Str);
			}
			jo.put("standardIndexNum",hazard.getStandardIndexes().size());
			jsonArray.put(jo);
		}
		Map<String, Object> json = new HashMap<String, Object>();
	    json.put("total", total);// total键 存放总记录数，必须的
	    json.put("rows", jsonArray);// rows键 存放每页记录 list
	    String showList= JSONObject.valueToString(json);
		out.print(showList);
		out.flush();
		out.close();
		return "showList";
	}
	
		//映射
		//根据管理对象编号获取相关危险源数据
		public String show() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out;  
	        out = response.getWriter();
			Long a=0l;
			int b=0;
			String Str=null;
			List<Hazard> lists=new ArrayList<Hazard>();
			JSONArray jsonArray=new JSONArray();
			if(manageObjectSn!=null){
				ManageObject manageObject=manageObjectService.getByManageObjectSn(manageObjectSn);
				Set<Hazard> set=manageObject.getHazards();
				if(set.size()>0){
					for(Iterator<Hazard> iter=set.iterator();iter.hasNext();){
						lists.add(iter.next());
						a++;
					}
					for(Hazard hazard:lists){
						b++;
						JSONObject jo=new JSONObject();
						jo.put("id", hazard.getId());
						//jo.put("departmentTypeName", hazard.getDepartmentType().getDepartmentTypeName());
						jo.put("departmentTypeSn", hazard.getDepartmentType().getDepartmentTypeSn());
						jo.put("hazardSn", hazard.getHazardSn());
						jo.put("hazardDescription", hazard.getHazardDescription());
						jo.put("resultDescription", hazard.getResultDescription());
						jo.put("accidentTypeName", hazard.getAccidentType().getAccidentTypeName());
						jo.put("accidentTypeSn", hazard.getAccidentType().getAccidentTypeSn());
						jo.put("riskLevel", hazard.getRiskLevel());
						if(b>(page-1)*rows&&b<=rows*page){
							jsonArray.put(jo);
						}
						if(b>page*rows){
							break;
						}
					}
				}
			}
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", a);// total键 存放总记录数，必须的
	        json.put("rows", jsonArray);// rows键 存放每页记录 list
	        Str= JSONObject.valueToString(json);
			out.print(Str);
			out.flush();
			out.close();
			return "showList";
		}
		//根据危险源获取指标
		public String showStandardIndex() throws IOException{
			HttpServletResponse response=ServletActionContext.getResponse();  
	        response.setContentType("text/html");  
	        response.setContentType("text/plain; charset=utf-8");
	        PrintWriter out = response.getWriter();
			String hql="select i from StandardIndex i LEFT JOIN i.hazards h where i.deleted=false and h.hazardSn='"+hazardSn+"'";
			List<StandardIndex> standardIndexes=standardindexService.getPart(hql);
			hql=hql.replaceFirst("i", "count(*)");
			long total=standardindexService.count(hql);
			JSONArray jsonArray=new JSONArray();
			for(StandardIndex standardIndex:standardIndexes){
				JSONObject jo=new JSONObject();
				jo.put("id", standardIndex.getId());
				jo.put("indexSn", standardIndex.getIndexSn());
				jo.put("indexName", standardIndex.getIndexName());
				if(standardIndex!=null){
					jo.put("standardSn", standardIndex.getStandard().getStandardSn());
					jo.put("standardName", standardIndex.getStandard().getStandardName());
				}
				jsonArray.put(jo);
			}
			Map<String, Object> json = new HashMap<String, Object>();
	        json.put("total", total);// total键 存放总记录数，必须的
	        json.put("rows", jsonArray);// rows键 存放每页记录 list
			out.print(JSONObject.valueToString(json));
			out.flush();
			out.close();
			return SUCCESS;
		}
	//获取所有危险源,形成下拉列表
	public String showList() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out=response.getWriter();
		List<Hazard> jsonLoad=null;
		jsonLoad=hazardService.getByHazard(q,departmentTypeSn,1,20);
		JSONArray jsonArray=new JSONArray();
		for(Hazard hazard:jsonLoad){
			JSONObject jo=new JSONObject();
			jo.put("id",hazard.getHazardSn());
			jo.put("text",hazard.getHazardDescription());
			jo.put("value",hazard.getHazardSn());
			jo.put("state", "closed");
			jsonArray.put(jo);
		}
		out.print(jsonArray.toString());
		out.flush();
		out.close();
		return "showList";
	}
	//获取事故类型列表
	public String accidentType() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
		List<AccidentType> list=accidentTypeService.getAllAccidentType();
		JSONArray jsonArray=new JSONArray();
		for(AccidentType acci:list){
			JSONObject jo=new JSONObject();
			jo.put("id",acci.getAccidentTypeSn());
			jo.put("text",acci.getAccidentTypeName());
			jo.put("value",acci.getAccidentTypeSn());
			jo.put("state", "closed");
			jsonArray.put(jo);
		}
		out.print(jsonArray.toString());
        out.flush(); 
        out.close(); 
		return "showList";
	}
	//增加数据
	public String add(){
		Hazard haz=hazardService.getByHazardSn(hazardSn);
		if(haz!=null){
			pag=LOGIN;
			return SUCCESS;
		}else{
			if(hazardSn==null && hazardSn.trim().length()==0){
				pag=ERROR;
				return SUCCESS;
			}
			Hazard hazard=new Hazard();
			DepartmentType departmentType=departmentTypeService.getByDepartmentTypeSn(departmentTypeSn);
			hazard.setDepartmentType(departmentType);
			AccidentType accidentType=accidentTypeService.getByAccidentTypeSn(accidentTypeSn);
			hazard.setAccidentType(accidentType);
			hazard.setHazardSn(hazardSn);
			hazard.setHazardDescription(hazardDescription);
			hazard.setResultDescription(resultDescription);
			hazard.setRiskLevel(riskLevel);
			try{
				hazardService.addHazard(hazard);
				pag=SUCCESS;
			}catch(Exception e){
				pag=ERROR;
			}
			return SUCCESS;
		}
	}
	//更新数据
	public String update(){
		Hazard hazard=hazardService.getById(Hazard.class,id);
		if(!hazardSn.equals(hazard.getHazardSn())){
			Hazard haz=hazardService.getByHazardSn(hazardSn);
			if(haz!=null){
				pag=LOGIN;
				return SUCCESS;
			}
		}
		hazard.setHazardSn(hazardSn);
		hazard.setHazardDescription(hazardDescription);
		hazard.setResultDescription(resultDescription);
		hazard.setRiskLevel(riskLevel);
		try{
			AccidentType accidentType=accidentTypeService.getByAccidentTypeSn(accidentTypeSn);
			hazard.setAccidentType(accidentType);
			hazardService.updateHazard(hazard);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//删除数据
	public String delete(){
		try{
			hazardService.deleteHazard(id);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		return SUCCESS;
	}
	//增加危险源关联的管理对象
	public String addManageObject() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        ManageObject manageObject=manageObjectService.getByManageObjectSn(manageObjectSn);
		Hazard hazard=hazardService.getByHazardSn(hazardSn);
		boolean a=hazard.getManageObjects().add(manageObject);
		try{
			hazardService.updateHazard(hazard);
		}catch(Exception e){
			pag="ERROR";
			out.print(pag);
			out.flush();
			out.close();
			return "showList";
		}
		if(a==true){
			pag="SUCCESS";
		}else{
			pag="ERROR";
		}
		out.print(pag);
		out.flush();
		out.close();
		return "showList";
	}
	//增加危险源关联的指标
	public String addStandardIndex() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out= response.getWriter();
        try{
	        StandardIndex standardIndex=standardindexService.getById(id);
			Hazard hazard=hazardService.getByHazardSn(hazardSn);
			if(hazard.getStandardIndexes().contains(standardIndex)){
				pag=LOGIN;
				out.print(pag);
				out.flush();
				out.close();
				return "showList";
			}
			hazard.getStandardIndexes().add(standardIndex);
			hazardService.updateHazard(hazard);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
        out.print(pag);
		out.flush();
		out.close();
		return "showList";
	}
	//删除危险源对应的管理对象
	public String deleteManageObject() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out=response.getWriter();
        ManageObject manageObject=manageObjectService.getByManageObjectSn(manageObjectSn);
		Hazard hazard=hazardService.getByHazardSn(hazardSn);
		boolean a=hazard.getManageObjects().remove(manageObject);
		try{
			hazardService.updateHazard(hazard);
		}catch(Exception e){
			pag="ERROR";
			out.print(pag);
			out.flush();
			out.close();
			return "showList";
		}
		
		if(a==true){
			pag="SUCCESS";
		}else{
			pag="ERROR";
		}
		out.print(pag);
		out.flush();
		out.close();
		return "showList";
	}
	//删除危险源对应的指标
	public String deleteStandardIndex() throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();  
        response.setContentType("text/html");  
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter out=response.getWriter();
        try{
        	StandardIndex standardIndex=standardindexService.getById(id);
			Hazard hazard=hazardService.getByHazardSn(hazardSn);
			hazard.getStandardIndexes().remove(standardIndex);
			hazardService.updateHazard(hazard);
			pag=SUCCESS;
		}catch(Exception e){
			pag=ERROR;
		}
		out.print(pag);
		out.flush();
		out.close();
		return "showList";
	}
	//导入标准危险源之间的关联
	@SuppressWarnings("resource")
	public String standardHazard() throws IOException{
		InputStream is= new FileInputStream(uploadExcel);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		StandardIndex standardIndex=null;
		Hazard hazard=null;
		String standardString="";
		String hazardString="";
        float num=0f;
        String notStandardIndex="第";
        String notHazard="第";
        String isError="第";
        String nullData="第";
		// 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            num=hssfSheet.getLastRowNum();
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                session.put("progressValue", (int)((float)rowNum*100/num));
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                // 循环列Cell
                // 0管理对象编号  1管理对象名称   2危险源编号 3危险源名称 
                try{
	                //0管理对象编号  
	                HSSFCell indexSn = hssfRow.getCell(0);
	                //判断是否为空
	                if (indexSn == null || indexSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                standardString=indexSn.getStringCellValue();
	                standardIndex=standardindexService.getByindexSn(standardString);
	                //指标为空，判断是否有回车或空格
	                if (standardIndex == null) {
	                	standardString=standardString.replace("\\n", "").trim();
	                	standardIndex=standardindexService.getByindexSn(standardString);
	                	if(standardIndex == null){
	                		notStandardIndex+=(rowNum+1)+",";
	                		continue;
	                	}
	                }
	                //2危险源编号
	                HSSFCell hazardSn = hssfRow.getCell(2);
	                if (hazardSn == null || hazardSn.toString().trim().length() == 0) {
	                	nullData+=(rowNum+1)+",";
	                    continue;
	                }
	                hazardString=hazardSn.getStringCellValue();
	                hazard=hazardService.getByHazardSn(hazardString);
	                if (hazard == null) {
	                	hazardString=hazardString.replace("\\n", "").trim();
	                	hazard=hazardService.getByHazardSn(hazardString);
	                	if(hazard==null){
	                		notHazard+=(rowNum+1)+",";
	                		continue;
	                	}
	                }
	                hazard.getStandardIndexes().add(standardIndex);
	                hazardService.updateHazard(hazard);
	            }catch(Exception e){
                	isError+=(rowNum+1)+",";
                	continue;
                }
        }
    }	  
		pag="";
        String[] number;
        if(!"第".equals(nullData)){
        	nullData=nullData.substring(0, nullData.length()-1) + "行有空数据！" + "<br />";
        	pag+=nullData;
        	number=nullData.split(",");
        	pag=pag+"共"+number.length+"行"+ "<br /><br />";
        }
        if(!"第".equals(notStandardIndex)){
        	notStandardIndex=notStandardIndex.substring(0, notStandardIndex.length()-1) + "行指标编号不存在！" + "<br />";
        	pag+=notStandardIndex;
        	number=notStandardIndex.split(",");
        	pag=pag+"共"+number.length+"行"+ "<br /><br />";
        }
        if(!"第".equals(notHazard)){
        	notHazard=notHazard.substring(0, notHazard.length()-1) + "行危险源编号不存在！" + "<br />";
        	pag+=notHazard;
        	number=notHazard.split(",");
        	pag=pag+"共"+number.length+"行"+ "<br /><br />";
        }
        if(!"第".equals(isError)){
        	isError=isError.substring(0, isError.length()-1) + "行导入异常，请检查数据！" + "<br />";
        	pag+=isError;
        	number=isError.split(",");
        	pag=pag+"共"+number.length+"行"+ "<br /><br />";
        }
        if(pag.equals("")){
        	pag="导入数据成功！";
        }else{
        	pag+="其他数据导入成功！";
        }
        session.put("progressValue",0);
    	return SUCCESS;
	}
	
}
