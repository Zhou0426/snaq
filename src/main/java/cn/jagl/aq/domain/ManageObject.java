package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name="manage_object")
@Indexed
@Analyzer(impl=SmartChineseAnalyzer.class)//�ִ���
public class ManageObject implements Serializable {
	private int id;//�������Զ�����
	private DepartmentType departmentType;//������������NO
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String manageObjectSn;//���������
	@Field(index=Index.YES,analyze=Analyze.YES,store=Store.YES)
	private String manageObjectName;//�����������
	private ManageObjectType manageObjectType;//�����������
	private ManageObject parent;//�����������
	private Set<ManageObject> children=new HashSet<ManageObject>(0);//�Ӽ��������
	private Set<Hazard> hazards=new HashSet<Hazard>(0); 	
	private Set<Department> departments=new HashSet<Department>(0);//�����������Ĺ�굥λ
	
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn")
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="manage_object_sn",length=100,unique=true,nullable=false)
	public String getManageObjectSn() {
		return manageObjectSn;
	}
	public void setManageObjectSn(String manageObjectSn) {
		this.manageObjectSn = manageObjectSn;
	}
	@Column(name="manage_object_name",length=45,nullable=false)
	public String getManageObjectName() {
		return manageObjectName;
	}
	public void setManageObjectName(String manageObjectName) {
		this.manageObjectName = manageObjectName;
	}
	@Enumerated(EnumType.STRING)
	@Column(name="manage_object_type",length=2,nullable=false)
	public ManageObjectType getManageObjectType() {
		return manageObjectType;
	}
	public void setManageObjectType(ManageObjectType manageObjectType) {
		this.manageObjectType = manageObjectType;
	}
	@ManyToOne(targetEntity=ManageObject.class,cascade=CascadeType.ALL)
	@JoinColumn(name="parent_manage_object_sn",referencedColumnName="manage_object_sn")
	public ManageObject getParent() {
		return parent;
	}
	public void setParent(ManageObject parent) {
		this.parent = parent;
	}
	@OneToMany(targetEntity=ManageObject.class,mappedBy="parent")
	public Set<ManageObject> getChildren() {
		return children;
	}
	public void setChildren(Set<ManageObject> children) {
		this.children = children;
	}
	@ManyToMany(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinTable(name="manage_object_department",joinColumns=@JoinColumn(name="manage_object_sn",referencedColumnName="manage_object_sn"),inverseJoinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"))
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	@ManyToMany(targetEntity=Hazard.class,cascade=CascadeType.ALL)
	@JoinTable(name="manage_object_hazard",joinColumns=@JoinColumn(name="manage_object_sn",referencedColumnName="manage_object_sn"),inverseJoinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"),uniqueConstraints=@UniqueConstraint(name="UK_manage_object_hazard",columnNames = {"manage_object_sn", "hazard_sn"}))
	public Set<Hazard> getHazards() {
		return hazards;
	}
	public void setHazards(Set<Hazard> hazards) {
		this.hazards = hazards;
	}
}
