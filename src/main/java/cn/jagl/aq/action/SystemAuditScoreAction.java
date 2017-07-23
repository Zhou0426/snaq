package cn.jagl.aq.action;

import java.util.HashMap;
import java.util.Map;

import cn.jagl.aq.domain.SystemAuditScore;

public class SystemAuditScoreAction extends BaseAction<SystemAuditScore>{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String indexSn;
	private Integer conformDegree;
	private String auditSn;
	private Map<String,Object> map;
	//添加或更新
	public String saveorupdate(){
		map=new HashMap<String,Object>();
		map.put("status", "ok");
		try{
			SystemAuditScore systemAuditScore;
			systemAuditScore=systemAuditScoreService.getByMany(auditSn, indexSn);
			if(systemAuditScore!=null){
				systemAuditScore.setConformDegree(conformDegree);
				systemAuditScoreService.update(systemAuditScore);
			}else{
				systemAuditScore=new SystemAuditScore();
				systemAuditScore.setConformDegree(conformDegree);
				systemAuditScore.setStandardIndex(standardindexService.getByindexSn(indexSn));
				systemAuditScore.setSystemAudit(systemAuditService.getById(id));
				systemAuditScoreService.save(systemAuditScore);
			}
		}catch(Exception e){
			map.put("status", "nook");
		}		
		return SUCCESS;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
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
	public Integer getConformDegree() {
		return conformDegree;
	}
	public void setConformDegree(Integer conformDegree) {
		this.conformDegree = conformDegree;
	}
}
