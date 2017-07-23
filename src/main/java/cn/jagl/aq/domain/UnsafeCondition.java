package cn.jagl.aq.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("隐患")
public class UnsafeCondition extends InconformityItem implements java.io.Serializable {	
	private static final long serialVersionUID = 1L;
	private ManageObject machine;//机	
	private StandardIndex standardIndex;//指标
	private String problemDescription;//问题描述
	private Float deductPoints;//扣分
	private RiskLevel currentRiskLevel;//现风险等级
	private CorrectType correctType;//整改类型
	private Timestamp correctDeadline;//整改期限
	private Hazard hazrd;//对应的危险源	
	private Person correctPrincipal;//整改负责人
	private String correctProposal;//整改建议	
	private Boolean hasCorrectConfirmed;//已整改确认
	private LocalDate confirmTime;//确认输入时间
	private Boolean hasReviewed;//已复查
	private Boolean hasCorrectFinished;//已整改完成
	private Set<UnsafeConditionDefer> unsafeConditionDefers=new HashSet<UnsafeConditionDefer>();

	
	@ManyToOne(targetEntity=StandardIndex.class,cascade=CascadeType.ALL)
	@JoinColumns(value={@JoinColumn(name="standard_sn",referencedColumnName="standard_sn"),@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	public StandardIndex getStandardIndex() {
		return standardIndex;
	}
	public void setStandardIndex(StandardIndex standardIndex) {
		this.standardIndex = standardIndex;
	}
	@Column(name="problem_description",length=2000)
	public String getProblemDescription() {
		return problemDescription;
	}
	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}
	@Column(name="deduct_points")
	public Float getDeductPoints() {
		return deductPoints;
	}
	public void setDeductPoints(Float deductPoints) {
		this.deductPoints = deductPoints;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="current_risk_level")
	public RiskLevel getCurrentRiskLevel() {
		return currentRiskLevel;
	}
	public void setCurrentRiskLevel(RiskLevel currentRiskLevel) {
		this.currentRiskLevel = currentRiskLevel;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="correct_type",nullable=true)
	public CorrectType getCorrectType() {
		return correctType;
	}
	public void setCorrectType(CorrectType correctType) {
		this.correctType = correctType;
	}
	@Column(name="correct_deadline")
	public Timestamp getCorrectDeadline() {
		return correctDeadline;
	}
	public void setCorrectDeadline(Timestamp correctDeadline) {
		this.correctDeadline = correctDeadline;
	}
	@ManyToOne(targetEntity=Hazard.class,cascade=CascadeType.ALL)
	@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn")
	public Hazard getHazrd() {
		return hazrd;
	}
	public void setHazrd(Hazard hazrd) {
		this.hazrd = hazrd;
	}	
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="correct_principal_sn",referencedColumnName="person_id")
	public Person getCorrectPrincipal() {
		return correctPrincipal;
	}
	public void setCorrectPrincipal(Person correctPrincipal) {
		this.correctPrincipal = correctPrincipal;
	}
	@Column(name="correct_proposal",length=200)
	public String getCorrectProposal() {
		return correctProposal;
	}
	public void setCorrectProposal(String correctProposal) {
		this.correctProposal = correctProposal;
	}
	
	@ManyToOne(targetEntity=ManageObject.class,cascade=CascadeType.ALL)
	@JoinColumn(name="manage_object_sn",referencedColumnName="manage_object_sn")
	public ManageObject getMachine() {
		return machine;
	}
	public void setMachine(ManageObject machine) {
		this.machine = machine;
	}
	
	@Column(name="has_correct_confirmed")
	public Boolean getHasCorrectConfirmed() {
		return hasCorrectConfirmed;
	}
	public void setHasCorrectConfirmed(Boolean hasCorrectConfirmed) {
		this.hasCorrectConfirmed = hasCorrectConfirmed;
	}
	@Column(name="confirm_time",nullable=true)
	public LocalDate getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(LocalDate confirmTime) {
		this.confirmTime = confirmTime;
	}
	@Column(name="has_reviewed")
	public Boolean getHasReviewed() {
		return hasReviewed;
	}
	public void setHasReviewed(Boolean hasReviewed) {
		this.hasReviewed = hasReviewed;
	}
	@Column(name="has_correct_finished")
	public Boolean getHasCorrectFinished() {
		return hasCorrectFinished;
	}
	public void setHasCorrectFinished(Boolean hasCorrectFinished) {
		this.hasCorrectFinished = hasCorrectFinished;
	}
	@OneToMany(targetEntity=UnsafeConditionDefer.class,mappedBy="unsafecondition",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<UnsafeConditionDefer> getUnsafeConditionDefers() {
		return unsafeConditionDefers;
	}
	public void setUnsafeConditionDefers(Set<UnsafeConditionDefer> unsafeConditionDefers) {
		this.unsafeConditionDefers = unsafeConditionDefers;
	}
}
