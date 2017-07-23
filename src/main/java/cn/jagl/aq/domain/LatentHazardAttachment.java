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
	private LatentHazard latentHazard;//��Ӧ���ش�����
	private String attachmentType;//�������ͣ��ش��������桢�ش�����������������֪ͨ�顢����׷�����桢����
	private String logicalFileName;//�����߼��ļ���
	private String physicalFileName;//���������ļ���
	private Person uploadPerson;//�ϴ���
	private Timestamp  uploadDateTime;//�ϴ�ʱ��
	private Boolean deleted;//�Ƿ�ɾ��	
	
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
