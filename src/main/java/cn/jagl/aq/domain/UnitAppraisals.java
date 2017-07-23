package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="unit_appraisals", uniqueConstraints = {@UniqueConstraint(columnNames={"department_sn", "appraisals_date"})})
public class UnitAppraisals implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private Department department;//部门
	private java.time.LocalDate appraisalsDate;//日期
	private Float score;//得分
	private Boolean needComputing;//需要计算
	private java.time.LocalDateTime computeDatetime;//计算时间
	private Byte type;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}	
	@Column(name="appraisals_date")
	public java.time.LocalDate getAppraisalsDate() {
		return appraisalsDate;
	}
	public void setAppraisalsDate(java.time.LocalDate appraisalsDate) {
		this.appraisalsDate = appraisalsDate;
	}
	@Column(name="need_computing")
	public Boolean getNeedComputing() {
		return needComputing;
	}
	public void setNeedComputing(Boolean needComputing) {
		this.needComputing = needComputing;
	}
	@Column(name="score",columnDefinition="decimal(10,2)")
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	@Column(name="compute_datetime")
	public java.time.LocalDateTime getComputeDatetime() {
		return computeDatetime;
	}
	public void setComputeDatetime(java.time.LocalDateTime computeDatetime) {
		this.computeDatetime = computeDatetime;
	}
	@Column(name="type",nullable=false)
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
}
