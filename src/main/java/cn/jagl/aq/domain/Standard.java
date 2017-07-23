package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Indexed
@Entity
@Table(name="standard")
public class Standard implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长	
	private DepartmentType departmentType;//单位类型（该标准对应的贯标单位的类型）	
	private StandardType standardType;//标准类型（枚举）
	private String standardSn;//标准编号
	private String standardName;//标准名称
	private Boolean deleted;//是否删除	
	private Set<Department> implDepartments=new HashSet<Department>(0);//贯标单位
	private Set<StandardIndex> standardIndexs=new HashSet<StandardIndex>(0);
	@DocumentId
	@Id
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
	@Enumerated(EnumType.ORDINAL)
	@Column(name="standard_type")
	public StandardType getStandardType() {
		return standardType;
	}
	public void setStandardType(StandardType standardType) {
		this.standardType = standardType;
	}
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@Column(name="standard_sn",length=100,unique=true,nullable=false)
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@Column(name="standard_name",length=100,nullable=false)
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=Department.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_impl_department",joinColumns=@JoinColumn(name="standard_sn",referencedColumnName="standard_sn"),inverseJoinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"))
	@JSON(serialize=false)
	public Set<Department> getImplDepartments() {
		return implDepartments;
	}
	public void setImplDepartments(Set<Department> implDepartments) {
		this.implDepartments = implDepartments;
	}
	@OneToMany(targetEntity=StandardIndex.class,mappedBy="standard")
	@JSON(serialize=false)
	public Set<StandardIndex> getStandardIndexs() {
		return standardIndexs;
	}
	public void setStandardIndexs(Set<StandardIndex> standardIndexs) {
		this.standardIndexs = standardIndexs;
	}
}
