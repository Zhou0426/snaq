package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="latent_hazard_attachment")
public class LatentHazardAttachment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private LatentHazard latentHazard;//对应的重大隐患
	private String attachmentType;//附件类型：重大隐患报告、重大隐患治理方案、督办通知书、责任追究报告、其它
	private String logicalFileName;//附件逻辑文件名
	private String physicalFileName;//附件物理文件名
	private Person uploadPerson;//上传人
	private Timestamp  uploadDateTime;//上传时间
	private Boolean deleted;//是否删除	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="attachment_type")
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	@Column(name="logical_filename",length=200)
	public String getLogicalFileName() {
		return logicalFileName;
	}
	public void setLogicalFileName(String logicalFileName) {
		this.logicalFileName = logicalFileName;
	}
	@Column(name="physical_filename",length=200)
	public String getPhysicalFileName() {
		return physicalFileName;
	}
	public void setPhysicalFileName(String physicalFileName) {
		this.physicalFileName = physicalFileName;
	}
	@ManyToOne(targetEntity=LatentHazard.class,cascade=CascadeType.ALL)
	@JoinColumn(name="latent_hazard_sn",referencedColumnName="latent_hazard_sn")
	public LatentHazard getLatentHazard() {
		return latentHazard;
	}
	public void setLatentHazard(LatentHazard latentHazard) {
		this.latentHazard = latentHazard;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="person_id",referencedColumnName="person_id")
	public Person getUploadPerson() {
		return uploadPerson;
	}
	public void setUploadPerson(Person uploadPerson) {
		this.uploadPerson = uploadPerson;
	}
	@Column(name="upload_datetime")
	public Timestamp getUploadDateTime() {
		return uploadDateTime;
	}
	public void setUploadDateTime(Timestamp uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
