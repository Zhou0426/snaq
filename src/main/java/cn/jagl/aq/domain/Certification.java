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
@Table(name="certification",uniqueConstraints = {@UniqueConstraint(columnNames={"certification_type_sn", "certification_sn"})})
public class Certification implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增加
	private String certificationSn;//证件编号
	private CertificationType certificationType;
	private Person holder;//持有人
	private java.time.LocalDate issuedDate;//发证日期
	private java.time.LocalDate validStartDate;//有效开始日期
	private java.time.LocalDate validEndDate;//有效结束日期
	private String issuedBy;//发证单位
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="certification_sn")
	public String getCertificationSn() {
		return certificationSn;
	}
	public void setCertificationSn(String certificationSn) {
		this.certificationSn = certificationSn;
	}
	@ManyToOne(targetEntity=CertificationType.class)
	@JoinColumn(name="certification_type_sn",referencedColumnName="certification_type_sn")
	public CertificationType getCertificationType() {
		return certificationType;
	}
	public void setCertificationType(CertificationType certificationType) {
		this.certificationType = certificationType;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="holder_id",referencedColumnName="person_id")
	public Person getHolder() {
		return holder;
	}
	public void setHolder(Person holder) {
		this.holder = holder;
	}
	@Column(name="issued_date")
	public java.time.LocalDate getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(java.time.LocalDate issuedDate) {
		this.issuedDate = issuedDate;
	}
	@Column(name="valid_start_date")
	public java.time.LocalDate getValidStartDate() {
		return validStartDate;
	}
	public void setValidStartDate(java.time.LocalDate validStartDate) {
		this.validStartDate = validStartDate;
	}
	@Column(name="valid_end_date")
	public java.time.LocalDate getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(java.time.LocalDate validEndDate) {
		this.validEndDate = validEndDate;
	}
	@Column(name="issued_by")
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}	
}
