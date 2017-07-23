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
	private int id;//��ʶ�У��Զ�����
	private int statYear;//ͳ����
	private int statMonth;//ͳ����
	private Department department;//��Ա
	private int loginPersonCount;//��½����
	private float loginRatio;//��½����
	private float avgLoginCount;//�վ���½����
	private float avgLoginRatioPerDay;//�վ���½����
	private int sumOnlineTime;//�ۼ�����ʱ��
	private float avgOnlineTimePerCapitaDay;//�˾�ÿ������ʱ��
	private java.time.LocalDateTime statDateTime;//ͳ��ʱ��
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
