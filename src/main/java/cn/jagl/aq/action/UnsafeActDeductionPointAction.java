package cn.jagl.aq.action;

import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.UnsafeActDeductionPoint;
import cn.jagl.aq.domain.UnsafeActLevel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author ���
 * @since JDK1.8
 * @history 2017��5��5������9:39:04 ��� �½�
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
			jsonObject.put("message", "������Ѿ���ӣ������ٴ���ӣ�");
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
					u.setUnsafeActLevel(UnsafeActLevel.A�಻��ȫ��Ϊ);				
				}else if(level==1){
					u.setUnsafeActLevel(UnsafeActLevel.B�಻��ȫ��Ϊ);				
				}else{
					u.setUnsafeActLevel(UnsafeActLevel.C�಻��ȫ��Ϊ);				
				}
				unsafeActDeductionPointService.save(u);
				
				jsonObject.put("status", "ok");
				jsonObject.put("message", "��ӳɹ���");
			}catch(Exception e){
				jsonObject.put("status", "nook");
				jsonObject.put("message", "���ʧ�ܣ�");
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
				jsonObject.put("message", "������Ѿ���Ӵ��಻��ȫ��Ϊ���޷��޸ģ�");
				return "jsonObject";
			}else{
				try{
					u.setDeductionPoints(deductionPoints);
					u.setYear(year);
					if(type==0){
						u.setDepartmentType(departmentTypeService.getByDepartmentTypeSn(departmentTypeSn));
					}
					
					if(level==2){
						u.setUnsafeActLevel(UnsafeActLevel.A�಻��ȫ��Ϊ);				
					}else if(level==1){
						u.setUnsafeActLevel(UnsafeActLevel.B�಻��ȫ��Ϊ);				
					}else{
						u.setUnsafeActLevel(UnsafeActLevel.C�಻��ȫ��Ϊ);				
					}
					unsafeActDeductionPointService.update(u);
					
					jsonObject.put("status", "ok");
					jsonObject.put("message", "�޸ĳɹ���");
				}catch(Exception e){
					jsonObject.put("status", "nook");
					jsonObject.put("message", "�޸�ʧ�ܣ�");
				}
			}
		}else{
			jsonObject.put("status", "nook");		
			jsonObject.put("message", "�޸�ʧ�ܣ�");
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
