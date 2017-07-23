package cn.jagl.aq.action;

import cn.jagl.aq.domain.ResourceUseStat;
import net.sf.json.JSONArray;

public class ModuleStatisticsAction extends BaseAction<ResourceUseStat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int yearTime;
	private int monthTime;
	private String id;//Ä£¿é±àºÅ
	private JSONArray jsonarray;
	
	public String query(){
		jsonarray = moduleStatisticsService.findAll(yearTime, monthTime, id);
		return SUCCESS;
	}
	
	
	

	public JSONArray getJsonarray() {
		return jsonarray;
	}
	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}	
	public int getYearTime() {
		return yearTime;
	}
	public void setYearTime(int yearTime) {
		this.yearTime = yearTime;
	}
	public int getMonthTime() {
		return monthTime;
	}
	public void setMonthTime(int monthTime) {
		this.monthTime = monthTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
}
