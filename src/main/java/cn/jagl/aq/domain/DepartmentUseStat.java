package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name="department_use_stat",uniqueConstraints = {@UniqueConstraint(columnNames={"stat_year", "stat_month","department_sn"})})
public class DepartmentUseStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private int statYear;//统计年
	private int statMonth;//统计月
	private Department department;//人员
	private int loginPersonCount;//登陆人数
	private float loginRatio;//登陆比率
	private float avgLoginCount;//日均登陆人数
	private float avgLoginRatioPerDay;//日均登陆比率
	private int sumOnlineTime;//累计在线时间
	private float avgOnlineTimePerCapitaDay;//人均每天在线时间
	private java.time.LocalDateTime statDateTime;//统计时间
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="stat_year")
	public int getStatYear() {
		return statYear;
	}
	public void setStatYear(int statYear) {
		this.statYear = statYear;
	}
	@Column(name="stat_month")
	public int getStatMonth() {
		return statMonth;
	}
	public void setStatMonth(int statMonth) {
		this.statMonth = statMonth;
	}	
	@Column(name="sum_online_time")
	public int getSumOnlineTime() {
		return sumOnlineTime;
	}
	public void setSumOnlineTime(int sumOnlineTime) {
		this.sumOnlineTime = sumOnlineTime;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn",nullable=false)
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="login_person_count")
	public int getLoginPersonCount() {
		return loginPersonCount;
	}
	public void setLoginPersonCount(int loginPersonCount) {
		this.loginPersonCount = loginPersonCount;
	}
	@Column(name="login_ratio")
	public float getLoginRatio() {
		return loginRatio;
	}
	public void setLoginRatio(float loginRatio) {
		this.loginRatio = loginRatio;
	}
	@Column(name="avg_login_count")
	public float getAvgLoginCount() {
		return avgLoginCount;
	}
	public void setAvgLoginCount(float avgLoginCount) {
		this.avgLoginCount = avgLoginCount;
	}
	@Column(name="avg_login_ratio_per_day")
	public float getAvgLoginRatioPerDay() {
		return avgLoginRatioPerDay;
	}
	public void setAvgLoginRatioPerDay(float avgLoginRatioPerDay) {
		this.avgLoginRatioPerDay = avgLoginRatioPerDay;
	}
	@Column(name="avg_online_time_per_capita_day")
	public float getAvgOnlineTimePerCapitaDay() {
		return avgOnlineTimePerCapitaDay;
	}
	public void setAvgOnlineTimePerCapitaDay(float avgOnlineTimePerCapitaDay) {
		this.avgOnlineTimePerCapitaDay = avgOnlineTimePerCapitaDay;
	}
	@Column(name="stat_datetime")
	public java.time.LocalDateTime getStatDateTime() {
		return statDateTime;
	}
	public void setStatDateTime(java.time.LocalDateTime statDateTime) {
		this.statDateTime = statDateTime;
	}
}
