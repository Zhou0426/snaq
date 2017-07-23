package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="management_review")
public class ManagementReview implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private Integer auditYear;//评审年份
	private String reviewSn;//评审编号
	private Department department;//评审单位
	private String purpose;//评审目的
	private String scope;//评审范围
	private String reviewBasis;//评审依据
	private java.time.LocalDate reviewDate;//评审时间
	private String reviewLocation;//评审地点
	private Person reviewEmcee;//评审主持人
	private Set<Person> participants=new HashSet<Person>(0);
	private String reviewContent;//评审内容
	private String reviewRequirement;//评审要求
	private String reviewMethod;//评审方法
	private String reviewInput;//评审输入
	private String reviewResult;//评审结果
	private String correctPrevention;//纠正与预防措施
	private String resultConclusion;//结果总结
	private String reviewOutput;//评审输出
	private Set<ManagementReviewAttachment> managementReviewAttachments=new HashSet<ManagementReviewAttachment>(0);
	private Boolean deleted;//逻辑删除

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name="audit_year")
	public Integer getAuditYear() {
		return auditYear;
	}

	public void setAuditYear(Integer auditYear) {
		this.auditYear = auditYear;
	}
	@Column(name="management_review_sn",unique=true)
	public String getReviewSn() {
		return reviewSn;
	}	
	public void setReviewSn(String reviewSn) {
		this.reviewSn = reviewSn;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	@Type(type="text") 
	@Column(name="purpose")
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Type(type="text") 
	@Column(name="scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	@Type(type="text") 
	@Column(name="review_basis")
	public String getReviewBasis() {
		return reviewBasis;
	}

	public void setReviewBasis(String reviewBasis) {
		this.reviewBasis = reviewBasis;
	}
	@Column(name="review_date")
	public java.time.LocalDate getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(java.time.LocalDate reviewDate) {
		this.reviewDate = reviewDate;
	}
	@Column(name="review_location")
	public String getReviewLocation() {
		return reviewLocation;
	}

	public void setReviewLocation(String reviewLocation) {
		this.reviewLocation = reviewLocation;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="emcee_id",referencedColumnName="person_id")
	public Person getReviewEmcee() {
		return reviewEmcee;
	}

	public void setReviewEmcee(Person reviewEmcee) {
		this.reviewEmcee = reviewEmcee;
	}
	@ManyToMany(targetEntity=Person.class)
	@JoinTable(name="management_review_participant",joinColumns=@JoinColumn(name="management_review_sn",referencedColumnName="management_review_sn"),inverseJoinColumns=@JoinColumn(name="participant_id",referencedColumnName="person_id"))
	public Set<Person> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<Person> participants) {
		this.participants = participants;
	}
	@Type(type="text") 
	@Column(name="review_content")
	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	@Type(type="text") 
	@Column(name="review_requirement")
	public String getReviewRequirement() {
		return reviewRequirement;
	}

	public void setReviewRequirement(String reviewRequirement) {
		this.reviewRequirement = reviewRequirement;
	}
	@Type(type="text") 
	@Column(name="review_method")
	public String getReviewMethod() {
		return reviewMethod;
	}

	public void setReviewMethod(String reviewMethod) {
		this.reviewMethod = reviewMethod;
	}
	@Type(type="text") 
	@Column(name="review_input")
	public String getReviewInput() {
		return reviewInput;
	}

	public void setReviewInput(String reviewInput) {
		this.reviewInput = reviewInput;
	}
	@Type(type="text") 
	@Column(name="review_result")
	public String getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}
	@Type(type="text") 
	@Column(name="correct_prevention")
	public String getCorrectPrevention() {
		return correctPrevention;
	}

	public void setCorrectPrevention(String correctPrevention) {
		this.correctPrevention = correctPrevention;
	}
	@Type(type="text") 
	@Column(name="result_conclusion")
	public String getResultConclusion() {
		return resultConclusion;
	}

	public void setResultConclusion(String resultConclusion) {
		this.resultConclusion = resultConclusion;
	}
	@Type(type="text") 
	@Column(name="review_output")
	public String getReviewOutput() {
		return reviewOutput;
	}

	public void setReviewOutput(String reviewOutput) {
		this.reviewOutput = reviewOutput;
	}
	@OneToMany(targetEntity=ManagementReviewAttachment.class,mappedBy="managementReview")	
	public Set<ManagementReviewAttachment> getManagementReviewAttachments() {
		return managementReviewAttachments;
	}
	public void setManagementReviewAttachments(Set<ManagementReviewAttachment> managementReviewAttachments) {
		this.managementReviewAttachments = managementReviewAttachments;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	
}
