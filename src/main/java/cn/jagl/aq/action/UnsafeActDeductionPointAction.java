package cn.jagl.aq.action;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import cn.jagl.aq.domain.UnsafeActLevel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author 马辉
 * @since JDK1.8
 * @history 2017年5月5日下午9:39:04 马辉 新建
 */
public class UnsafeActDeductionPointAction extends BaseAction<UnsafeActDeductionPoint> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2467000614209039473L;
	
	private JSONObject jsonObject=new JSONObject();
	private JSONArray jsonArray=new  JSONArray();
	
	private int id;
	
	private int year;
	
	private String departmentTypeSn;
	
	private Byte type;
	
	private int level;

	private Double deductionPoints;
	
	private String departmentSn;


	public String query(){
		if(type==0){
			jsonObject=unsafeActDeductionPointService.query(type, departmentTypeSn, null, page, rows);
		}else{
			Department department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
			if(department!=null){
				jsonObject=unsafeActDeductionPointService.query(type, null, department.getDepartmentSn(), page, rows);
			}
		}
		return "jsonObject";
	}
	
	public String save(){
		String dSn="";
		Department department=null;
		if(type!=0){
			department=departmentService.getUpNearestImplDepartment((String) session.get("departmentSn"));
			if(department!=null){
				dSn=department.getDepartmentSn();
			}
		}
		UnsafeActDeductionPoint u=unsafeActDeductionPointService.getByMany(type, departmentTypeSn, dSn, year, level);
		if(u!=null){
			jsonObject.put("status", "nook");
			jsonObject.put("message", "该年度已经添加，无需再次添加！");
			return "jsonObject";
		}else{
			try{
				u=new UnsafeActDeductionPoint();
				u.setDeductionPoints(deductionPoints);
				u.setYear(year);
				if(type==0){
					u.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
				}else{
					u.setDepartment(department);
				}
				
				if(level==2){
					u.setUnsafeActLevel(UnsafeActLevel.A类不安全行为);				
				}else if(level==1){
					u.setUnsafeActLevel(UnsafeActLevel.B类不安全行为);				
				}else{
					u.setUnsafeActLevel(UnsafeActLevel.C类不安全行为);				
				}
				unsafeActDeductionPointService.save(u);
				
				jsonObject.put("status", "ok");
				jsonObject.put("message", "添加成功！");
			}catch(Exception e){
				jsonObject.put("status", "nook");
				jsonObject.put("message", "添加失败！");
			}
			
			
		}
		return "jsonObject";
	}
	
	public String delete(){
		try{
			unsafeActDeductionPointService.delete(id);
			jsonObject.put("status", "ok");
		}catch(Exception e){
			jsonObject.put("status", "nook");
		}
		return "jsonObject";
	}
	
	public String update(){
		UnsafeActDeductionPoint u=unsafeActDeductionPointService.getById(id);
		if(u!=null){
			UnsafeActDeductionPoint u2=unsafeActDeductionPointService.getByMany(type, departmentTypeSn, departmentSn, year, level);
			if(u2!=null&&u2.getId()!=u.getId()){
				jsonObject.put("status", "nook");		
				jsonObject.put("message", "该年度已经添加此类不安全行为，无法修改！");
				return "jsonObject";
			}else{
				try{
					u.setDeductionPoints(deductionPoints);
					u.setYear(year);
					if(type==0){
						u.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
					}
					
					if(level==2){
						u.setUnsafeActLevel(UnsafeActLevel.A类不安全行为);				
					}else if(level==1){
						u.setUnsafeActLevel(UnsafeActLevel.B类不安全行为);				
					}else{
						u.setUnsafeActLevel(UnsafeActLevel.C类不安全行为);				
					}
					unsafeActDeductionPointService.update(u);
					
					jsonObject.put("status", "ok");
					jsonObject.put("message", "修改成功！");
				}catch(Exception e){
					jsonObject.put("status", "nook");
					jsonObject.put("message", "修改失败！");
				}
			}
		}else{
			jsonObject.put("status", "nook");		
			jsonObject.put("message", "修改失败！");
			return "jsonObject";
		}
		return "jsonObject";
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}

	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public Double getDeductionPoints() {
		return deductionPoints;
	}

	public void setDeductionPoints(Double deductionPoints) {
		this.deductionPoints = deductionPoints;
	}

	public String getDepartmentSn() {
		return departmentSn;
	}

	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
}
