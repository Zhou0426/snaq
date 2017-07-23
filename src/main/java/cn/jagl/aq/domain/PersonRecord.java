package cn.jagl.aq.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="person_record")
public class PersonRecord {
	private int id;
	private Person person;//人员
	private Department department;//所在部门
	private java.time.LocalDateTime startDateTime;//起始时间
	private java.time.LocalDateTime endDateTime;//结束时间
	private String job;//职务
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="person_id",referencedColumnName="person_id")
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="start_date_time")
	public java.time.LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(java.time.LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	@Column(name="end_date_time")
	public java.time.LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(java.time.LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	@Column(name="job")
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
}
