package cn.jagl.aq.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="system_audit_score_detail")
public class SystemAuditScoreDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private SystemAudit systemAudit;//所属于的审核
	private StandardIndex standardIndex;//对应指标
	private Department department;//部门	
	private Integer conformDegree;//百分制符合度
	private LocalDateTime inputDateTime;//录入时间
	private Person editor;//录入人
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=SystemAudit.class)
	@JoinColumn(name="audit_sn",referencedColumnName="audit_sn")
	public SystemAudit getSystemAudit() {
		return systemAudit;
	}
	public void setSystemAudit(SystemAudit systemAudit) {
		this.systemAudit = systemAudit;
	}
	@ManyToOne(targetEntity=StandardIndex.class)
	@JoinColumns(value={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	public StandardIndex getStandardIndex() {
		return standardIndex;
	}
	public void setStandardIndex(StandardIndex standardIndex) {
		this.standardIndex = standardIndex;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="input_datetime")
	public LocalDateTime getInputDateTime() {
		return inputDateTime;
	}	
	public void setInputDateTime(LocalDateTime inputDateTime) {
		this.inputDateTime = inputDateTime;
	}	
	@Column(name="conform_degree")
	public Integer getConformDegree() {
		return conformDegree;
	}
	public void setConformDegree(Integer conformDegree) {
		this.conformDegree = conformDegree;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="editor_id",referencedColumnName="person_id")
	public Person getEditor() {
		return editor;
	}	
	public void setEditor(Person editor) {
		this.editor = editor;
	}
}
