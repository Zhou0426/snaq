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

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name="unsafe_act_standard")
@Indexed
@Analyzer(impl=SmartChineseAnalyzer.class)//分词器
@FullTextFilterDefs({
	@FullTextFilterDef(name="departmentTypeFilter",impl=DepartmentTypeFilterFactory.class)
})
public class UnsafeActStandard implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	@IndexedEmbedded
	private DepartmentType departmentType;//单位类型
	private String standardSn;//标准编号
	@Field(index=Index.YES,analyze=Analyze.YES,store=Store.YES)
	private String standardDescription;//标准名称
	private UnsafeActLevel unsafeActLevel;//不安全行为等级
	private Set<Speciality> specialities=new HashSet<Speciality>(0);//不安全行为所属专业
	private UnsafeActStandard parent;//父级标准
	
	private Set<UnsafeActStandard> children=new HashSet<UnsafeActStandard>(0);//子级标准
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DepartmentType.class)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn")
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="unsafe_act_standard_sn",length=45,unique=true)
	public String getStandardSn() {
		return standardSn;
	}
	public void setStandardSn(String standardSn) {
		this.standardSn = standardSn;
	}
	@Column(name="standard_description",length=300)
	public String getStandardDescription() {
		return standardDescription;
	}
	public void setStandardDescription(String standardDescription) {
		this.standardDescription = standardDescription;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="unsafe_act_level",nullable=true)
	public UnsafeActLevel getUnsafeActLevel() {
		return unsafeActLevel;
	}
	public void setUnsafeActLevel(UnsafeActLevel unsafeActLevel) {
		this.unsafeActLevel = unsafeActLevel;
	}
	@ManyToMany(targetEntity=Speciality.class)
	@JoinTable(name="unsafe_act_speciality",joinColumns=@JoinColumn(name="unsafe_act_standard_sn",referencedColumnName="unsafe_act_standard_sn"),inverseJoinColumns=@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn"))
	@JSON(serialize=false)
	public Set<Speciality> getSpecialities() {
		return specialities;
	}
	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}
	@ManyToOne(targetEntity=UnsafeActStandard.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_unsafe_act_standard_sn",referencedColumnName="unsafe_act_standard_sn")
	@JSON(serialize=false)
	public UnsafeActStandard getParent() {
		return parent;
	}
	public void setParent(UnsafeActStandard parent) {
		this.parent = parent;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
	@JSON(serialize=false)
	public Set<UnsafeActStandard> getChildren() {
		return children;
	}
	public void setChildren(Set<UnsafeActStandard> children) {
		this.children = children;
	}
}
