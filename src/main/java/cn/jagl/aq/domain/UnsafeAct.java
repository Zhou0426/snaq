package cn.jagl.aq.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("不安全行为")
public class UnsafeAct extends InconformityItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Person violator;//不安全行为人员
	private String actDescription;//行为描述
	private UnsafeActMark unsafeActMark;//痕迹：有痕、无痕
	private UnsafeActStandard unsafeActStandard;//不安全行为标准
	
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="violator_id",referencedColumnName="person_id")
	public Person getViolator() {
		return violator;
	}
	public void setViolator(Person violator) {
		this.violator = violator;
	}	
	@Column(name="act_description",length=2000)
	public String getActDescription() {
		return actDescription;
	}
	public void setActDescription(String actDescription) {
		this.actDescription = actDescription;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="unsafe_act_mark",nullable=true)
	public UnsafeActMark getUnsafeActMark() {
		return unsafeActMark;
	}
	public void setUnsafeActMark(UnsafeActMark unsafeActMark) {
		this.unsafeActMark = unsafeActMark;
	}
	@ManyToOne(targetEntity=UnsafeActStandard.class)
	@JoinColumn(name="unsafe_act_standard_sn",referencedColumnName="unsafe_act_standard_sn")
	public UnsafeActStandard getUnsafeActStandard() {
		return unsafeActStandard;
	}
	public void setUnsafeActStandard(UnsafeActStandard unsafeActStandard) {
		this.unsafeActStandard = unsafeActStandard;
	}
}
