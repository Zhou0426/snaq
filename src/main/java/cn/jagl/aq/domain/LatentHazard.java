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
	private int id;//��ʶ�У��Զ�����
	private String latentHazardSn;//�ش�������ţ�����ʱ���Զ�����
	private CheckClass checkClass;//������
	private Department checkUnit;//��鵥λ
	private String checkUnitName;//�ⲿ��鵥λ
	private String latentHazardDescription;//�ش���������
	private Department department;//�����Ĺ�굥λ
	private Department departmentReportTo;//�������ҵ����(���޼��Ŵ��ҵ�λ����JTCS)
	private java.time.LocalDateTime happenDateTime;//����ʱ��
	private String happenLocation;//�����ص�
	private java.time.LocalDateTime reportDateTime;//�ϱ�ʱ��
	private String status;//״̬��δ�ϱ������ϱ�������ˡ�������
	private java.time.LocalDateTime auditDateTime;//���ʱ��	
	private Person auditor;//�����
	private String auditSuggestion;//������
	private java.time.LocalDateTime cancelDateTime;//����ʱ��	
	private Person cancelPerson;//������
	private Person editor;//�༭��
	private Boolean deleted;//�Ƿ�ɾ��
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
