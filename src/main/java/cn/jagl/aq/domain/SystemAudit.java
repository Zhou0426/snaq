package cn.jagl.aq.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="system_audit")
public class SystemAudit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private String auditSn;//审核编号：根据日期时间自动生成	
	private Department auditedDepartment;//被审核单位
	private Standard standard;//审核标准
	private SystemAuditType systemAuditType;//审核类型：单位审核、季度审核、神华审核
	private LocalDate startDate;//审核开始日期
	private LocalDate endDate;//审核结束日期
	private Person auditTeamLeader;//审核组长
	private String strAuditTeamLeader;//审核组长：文本
	private String strCheckers;//检查人员：文本
	private Set<InconformityItem> inconformityItemes=new HashSet<InconformityItem>(0);//本次审核发现的不符合项
	private Set<Person> auditors=new HashSet<Person>(0);//审核成员
	private Set<CheckTable> checkTables=new HashSet<CheckTable>(0);//审核成员
	private Set<StandardIndex> notScoredIndex=new HashSet<StandardIndex>(0);//不参与计算的指标
	private Set<SystemAuditAttachment> systemAuditAttachments=new HashSet<SystemAuditAttachment>(0);//不参与计算的指标
	private Float designPoints;//设计分值
	private Float realScore;//实际得分
	private Float computedScore;//系统计算得分
	private Float amendScore;//修正分
	private Float finalScore;//最终得分
	private String remark;//备注
	private Person editor;//录入人
	private Boolean deleted;//是否删除
	private LocalDate checkQuarter;//审核季度
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="audit_sn",length=45)
	public String getAuditSn() {
		return auditSn;
	}
	public void setAuditSn(String auditSn) {
		this.auditSn = auditSn;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getAuditedDepartment() {
		return auditedDepartment;
	}
	public void setAuditedDepartment(Department auditedDepartment) {
		this.auditedDepartment = auditedDepartment;
	}
	@ManyToOne(targetEntity=Standard.class)
	@JoinColumn(name="standard_sn",referencedColumnName="standard_sn")
	public Standard getStandard() {
		return standard;
	}
	public void setStandard(Standard standard) {
		this.standard = standard;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="audit_type",nullable=true)
	public SystemAuditType getSystemAuditType() {
		return systemAuditType;
	}
	public void setSystemAuditType(SystemAuditType systemAuditType) {
		this.systemAuditType = systemAuditType;
	}
	@Column(name="start_date")
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	@Column(name="end_date")
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="audit_team_leader_id",referencedColumnName="person_id")
	public Person getAuditTeamLeader() {
		return auditTeamLeader;
	}
	public void setAuditTeamLeader(Person auditTeamLeader) {
		this.auditTeamLeader = auditTeamLeader;
	}
	@Column(name="str_audit_team_leader")
	public String getStrAuditTeamLeader() {
		return strAuditTeamLeader;
	}
	public void setStrAuditTeamLeader(String strAuditTeamLeader) {
		this.strAuditTeamLeader = strAuditTeamLeader;
	}
	@OneToMany(targetEntity=InconformityItem.class,mappedBy="systemAudit")
	@JSON(serialize=false)
	public Set<InconformityItem> getInconformityItemes() {
		return inconformityItemes;
	}
	public void setInconformityItemes(Set<InconformityItem> inconformityItemes) {
		this.inconformityItemes = inconformityItemes;
	}
	@ManyToMany(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinTable(name="system_audit_person",joinColumns=@JoinColumn(name="audit_sn",referencedColumnName="audit_sn"),inverseJoinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"))
	@JSON(serialize=false)
	public Set<Person> getAuditors() {
		return auditors;
	}
	public void setAuditors(Set<Person> auditors) {
		this.auditors = auditors;
	}
	@OneToMany(targetEntity=CheckTable.class,mappedBy="systemAudit")
	@JSON(serialize=false)
	public Set<CheckTable> getCheckTables() {
		return checkTables;
	}
	public void setCheckTables(Set<CheckTable> checkTables) {
		this.checkTables = checkTables;
	}
	
	@OneToMany(targetEntity=SystemAuditAttachment.class,mappedBy="systemAudit")
	@JSON(serialize=false)
	public Set<SystemAuditAttachment> getSystemAuditAttachments() {
		return systemAuditAttachments;
	}
	public void setSystemAuditAttachments(Set<SystemAuditAttachment> systemAuditAttachments) {
		this.systemAuditAttachments = systemAuditAttachments;
	}
	
	@ManyToMany(targetEntity=StandardIndex.class,cascade=CascadeType.ALL)
	@JoinTable(name="system_audit_not_scored_index",joinColumns=@JoinColumn(name="audit_sn",referencedColumnName="audit_sn"),inverseJoinColumns=@JoinColumn(name="index_sn",referencedColumnName="index_sn"))
	@JSON(serialize=false)
	public Set<StandardIndex> getNotScoredIndex() {
		return notScoredIndex;
	}
	public void setNotScoredIndex(Set<StandardIndex> notScoredIndex) {
		this.notScoredIndex = notScoredIndex;
	}
	@Column(name="str_checkers",length=500)
	public String getStrCheckers() {
		return strCheckers;
	}
	public void setStrCheckers(String strCheckers) {
		this.strCheckers = strCheckers;
	}
	@Column(name="design_points")
	public Float getDesignPoints() {
		return designPoints;
	}
	public void setDesignPoints(Float designPoints) {
		this.designPoints = designPoints;
	}
	@Column(name="real_score")
	public Float getRealScore() {
		return realScore;
	}
	public void setRealScore(Float realScore) {
		this.realScore = realScore;
	}
	@Column(name="computed_score")
	public Float getComputedScore() {
		return computedScore;
	}
	public void setComputedScore(Float computedScore) {
		this.computedScore = computedScore;
	}
	@Column(name="amend_score")
	public Float getAmendScore() {
		return amendScore;
	}
	public void setAmendScore(Float amendScore) {
		this.amendScore = amendScore;
	}
	@Column(name="final_score")
	public Float getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(Float finalScore) {
		this.finalScore = finalScore;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	@Column(name="check_quarter")
	public LocalDate getCheckQuarter() {
		return checkQuarter;
	}
	public void setCheckQuarter(LocalDate checkQuarter) {
		this.checkQuarter = checkQuarter;
	}
}
