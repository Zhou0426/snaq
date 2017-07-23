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
import javax.persistence.Table;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
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
@Table(name="hazard")
@Indexed
@Analyzer(impl=SmartChineseAnalyzer.class)//分词器
@FullTextFilterDefs({
	@FullTextFilterDef(name="hazardDepartmentTypeFilter",impl=DepartmentTypeFilterFactory.class)
})
public class Hazard  implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	@IndexedEmbedded
	private DepartmentType departmentType;//所属部门类型
	private String hazardSn;//危险源（危害）编号
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String hazardDescription;//危险源（危害）名称
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String resultDescription;//后果描述	
	private AccidentType accidentType;//可能造成的事故类型
	private RiskLevel riskLevel;//风险等级
	private Set<ManageObject> manageObjects=new HashSet<ManageObject>(0);
	private Set<Department> departments=new HashSet<Department>(0);//危险源关联的贯标单位
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>(0);//关联的考核评分指标
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="hazard_sn",length=100,unique=true,nullable=false)
	public String getHazardSn() {
		return hazardSn;
	}
	public void setHazardSn(String hazardSn) {
		this.hazardSn = hazardSn;
	}
	@Column(name="hazard_description",length=300,nullable=false)
	public String getHazardDescription() {
		return hazardDescription;
	}
	public void setHazardDescription(String hazardDescription) {
		this.hazardDescription = hazardDescription;
	}
	@Column(name="result_description",length=300,nullable=false)
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	@ManyToOne(targetEntity=AccidentType.class)
	@JoinColumn(name="accident_type_sn",referencedColumnName="accident_type_sn",nullable=false)
	public AccidentType getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(AccidentType accidentType) {
		this.accidentType = accidentType;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="risk_level",nullable=false)
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn",nullable=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@ManyToMany(targetEntity=ManageObject.class,cascade=CascadeType.ALL)
	@JoinTable(name="manage_object_hazard",joinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"),inverseJoinColumns=@JoinColumn(name="manage_object_sn",referencedColumnName="manage_object_sn"))
	public Set<ManageObject> getManageObjects() {
		return manageObjects;
	}
	public void setManageObjects(Set<ManageObject> manageObjects) {
		this.manageObjects = manageObjects;
	}
	@ManyToMany(targetEntity=Department.class)
	@JoinTable(name="hazard_department",joinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"),inverseJoinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"))
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	@ManyToMany(targetEntity=StandardIndex.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_hazard",joinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"),inverseJoinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	public Set<StandardIndex> getStandardIndexes() {
		return standardIndexes;
	}
	public void setStandardIndexes(Set<StandardIndex> standardIndexes) {
		this.standardIndexes = standardIndexes;
	}
}
