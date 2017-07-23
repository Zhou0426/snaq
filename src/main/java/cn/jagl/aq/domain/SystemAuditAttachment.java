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

/**
 *体系审核附件
 * @author 马辉
 * @since JDK1.8
 * @history 2017年4月18日下午5:58:25 马辉 新建
 */
@Entity
@Table(name="audit_attachment")
public class SystemAuditAttachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1461675170515792365L;
	
	private Integer id;
	
	private SystemAudit systemAudit;//对应体系审核
	
	private String attachmentType;//附件类型
	
	private String logicalFileName;//附件逻辑文件名
	
	private String physicalFileName;//附件物理文件名
	
	private Boolean deleted;//是否删除

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(targetEntity=SystemAudit.class,cascade=CascadeType.ALL)
	@JoinColumn(name="audit_sn",referencedColumnName="audit_sn")
	public SystemAudit getSystemAudit() {
		return systemAudit;
	}

	public void setSystemAudit(SystemAudit systemAudit) {
		this.systemAudit = systemAudit;
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
