package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="supervise_daily_report", uniqueConstraints = {@UniqueConstraint(columnNames={"department_type_sn", "report_date"})})
public class SuperviseDailyReport  implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private DepartmentType departmentType;//单位类型
	private Date reportDate;//日报日期
	private String fileName;//生成的日报文件名
	private java.sql.Timestamp createdTime;//生成的时间
	private Set<SuperviseDailyReportDetails> superviseDailyReportDetails=new HashSet<SuperviseDailyReportDetails>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="report_date",nullable=false)
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(java.sql.Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	@OneToMany(targetEntity=SuperviseDailyReportDetails.class,mappedBy="superviseDailyReport")
	@JSON(serialize=false)
	public Set<SuperviseDailyReportDetails> getSuperviseDailyReportDetails() {
		return superviseDailyReportDetails;
	}
	public void setSuperviseDailyReportDetails(Set<SuperviseDailyReportDetails> superviseDailyReportDetails) {
		this.superviseDailyReportDetails = superviseDailyReportDetails;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn",nullable=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
}
