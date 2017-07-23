package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="hazard_reported")
public class HazardReported implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	private String reportSn;//上报编号，根据时间自动生成
	private DepartmentType departmentType;//所属部门类型
	private String hazardDescription;//危险源描述
	private String resultDescription;//后果描述	
	private AccidentType accidentType;//可能造成的事故类型
	private RiskLevel riskLevel;//风险等级
	private Person reportPerson;//上报人
	private java.time.LocalDateTime reportDateTime;//上报时间
	private Person auditor;//审核人
	private java.time.LocalDateTime auditedDateTime;//审批时间
	private String auditSuggestion;//审核意见
	private Boolean auditedStatus;//审核结果
	private Boolean deleted;//是否删除
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>(0);//关联的考核评分指标
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="report_sn",unique=true,nullable=false)
	public String getReportSn() {
		return reportSn;
	}
	public void setReportSn(String reportSn) {
		this.reportSn = reportSn;
	}
	@Column(name="hazard_description",length=500,nullable=false)
	public String getHazardDescription() {
		return hazardDescription;
	}
	public void setHazardDescription(String hazardDescription) {
		this.hazardDescription = hazardDescription;
	}

	@Column(name="result_description",length=300,nullable=false)
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	@ManyToOne(targetEntity=AccidentType.class)
	@JoinColumn(name="accident_type_sn",referencedColumnName="accident_type_sn",nullable=false)
	public AccidentType getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(AccidentType accidentType) {
		this.accidentType = accidentType;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="risk_level",nullable=false)
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn",nullable=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="report_person_id",referencedColumnName="person_id")
	public Person getReportPerson() {
		return reportPerson;
	}
	public void setReportPerson(Person reportPerson) {
		this.reportPerson = reportPerson;
	}
	@Column(name="report_datetime")
	public java.time.LocalDateTime getReportDateTime() {
		return reportDateTime;
	}
	public void setReportDateTime(java.time.LocalDateTime reportDateTime) {
		this.reportDateTime = reportDateTime;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="auditor_id",referencedColumnName="person_id")
	public Person getAuditor() {
		return auditor;
	}
	public void setAuditor(Person auditor) {
		this.auditor = auditor;
	}
	@Column(name="audited_datetime")
	public java.time.LocalDateTime getAuditedDateTime() {
		return auditedDateTime;
	}
	public void setAuditedDateTime(java.time.LocalDateTime auditedDateTime) {
		this.auditedDateTime = auditedDateTime;
	}
	@Column(name="audit_suggestion")
	public String getAuditSuggestion() {
		return auditSuggestion;
	}
	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
	}
	@Column(name="audit_status")
	public Boolean getAuditedStatus() {
		return auditedStatus;
	}
	public void setAuditedStatus(Boolean auditedStatus) {
		this.auditedStatus = auditedStatus;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=StandardIndex.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_hazard_reported",joinColumns=@JoinColumn(name="report_sn",referencedColumnName="report_sn"),inverseJoinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	public Set<StandardIndex> getStandardIndexes() {
		return standardIndexes;
	}
	public void setStandardIndexes(Set<StandardIndex> standardIndexes) {
		this.standardIndexes = standardIndexes;
	}
}
