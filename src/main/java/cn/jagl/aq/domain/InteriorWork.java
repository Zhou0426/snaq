package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="interior_work")
public class InteriorWork implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	private String interiorWorkSn;//内业资料编号
	private String interiorWorkNname;//内业资料名称
	private String attachmentPath;//附件路径
	private Person uploader;//上传人
	private java.sql.Timestamp uploadDatetime;//上传时间
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>(0);//相关指标
	private Department department;//上传人所在部门
	private Set<InteriorWorkAttachment> attachments=new HashSet<InteriorWorkAttachment>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="interior_work_sn",length=45,unique=true,nullable=false)
	public String getInteriorWorkSn() {
		return interiorWorkSn;
	}
	public void setInteriorWorkSn(String interiorWorkSn) {
		this.interiorWorkSn = interiorWorkSn;
	}
	@Column(name="interior_work_name",length=200,nullable=false)
	public String getInteriorWorkNname() {
		return interiorWorkNname;
	}
	public void setInteriorWorkNname(String interiorWorkNname) {
		this.interiorWorkNname = interiorWorkNname;
	}
	@Column(name="attachment_path",length=200,nullable=false)
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="person_id",referencedColumnName="person_id")
	public Person getUploader() {
		return uploader;
	}
	public void setUploader(Person uploader) {
		this.uploader = uploader;
	}
	@Column(name="upload_datetime")
	public java.sql.Timestamp getUploadDatetime() {
		return uploadDatetime;
	}
	public void setUploadDatetime(java.sql.Timestamp uploadDatetime) {
		this.uploadDatetime = uploadDatetime;
	}
	@ManyToMany(targetEntity=StandardIndex.class,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_interior_work",joinColumns=@JoinColumn(name="interior_work_sn",referencedColumnName="interior_work_sn"),inverseJoinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	@JSON(serialize=false)
	public Set<StandardIndex> getStandardIndexes() {
		return standardIndexes;
	}
	public void setStandardIndexes(Set<StandardIndex> standardIndexes) {
		this.standardIndexes = standardIndexes;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@OneToMany(targetEntity=InteriorWorkAttachment.class,
			mappedBy="interiorWork",
			fetch=FetchType.LAZY,
			cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InteriorWorkAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<InteriorWorkAttachment> attachments) {
		this.attachments = attachments;
	}
}
