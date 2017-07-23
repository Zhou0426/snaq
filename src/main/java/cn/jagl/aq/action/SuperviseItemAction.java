package cn.jagl.aq.action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cn.jagl.aq.domain.Department;
import cn.jagl.aq.domain.DepartmentType;
import cn.jagl.aq.domain.SuperviseItem;
import cn.jagl.aq.domain.SuperviseDailyReport;
import cn.jagl.aq.domain.SuperviseDailyReportDetails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import software.amazon.ion.Timestamp;
public class SuperviseItemAction extends BaseAction<SuperviseItem> {
	/**
	 * @author Sakura
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String superviseContentName;
	private String parentId;
	private Set<SuperviseItem> child=new HashSet<SuperviseItem>(0);
	private String departmentSn;
	private String personId;
	private JSONArray jsonArray=new JSONArray();
	private JSONObject jsonObject=new JSONObject();
	
	public String show(){
        java.util.Date nDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(nDate);
        Date reportDate = Date.valueOf(sDate);

        Department implDepartment=departmentService.getUpNearestImplDepartment(personService.getByPersonId(personId).getDepartment().getDepartmentSn());
        departmentSn = implDepartment.getDepartmentSn();
        DepartmentType departmentType = departmentService.getUpNearestImplDepartment(departmentSn).getDepartmentType();        
        SuperviseDailyReport superviseDailyReport=superviseDailyReportService.getSuperviseDailyReportByDateandSn(reportDate, departmentType.getDepartmentTypeSn());
        List<SuperviseItem> superviseItems = superviseItemService.getSuperviseItemsByType(departmentType.getDepartmentTypeSn());
        if(superviseDailyReport!=null){
        	int order=0;
        	String parentSn="";
        	for(SuperviseItem superviseItem:superviseItems){
        		if(superviseItem.getParent()!=null){
        			SuperviseDailyReportDetails superviseDailyReportDetails=superviseDailyReportDetailsService.getByMany(superviseDailyReport.getId(),departmentSn, superviseItem.getSuperviseItemSn());
            		JSONObject jo=new JSONObject();
            		if(!parentSn.endsWith(superviseItem.getParent().getSuperviseItemSn())){
            			order++;
            		}
            		jo.put("order", order);
            		jo.put("parentName", superviseItem.getParent().getSuperviseContentName());
            		jo.put("itemName", superviseItem.getSuperviseContentName());
            		jo.put("parentSn", superviseItem.getParent().getSuperviseItemSn());
            		jo.put("itemSn", superviseItem.getSuperviseItemSn());
            		if(superviseDailyReportDetails!=null){
            			jo.put("reportSketch", superviseDailyReportDetails.getReportSketch());
            			jo.put("reportDetails", superviseDailyReportDetails.getReportDetails());
            		}
            		parentSn=superviseItem.getParent().getSuperviseItemSn();
            		jsonArray.add(jo);
            	}
        	} 
        }else{ 
        	int order=0;
        	String parentSn="";
        	for(SuperviseItem superviseItem:superviseItems){
        		if(superviseItem.getParent()!=null){
        			if(!parentSn.endsWith(superviseItem.getParent().getSuperviseItemSn())){
            			order++;
            		}
            		JSONObject jo=new JSONObject();
            		jo.put("order", order);
            		jo.put("parentName", superviseItem.getParent().getSuperviseContentName());
            		jo.put("itemName", superviseItem.getSuperviseContentName());
            		jo.put("parentSn", superviseItem.getParent().getSuperviseItemSn());
            		jo.put("itemSn", superviseItem.getSuperviseItemSn());
            		parentSn=superviseItem.getParent().getSuperviseItemSn();
            		jsonArray.add(jo);
            	}
        	}        	
        }
		return "jsonArray";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSuperviseContentName() {
		return superviseContentName;
	}
	public void setSuperviseContentName(String superviseContentName) {
		this.superviseContentName = superviseContentName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Set<SuperviseItem> getChild() {
		return child;
	}
	public void setChild(Set<SuperviseItem> child) {
		this.child = child;
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
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
}
