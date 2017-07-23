package cn.jagl.aq.domain;

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
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
/**
 * 
 * @author 孟现飞
 * 日期：2016-06-09
 * 
 * 
 */
@Indexed
@Entity
@Table(name="department_type")
public class DepartmentType implements java.io.Serializable{	
	private static final long serialVersionUID = 1L;
	private int id;
	private String departmentTypeSn;//部门类型编号
	private String departmentTypeName;//部门类型名称
	private DepartmentType parentDepartmentType;//父级部门类型
	private Set<DepartmentType> childDepartmentTypes;//子部门类型
	private Boolean isImplDepartmentType;//
	private int showSequence;//显示顺序
	private Set<RoleResource> roleResources=new HashSet<RoleResource>(0);
	private Set<Standard> standards=new HashSet<Standard>(0);
	private Set<AccidentType> accidentTypes=new HashSet<AccidentType>(0);
	private Set<ManageObject> manageObjects=new HashSet<ManageObject>(0);
	private Set<DocumentTemplate> documentTemplates=new HashSet<DocumentTemplate>(0);
	private Set<SuperviseItem> superviseContents=new HashSet<SuperviseItem>(0);
	private Set<SuperviseDailyReport> superviseDailyReports=new HashSet<SuperviseDailyReport>(0);
		
	@DocumentId
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@Column(name="department_type_sn",unique=true,nullable=false,length=45)
	public String getDepartmentTypeSn() {
		return departmentTypeSn;
	}
	public void setDepartmentTypeSn(String departmentTypeSn) {
		this.departmentTypeSn = departmentTypeSn;
	}
	@Column(name="department_type_name",nullable=false,length=45)
	public String getDepartmentTypeName() {
		return departmentTypeName;
	}
	public void setDepartmentTypeName(String departmentTypeName) {
		this.departmentTypeName = departmentTypeName;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="parent_department_type_sn",referencedColumnName="department_type_sn")
	@JSON(serialize=false)
	public DepartmentType getParentDepartmentType() {
		return parentDepartmentType;
	}
	public void setParentDepartmentType(DepartmentType parentDepartmentType) {
		this.parentDepartmentType = parentDepartmentType;
	}
	@OneToMany(targetEntity=DepartmentType.class,mappedBy="parentDepartmentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<DepartmentType> getChildDepartmentTypes() {
		return childDepartmentTypes;
	}
	public void setChildDepartmentTypes(Set<DepartmentType> childDepartmentTypes) {
		this.childDepartmentTypes = childDepartmentTypes;
	}
	@Column(name="is_impl_department_type",nullable=true)
	public Boolean getIsImplDepartmentType() {
		return isImplDepartmentType;
	}
	public void setIsImplDepartmentType(Boolean isImplDepartmentType) {
		this.isImplDepartmentType = isImplDepartmentType;
	}
	@Column(name="show_sequence",nullable=false)
	public int getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(int showSequence) {
		this.showSequence = showSequence;
	}
	@OneToMany(targetEntity=Standard.class,mappedBy="departmentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<Standard> getStandards() {
		return standards;
	}
	public void setStandards(Set<Standard> standards) {
		this.standards = standards;
	}
	@OneToMany(targetEntity=AccidentType.class,mappedBy="departmentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<AccidentType> getAccidentTypes() {
		return accidentTypes;
	}
	public void setAccidentTypes(Set<AccidentType> accidentTypes) {
		this.accidentTypes = accidentTypes;
	}
	@OneToMany(targetEntity=ManageObject.class,mappedBy="departmentType", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<ManageObject> getManageObjects() {
		return manageObjects;
	}
	public void setManageObjects(Set<ManageObject> manageObjects) {
		this.manageObjects = manageObjects;
	}
	@OneToMany(targetEntity=DocumentTemplate.class,mappedBy="departmentType", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<DocumentTemplate> getDocumentTemplates() {
		return documentTemplates;
	}
	public void setDocumentTemplates(Set<DocumentTemplate> documentTemplates) {
		this.documentTemplates = documentTemplates;
	}
	@OneToMany(targetEntity=SuperviseItem.class,mappedBy="departmentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<SuperviseItem> getSuperviseContents() {
		return superviseContents;
	}
	public void setSuperviseContents(Set<SuperviseItem> superviseContents) {
		this.superviseContents = superviseContents;
	}
	@OneToMany(targetEntity=SuperviseDailyReport.class,mappedBy="departmentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<SuperviseDailyReport> getSuperviseDailyReports() {
		return superviseDailyReports;
	}
	public void setSuperviseDailyReports(Set<SuperviseDailyReport> superviseDailyReports) {
		this.superviseDailyReports = superviseDailyReports;
	}
	@ManyToMany(targetEntity=RoleResource.class,cascade=CascadeType.ALL)
	@JoinTable(name="role_resource_department_type",joinColumns=@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn"),inverseJoinColumns={@JoinColumn(name="role_sn",referencedColumnName="role_sn"),@JoinColumn(name="resource_sn",referencedColumnName="resource_sn")})
	@JSON(serialize=false)
	public Set<RoleResource> getRoleResources() {
		return roleResources;
	}
	public void setRoleResources(Set<RoleResource> roleResources) {
		this.roleResources = roleResources;
	}
}
