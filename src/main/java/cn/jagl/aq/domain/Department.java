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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name="department")
@Indexed
@Analyzer(impl=SmartChineseAnalyzer.class)//分词器
public class Department implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;		
	private DepartmentType departmentType;//部门类型
	private String departmentSn;//部门编号
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String departmentName;//部门名称
	private Department parentDepartment;//父级部门
	private Set<Department> childDepartments;//子部门
	private String implDepartmentName;//所属贯标单位名称
	private Integer status;//0表示停止生产，1表示正常生产，null没设置
	private Integer showSequence;//显示顺序
	private Boolean deleted;//是否删除
	private Set<RoleResource> roleResources=new HashSet<RoleResource>(0);
	private Set<ManageObject> manageObjects=new HashSet<ManageObject>(0);
	private Set<Hazard> hazards=new HashSet<Hazard>(0);
	private Set<Standard> standards=new HashSet<Standard>(0);
	private Set<InteriorWork> interiorWorks=new HashSet<InteriorWork>(0);
	private Set<UnitAppraisals> unitAppraisals=new HashSet<UnitAppraisals>(0);
	private Person principal;//负责人	
	private Set<CheckTaskAppraisals> checkTaskAppraisals=new HashSet<CheckTaskAppraisals>(0);
	private Set<PersonRecord> records=new HashSet<PersonRecord>(0);//部门下的人员履历
	private Set<DepartmentUseStat> departmentUseStat=new HashSet<DepartmentUseStat>(0);//部门使用统计
	
	
	@ManyToMany(targetEntity=RoleResource.class,cascade=CascadeType.ALL)
	@JoinTable(name="role_resource_department",joinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"),inverseJoinColumns={@JoinColumn(name="role_sn",referencedColumnName="role_sn"),@JoinColumn(name="resource_sn",referencedColumnName="resource_sn")})
	@JSON(serialize=false)
	public Set<RoleResource> getRoleResources() {
		return roleResources;
	}
	public void setRoleResources(Set<RoleResource> roleResources) {
		this.roleResources = roleResources;
	}
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/*
	 * 与父级部门的关系映射
	 * 
	 */
	
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_department_sn",columnDefinition="VARCHAR(45)",referencedColumnName="department_sn")
	@JSON(serialize=false)
	public Department getParentDepartment() {
		return parentDepartment;
	}	
	public void setParentDepartment(Department parent) {
		this.parentDepartment = parent;
	}
	/*
	 * 与子部门的关系映射
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentDepartment", fetch = FetchType.LAZY)
	@JSON(serialize=false)
	public Set<Department> getChildDepartments() {  
	    return childDepartments;
	}
	public void setChildDepartments(Set<Department> children){
		this.childDepartments=children;
	}
	/*
	 * 部门、部门类型的关系映射
	 */
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="department_type_sn",columnDefinition="VARCHAR(45)", referencedColumnName="department_type_sn",nullable=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="department_sn",length=45,unique=true,nullable=false)
	public String getDepartmentSn() {
		return departmentSn;
	}
	public void setDepartmentSn(String departmentSn) {
		this.departmentSn = departmentSn;
	}
	@Column(name="department_name",length=45,unique=false,nullable=false)
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	@Column(name="impl_department_name")
	public String getImplDepartmentName() {
		return implDepartmentName;
	}
	public void setImplDepartmentName(String implDepartmentName) {
		this.implDepartmentName = implDepartmentName;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="show_sequence",nullable=false)
	public Integer getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(Integer showSequence) {
		this.showSequence = showSequence;
	}
	@Column(name="deleted",nullable=false)
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=ManageObject.class,cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="manage_object_department",joinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"),inverseJoinColumns=@JoinColumn(name="manage_object_sn",referencedColumnName="manage_object_sn"))
	@JSON(serialize=false)
	public Set<ManageObject> getManageObjects() {
		return manageObjects;
	}
	public void setManageObjects(Set<ManageObject> manageObjects) {
		this.manageObjects = manageObjects;
	}
	@ManyToMany(targetEntity=Hazard.class,cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="hazard_department",joinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"),inverseJoinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"))
	@JSON(serialize=false)
	public Set<Hazard> getHazards() {
		return hazards;
	}
	public void setHazards(Set<Hazard> hazards) {
		this.hazards = hazards;
	}
	@ManyToMany(targetEntity=Standard.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_impl_department",joinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"),inverseJoinColumns=@JoinColumn(name="standard_sn",referencedColumnName="standard_sn"))
	@JSON(serialize=false)
	public Set<Standard> getStandards() {
		return standards;
	}
	public void setStandards(Set<Standard> standards) {
		this.standards = standards;
	}
	@OneToMany(targetEntity=InteriorWork.class,mappedBy="department",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InteriorWork> getInteriorWorks() {
		return interiorWorks;
	}
	public void setInteriorWorks(Set<InteriorWork> interiorWorks) {
		this.interiorWorks = interiorWorks;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="principal_id",referencedColumnName="person_id")
	public Person getPrincipal() {
		return principal;
	}
	public void setPrincipal(Person principal) {
		this.principal = principal;
	}
	@OneToMany(targetEntity=UnitAppraisals.class,mappedBy="department")
	public Set<UnitAppraisals> getUnitAppraisals() {
		return unitAppraisals;
	}
	public void setUnitAppraisals(Set<UnitAppraisals> unitAppraisals) {
		this.unitAppraisals = unitAppraisals;
	}
	@OneToMany(targetEntity=CheckTaskAppraisals.class,mappedBy="department")
	public Set<CheckTaskAppraisals> getCheckTaskAppraisals() {
		return checkTaskAppraisals;
	}
	public void setCheckTaskAppraisals(Set<CheckTaskAppraisals> checkTaskAppraisals) {
		this.checkTaskAppraisals = checkTaskAppraisals;
	}
	@OneToMany(targetEntity=PersonRecord.class,mappedBy="department")
	public Set<PersonRecord> getRecords() {
		return records;
	}
	public void setRecords(Set<PersonRecord> records) {
		this.records = records;
	}
	@OneToMany(targetEntity=DepartmentUseStat.class,mappedBy="department")
	public Set<DepartmentUseStat> getDepartmentUseStat() {
		return departmentUseStat;
	}
	public void setDepartmentUseStat(Set<DepartmentUseStat> departmentUseStat) {
		this.departmentUseStat = departmentUseStat;
	}	
}
