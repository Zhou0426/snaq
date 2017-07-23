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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="latent_hazard")
public class LatentHazard implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private String latentHazardSn;//重大隐患编号，按照时间自动生成
	private CheckClass checkClass;//检查分类
	private Department checkUnit;//检查单位
	private String checkUnitName;//外部检查单位
	private String latentHazardDescription;//重大隐患描述
	private Department department;//发生的贯标单位
	private Department departmentReportTo;//隐患相关业务部门(仅限集团处室单位类型JTCS)
	private java.time.LocalDateTime happenDateTime;//发生时间
	private String happenLocation;//发生地点
	private java.time.LocalDateTime reportDateTime;//上报时间
	private String status;//状态：未上报、已上报、已审核、已销号
	private java.time.LocalDateTime auditDateTime;//审核时间	
	private Person auditor;//审核人
	private String auditSuggestion;//审核意见
	private java.time.LocalDateTime cancelDateTime;//销号时间	
	private Person cancelPerson;//销号人
	private Person editor;//编辑人
	private Boolean deleted;//是否删除
	private Set<LatentHazardAttachment> latentHazardAttachments = new HashSet<LatentHazardAttachment>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="latent_hazard_sn",length=20,unique=true)
	public String getLatentHazardSn() {
		return latentHazardSn;
	}
	public void setLatentHazardSn(String latentHazardSn) {
		this.latentHazardSn = latentHazardSn;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="check_class")
	public CheckClass getCheckClass() {
		return checkClass;
	}
	public void setCheckClass(CheckClass checkClass) {
		this.checkClass = checkClass;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="check_unit",referencedColumnName="department_sn")
	public Department getCheckUnit() {
		return checkUnit;
	}
	public void setCheckUnit(Department checkUnit) {
		this.checkUnit = checkUnit;
	}
	@Column(name="check_unit_name")
	public String getCheckUnitName() {
		return checkUnitName;
	}
	public void setCheckUnitName(String checkUnitName) {
		this.checkUnitName = checkUnitName;
	}
	@Column(name="latent_hazard_description",length=500)
	public String getLatentHazardDescription() {
		return latentHazardDescription;
	}
	public void setLatentHazardDescription(String latentHazardDescription) {
		this.latentHazardDescription = latentHazardDescription;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="report_to_department_sn",referencedColumnName="department_sn")
	public Department getDepartmentReportTo() {
		return departmentReportTo;
	}
	public void setDepartmentReportTo(Department departmentReportTo) {
		this.departmentReportTo = departmentReportTo;
	}
	@Column(name="happen_datetime")
	public java.time.LocalDateTime getHappenDateTime() {
		return happenDateTime;
	}
	public void setHappenDateTime(java.time.LocalDateTime happenDateTime) {
		this.happenDateTime = happenDateTime;
	}
	@Column(name="happen_location",length=45)
	public String getHappenLocation() {
		return happenLocation;
	}
	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="editor_id",referencedColumnName="person_id")
	public Person getEditor() {
		return editor;
	}
	public void setEditor(Person editor) {
		this.editor = editor;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@Column(name="report_datetime")
	public java.time.LocalDateTime getReportDateTime() {
		return reportDateTime;
	}
	public void setReportDateTime(java.time.LocalDateTime reportDateTime) {
		this.reportDateTime = reportDateTime;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="auditor_id",referencedColumnName="person_id")
	public Person getAuditor() {
		return auditor;
	}
	public void setAuditor(Person auditor) {
		this.auditor = auditor;
	}
	@Column(name="audit_suggestion",length=500)
	public String getAuditSuggestion() {
		return auditSuggestion;
	}
	public void setAuditSuggestion(String auditSuggestion) {
		this.auditSuggestion = auditSuggestion;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="cancel_person_id",referencedColumnName="person_id")
	public Person getCancelPerson() {
		return cancelPerson;
	}
	public void setCancelPerson(Person cancelPerson) {
		this.cancelPerson = cancelPerson;
	}	
	@Column(name="audit_datetime")
	public java.time.LocalDateTime getAuditDateTime() {
		return auditDateTime;
	}
	public void setAuditDateTime(java.time.LocalDateTime auditDateTime) {
		this.auditDateTime = auditDateTime;
	}
	@Column(name="cancel_datetime")
	public java.time.LocalDateTime getCancelDateTime() {
		return cancelDateTime;
	}
	public void setCancelDateTime(java.time.LocalDateTime cancelDateTime) {
		this.cancelDateTime = cancelDateTime;
	}
	@OneToMany(targetEntity=LatentHazardAttachment.class,mappedBy="latentHazard",
			fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<LatentHazardAttachment> getLatentHazardAttachments() {
		return latentHazardAttachments;
	}
	public void setLatentHazardAttachments(Set<LatentHazardAttachment> latentHazardAttachments) {
		this.latentHazardAttachments = latentHazardAttachments;
	}
	
}
