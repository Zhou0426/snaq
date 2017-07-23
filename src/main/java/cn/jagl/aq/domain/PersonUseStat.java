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
@Table(name="person_use_stat",uniqueConstraints = {@UniqueConstraint(columnNames={"stat_year", "stat_month","person_id"})})
public class PersonUseStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private int statYear;//统计年	
	private int statMonth;//统计月
	private Person person;//人员
	private int loginCount;//登陆次数
	private float avgLoginCountPerDay;//日均登陆次数
	private int sumOnlineTime;//累计在线时间
	private float avgOnlineTimePerLogin;//平均每次登陆在线时间
	private float avgOnlineTimePerDay;//日均在线时间
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
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="person_id",referencedColumnName="person_id")
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	@Column(name="login_count")
	public int getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	@Column(name="avg_login_count_per_day")
	public float getAvgLoginCountPerDay() {
		return avgLoginCountPerDay;
	}
	public void setAvgLoginCountPerDay(float avgLoginCountPerDay) {
		this.avgLoginCountPerDay = avgLoginCountPerDay;
	}
	@Column(name="sum_online_time")
	public int getSumOnlineTime() {
		return sumOnlineTime;
	}
	public void setSumOnlineTime(int sumOnlineTime) {
		this.sumOnlineTime = sumOnlineTime;
	}
	@Column(name="avg_online_time_per_login")
	public float getAvgOnlineTimePerLogin() {
		return avgOnlineTimePerLogin;
	}
	public void setAvgOnlineTimePerLogin(float avgOnlineTimePerLogin) {
		this.avgOnlineTimePerLogin = avgOnlineTimePerLogin;
	}
	@Column(name="avg_online_time_per_day")
	public float getAvgOnlineTimePerDay() {
		return avgOnlineTimePerDay;
	}
	public void setAvgOnlineTimePerDay(float avgOnlineTimePerDay) {
		this.avgOnlineTimePerDay = avgOnlineTimePerDay;
	}
	@Column(name="stat_datetime")
	public java.time.LocalDateTime getStatDateTime() {
		return statDateTime;
	}
	public void setStatDateTime(java.time.LocalDateTime statDateTime) {
		this.statDateTime = statDateTime;
	}
}
