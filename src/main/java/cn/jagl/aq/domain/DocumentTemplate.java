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
@Table(name="document_template")
public class DocumentTemplate implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	private DepartmentType departmentType;//部门类型	
	private String documentTemplateSn;//文档编号,唯一
	private String documentTemplateName;//文档名称
	private Set<TemplateAttachment> attachments=new HashSet<TemplateAttachment>(0);
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="department_type_sn",columnDefinition="VARCHAR(45)", referencedColumnName="department_type_sn",nullable=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="document_template_sn",length=100,unique=true,nullable=false)
	public String getDocumentTemplateSn() {
		return documentTemplateSn;
	}
	public void setDocumentTemplateSn(String documentTemplateSn) {
		this.documentTemplateSn = documentTemplateSn;
	}
	@Column(name="document_template_name",length=45,nullable=false)
	public String getDocumentTemplateName() {
		return documentTemplateName;
	}
	public void setDocumentTemplateName(String documentTemplateName) {
		this.documentTemplateName = documentTemplateName;
	}
	@OneToMany(targetEntity=TemplateAttachment.class,mappedBy="documentTemplate", cascade=CascadeType.ALL)	
	@JSON(serialize=false)
	public Set<TemplateAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<TemplateAttachment> attachments) {
		this.attachments = attachments;
	}
	@ManyToMany(targetEntity=StandardIndex.class)
	@JoinTable(name="standard_index_document_template",joinColumns=@JoinColumn(name="document_template_sn",referencedColumnName="document_template_sn"),inverseJoinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	@JSON(serialize=false)
	public Set<StandardIndex> getStandardIndexes() {
		return standardIndexes;
	}
	public void setStandardIndexes(Set<StandardIndex> standardIndexes) {
		this.standardIndexes = standardIndexes;
	}
}
