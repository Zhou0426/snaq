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
@Table(name="resource_use_stat", uniqueConstraints = {@UniqueConstraint(columnNames={"stat_year", "stat_month","resource_sn"})})
public class ResourceUseStat implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//��ʶ�У��Զ�����
	private int statYear;//ͳ����
	private int statMonth;//ͳ����
	private Resource resource;//��Դ��
	private int sumClickCount;//�ۼƵ������	
	private float avgClickCountPerDay;//�վ��������
	private int sumUserCount;//�ۼ�ʹ������
	private float avgUserCountPerDay;//�վ�ʹ������
	
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
	@ManyToOne(targetEntity=Resource.class)
	@JoinColumn(name="resource_sn",referencedColumnName="resource_sn")
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	@Column(name="sum_click_count")
	public int getSumClickCount() {
		return sumClickCount;
	}
	public void setSumClickCount(int sumClickCount) {
		this.sumClickCount = sumClickCount;
	}
	@Column(name="avg_click_count_per_day")
	public float getAvgClickCountPerDay() {
		return avgClickCountPerDay;
	}
	public void setAvgClickCountPerDay(float avgClickCountPerDay) {
		this.avgClickCountPerDay = avgClickCountPerDay;
	}
	@Column(name="sum_user_count")
	public int getSumUserCount() {
		return sumUserCount;
	}
	public void setSumUserCount(int sumUserCount) {
		this.sumUserCount = sumUserCount;
	}
	@Column(name="avg_user_count_per_day")
	public float getAvgUserCountPerDay() {
		return avgUserCountPerDay;
	}
	public void setAvgUserCountPerDay(float avgUserCountPerDay) {
		this.avgUserCountPerDay = avgUserCountPerDay;
	}
}
