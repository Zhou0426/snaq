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
@Table(name="inconformity_attachment")
public class InconformityAttachment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private InconformityItem inconformityItem;//��Ӧ�Ĳ�������
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
	@ManyToOne(targetEntity=InconformityItem.class,cascade=CascadeType.ALL)
	@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn")
	public InconformityItem getInconformityItem() {
		return inconformityItem;
	}
	public void setInconformityItem(InconformityItem inconformityItem) {
		this.inconformityItem = inconformityItem;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
