package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="unsafe_condition_correct_confirm")
public class UnsafeConditionCorrectConfirm implements Serializable {
	
	private int id;//
	private InconformityItem inconformityItem;//对应的隐患
	@ManyToOne(targetEntity=InconformityItem.class)
	@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn",nullable=false)
	public InconformityItem getInconformityItem() {
		return inconformityItem;
	}
	public void setInconformityItem(InconformityItem inconformityItem) {
		this.inconformityItem = inconformityItem;
	}
	private String confirmInfo;//整改确认信息，可以不填
	private Timestamp confirmTime;//确认输入时间
	private Person confirmer;//确认人
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name="confirm_info")
	public String getConfirmInfo() {
		return confirmInfo;
	}
	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
	}
	@Column(name="confirm_time",nullable=false)
	public Timestamp getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Timestamp confirmTime) {
		this.confirmTime = confirmTime;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="confirmer_id",referencedColumnName="person_id",nullable=false)
	public Person getConfirmer() {
		return confirmer;
	}
	public void setConfirmer(Person confirmer) {
		this.confirmer = confirmer;
	}	
}
