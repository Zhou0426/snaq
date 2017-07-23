package cn.jagl.aq.domain;

import java.io.Serializable;

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
@Table(name="management_review_attachment")
public class ManagementReviewAttachment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private ManagementReview managementReview;//��Ӧ�Ĺ�������
	private String attachmentType;//��������
	private String logicalFileName;//�����߼��ļ���
	private String physicalFileName;//���������ļ���
	private Boolean deleted;//�Ƿ�ɾ��	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=ManagementReview.class,cascade=CascadeType.ALL)
	@JoinColumn(name="management_review_sn",referencedColumnName="management_review_sn")
	public ManagementReview getManagementReview() {
		return managementReview;
	}
	public void setManagementReview(ManagementReview managementReview) {
		this.managementReview = managementReview;
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
	
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
