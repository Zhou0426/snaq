package cn.jagl.aq.domain;

import java.io.Serializable;

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
@Table(name="unsafe_act_deduction_point")
public class UnsafeActDeductionPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private int year;
	private DepartmentType departmentType;//单位考核只按照单位类型计算扣分
	private Department department;
	private UnsafeActLevel unsafeActLevel;
	private Double deductionPoints;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="year")
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	@ManyToOne(targetEntity=DepartmentType.class)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn")
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="unsafe_act_level",nullable=false)
	public UnsafeActLevel getUnsafeActLevel() {
		return unsafeActLevel;
	}
	public void setUnsafeActLevel(UnsafeActLevel unsafeActLevel) {
		this.unsafeActLevel = unsafeActLevel;
	}
	
	@Column(name="deduction_points")
	public Double getDeductionPoints() {
		return deductionPoints;
	}
	public void setDeductionPoints(Double deductionPoints) {
		this.deductionPoints = deductionPoints;
	}
	
}
