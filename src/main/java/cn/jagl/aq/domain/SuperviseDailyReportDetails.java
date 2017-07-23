package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="supervise_daily_report_details")
public class SuperviseDailyReportDetails  implements Serializable {	
	private static final long serialVersionUID = 1L;
	private int id;
	private SuperviseDailyReport superviseDailyReport;//上报日期
	private Department department;//上报的贯标单位
	private SuperviseItem superviseItem;//上报项目
	private String reportSketch;//上报简述
	private String reportDetails;//上报明细
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn", nullable=false)
	@JSON(serialize=false)
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@ManyToOne(targetEntity=SuperviseItem.class)
	@JoinColumn(name="supervise_item_sn",referencedColumnName="supervise_item_sn", nullable=false)
	@JSON(serialize=false)
	public SuperviseItem getSuperviseItem() {
		return superviseItem;
	}
	public void setSuperviseItem(SuperviseItem superviseItem) {
		this.superviseItem = superviseItem;
	}
	@Column(name="report_sketch",nullable=true,length=20)
	public String getReportSketch() {
		return reportSketch;
	}
	public void setReportSketch(String reportSketch) {
		this.reportSketch = reportSketch;
	}
	@ManyToOne(targetEntity=SuperviseDailyReport.class)
	@JoinColumn(name="supervise_daily_report_id",referencedColumnName="id",nullable=false)
	public SuperviseDailyReport getSuperviseDailyReport() {
		return superviseDailyReport;
	}
	public void setSuperviseDailyReport(SuperviseDailyReport superviseDailyReport) {
		this.superviseDailyReport = superviseDailyReport;
	}
	@Column(name="report_details",nullable=true,length=500)
	public String getReportDetails() {
		return reportDetails;
	}
	public void setReportDetails(String reportDetails) {
		this.reportDetails = reportDetails;
	}	
}
