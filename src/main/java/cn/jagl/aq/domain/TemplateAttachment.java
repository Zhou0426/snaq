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
@Table(name="template_attachment")
public class TemplateAttachment implements Serializable{
	private int id;//主键，自动增长
	private DocumentTemplate documentTemplate;
	private String attachmentLogicFileName;//附件逻辑文件名
	private String attachmentPhysicalFileName;//附件物理文件名（含路径）
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DocumentTemplate.class,cascade=CascadeType.ALL)
	@JoinColumn(name="document_template_sn",referencedColumnName="document_template_sn")
	public DocumentTemplate getDocumentTemplate() {
		return documentTemplate;
	}
	public void setDocumentTemplate(DocumentTemplate documentTemplate) {
		this.documentTemplate = documentTemplate;
	}
	@Column(name="attachment_logic_file_name",length=100)
	public String getAttachmentLogicFileName() {
		return attachmentLogicFileName;
	}
	public void setAttachmentLogicFileName(String attachmentLogicFileName) {
		this.attachmentLogicFileName = attachmentLogicFileName;
	}
	@Column(name="attachment_physical_file_name",length=200)
	public String getAttachmentPhysicalFileName() {
		return attachmentPhysicalFileName;
	}
	public void setAttachmentPhysicalFileName(String attachmentPhysicalFileName) {
		this.attachmentPhysicalFileName = attachmentPhysicalFileName;
	}

}
