package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="near_miss_audit")
public class NearMissAudit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private NearMiss nearMiss;//未遂事件
	private NearMissAuditType nearMissAuditType;//审核类别
	private String auditInfo;//审核信息
	private NearMissState nearMissState;//审核结果
	private Person auditor;//审核人	
	private Timestamp auditTime;//审核时间
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=NearMiss.class,cascade=CascadeType.ALL)
	@JoinColumn(name="near_miss_sn",referencedColumnName="near_miss_sn",nullable=false)
	public NearMiss getNearMiss() {
		return nearMiss;
	}
	public void setNearMiss(NearMiss nearMiss) {
		this.nearMiss = nearMiss;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="near_miss_audit_type",nullable=false)
	public NearMissAuditType getNearMissAuditType() {
		return nearMissAuditType;
	}
	public void setNearMissAuditType(NearMissAuditType nearMissAuditType) {
		this.nearMissAuditType = nearMissAuditType;
	}
	@Column(name="audit_info",length=500)
	public String getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="near_miss_state",nullable=false)
	public NearMissState getNearMissState() {
		return nearMissState;
	}
	public void setNearMissState(NearMissState nearMissState) {
		this.nearMissState = nearMissState;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="auditor_id",referencedColumnName="person_id",nullable=false)
	public Person getAuditor() {
		return auditor;
	}
	public void setAuditor(Person auditor) {
		this.auditor = auditor;
	}
	@Column(name="audit_time")
	public Timestamp getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}	
}
