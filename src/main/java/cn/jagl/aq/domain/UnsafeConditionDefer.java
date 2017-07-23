package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="unsafe_condition_defer")
public class UnsafeConditionDefer implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//
	private String applicationSn;//����������
	private UnsafeCondition unsafecondition;//��Ӧ������
	private String reason;//��������
	private Timestamp applyDateTime;//����ʱ��
	private Person applicant;//������
	private Timestamp applyDeferTo;//�������ڵ�
	private Person auditor ;//�����
	private Boolean passed;//��˽��
	private String auditRemark;//���˵��
	private Timestamp auditDatetime;//���ʱ��
	
	@Column(name="application_sn",length=40,unique=true,nullable=false)
	public String getApplicationSn() {
		return applicationSn;
	}
	public void setApplicationSn(String applicationSn) {
		this.applicationSn = applicationSn;
	}
	@ManyToOne(targetEntity=InconformityItem.class)
	@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn",nullable=false)
	public UnsafeCondition getUnsafecondition() {
		return unsafecondition;
	}
	public void setUnsafecondition(UnsafeCondition unsafecondition) {
		this.unsafecondition = unsafecondition;
	}	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	
	@Column(name="reason")
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Column(name="apply_datetime",nullable=false)
	public Timestamp getApplyDateTime() {
		return applyDateTime;
	}
	public void setApplyDateTime(Timestamp applyDateTime) {
		this.applyDateTime = applyDateTime;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="applicant_id",referencedColumnName="person_id",nullable=false)
	public Person getApplicant() {
		return applicant;
	}
	public void setApplicant(Person applicant) {
		this.applicant = applicant;
	}
	@Column(name="apply_defer_to",nullable=false)
	public Timestamp getApplyDeferTo() {
		return applyDeferTo;
	}
	public void setApplyDeferTo(Timestamp applyDeferTo) {
		this.applyDeferTo = applyDeferTo;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="auditor_id",referencedColumnName="person_id")
	public Person getAuditor() {
		return auditor;
	}
	public void setAuditor(Person auditor) {
		this.auditor = auditor;
	}
	@Column(name="passed")
	public Boolean getPassed() {
		return passed;
	}
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}
	@Column(name="audit_remark",length=300)
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	@Column(name="audit_datetime")
	public Timestamp getAuditDatetime() {
		return auditDatetime;
	}
	public void setAuditDatetime(Timestamp auditDatetime) {
		this.auditDatetime = auditDatetime;
	}
	
}
