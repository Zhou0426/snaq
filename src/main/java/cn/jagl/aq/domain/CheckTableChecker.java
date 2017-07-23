package cn.jagl.aq.domain;

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
@Table(name="check_table_checker",uniqueConstraints = {@UniqueConstraint(columnNames={"check_table_sn", "checker_id"})})
public class CheckTableChecker {
	private int id;//标识列，自动增长
	private CheckTable checkTable;//检查表
	private Person checker;//检查人
	private Boolean hasCompletedCheck;//已完成检查
	private java.time.LocalDateTime affirmDateTime;//确认时间
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=CheckTable.class,cascade = CascadeType.ALL)
	@JoinColumn(name="check_table_sn",referencedColumnName="check_table_sn",nullable=false)
	public CheckTable getCheckTable() {
		return checkTable;
	}
	public void setCheckTable(CheckTable checkTable) {
		this.checkTable = checkTable;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="checker_id",referencedColumnName="person_id",nullable=false)
	public Person getChecker() {
		return checker;
	}
	public void setChecker(Person checker) {
		this.checker = checker;
	}
	@Column(name="has_completed_check")
	public Boolean getHasCompletedCheck() {
		return hasCompletedCheck;
	}
	public void setHasCompletedCheck(Boolean hasCompletedCheck) {
		this.hasCompletedCheck = hasCompletedCheck;
	}
	@Column(name="affirm_datetime")
	public java.time.LocalDateTime getAffirmDateTime() {
		return affirmDateTime;
	}
	public void setAffirmDateTime(java.time.LocalDateTime affirmDateTime) {
		this.affirmDateTime = affirmDateTime;
	}
}
