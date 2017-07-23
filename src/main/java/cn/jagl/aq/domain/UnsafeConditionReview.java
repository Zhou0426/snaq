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
@Table(name="unsafe_condition_review")
public class UnsafeConditionReview implements Serializable {	
	private int id;//
	private InconformityItem inconformityItem;//对应的不符合项
	private String reviewInfo;//复查信息
	private Timestamp reviewTime;//复查时间
	private Boolean hasCorrectFinished;//已整改完成
	private Timestamp inputTime;//录入时间
	private Person reviewer;//复查人
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=InconformityItem.class)
	@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn",nullable=false)
	public InconformityItem getInconformityItem() {
		return inconformityItem;
	}
	public void setInconformityItem(InconformityItem inconformityItem) {
		this.inconformityItem = inconformityItem;
	}
	@Column(name="review_info")
	public String getReviewInfo() {
		return reviewInfo;
	}
	public void setReviewInfo(String reviewInfo) {
		this.reviewInfo = reviewInfo;
	}
	@Column(name="review_time",nullable=false)
	public Timestamp getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="reviewer_id",referencedColumnName="person_id",nullable=false)
	public Person getReviewer() {
		return reviewer;
	}
	public void setReviewer(Person reviewer) {
		this.reviewer = reviewer;
	}
	@Column(name="has_correct_finished")
	public Boolean getHasCorrectFinished() {
		return hasCorrectFinished;
	}
	public void setHasCorrectFinished(Boolean hasCorrectFinished) {
		this.hasCorrectFinished = hasCorrectFinished;
	}
	@Column(name="input_time")
	public Timestamp getInputTime() {
		return inputTime;
	}
	public void setInputTime(Timestamp inputTime) {
		this.inputTime = inputTime;
	}
}
