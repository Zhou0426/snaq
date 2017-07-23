package cn.jagl.aq.domain;

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
@Table(name="check_task_appraisals", uniqueConstraints = {@UniqueConstraint(columnNames={"appraisals_year_month", "department_sn","person_id"})})
public class CheckTaskAppraisals {
	private int id;//�������Զ�����	
	private java.time.LocalDate yearMonth;//��������
	private Department department;//��λ����
	private Person checker;//�����
	private Integer checkTaskCount;//��������������
	private Integer UnsafeActCheckTaskCount;//����ȫ��Ϊ����������
	private Float realCheckCount;//����ʵ�ʼ�����	
	private Float UnsafeActRealCheckCount;//����ȫ��Ϊʵ�ʼ�����	
	private Boolean needComputing;//��Ҫ����
	private java.time.LocalDateTime computeDatetime;//����ʱ��
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="appraisals_year_month")
	public java.time.LocalDate getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(java.time.LocalDate yearMonth) {
		this.yearMonth = yearMonth;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="person_id",referencedColumnName="person_id")
	public Person getChecker() {
		return checker;
	}
	public void setChecker(Person checker) {
		this.checker = checker;
	}
	@Column(name="check_task_count")
	public Integer getCheckTaskCount() {
		return checkTaskCount;
	}
	public void setCheckTaskCount(Integer checkTaskCount) {
		this.checkTaskCount = checkTaskCount;
	}
	@Column(name="unsafeAct_check_task_count")
	public Integer getUnsafeActCheckTaskCount() {
		return UnsafeActCheckTaskCount;
	}
	public void setUnsafeActCheckTaskCount(Integer unsafeActCheckTaskCount) {
		UnsafeActCheckTaskCount = unsafeActCheckTaskCount;
	}
	@Column(name="unsafeAct_real_check_count",columnDefinition="decimal(10,2)")
	public Float getUnsafeActRealCheckCount() {
		return UnsafeActRealCheckCount;
	}
	public void setUnsafeActRealCheckCount(Float unsafeActRealCheckCount) {
		UnsafeActRealCheckCount = unsafeActRealCheckCount;
	}
	@Column(name="real_check_count",columnDefinition="decimal(10,2)")
	public Float getRealCheckCount() {
		return realCheckCount;
	}
	public void setRealCheckCount(Float realCheckCount) {
		this.realCheckCount = realCheckCount;
	}
	@Column(name="need_computing")
	public Boolean getNeedComputing() {
		return needComputing;
	}
	public void setNeedComputing(Boolean needComputing) {
		this.needComputing = needComputing;
	}
	@Column(name="compute_datetime")
	public java.time.LocalDateTime getComputeDatetime() {
		return computeDatetime;
	}
	public void setComputeDatetime(java.time.LocalDateTime computeDatetime) {
		this.computeDatetime = computeDatetime;
	}
}
