package cn.jagl.aq.domain;

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
import org.apache.struts2.json.annotations.JSON;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="special_check")
public class SpecialCheck implements Serializable  {
	private static final long serialVersionUID = 1L;
	private int id;
	private String specialCheckSn;//专项检查编号
	private Standard standard;//依据标准
	private CheckerFrom checkerFrom;//检查人来自
	private String specialCheckName;//专项检查名称
	private Date startDate;//开始日期
	private Date endDate;//结束日期
	private Set<Speciality> specialities=new HashSet<Speciality>(0);
	private Set<Person> checkers=new HashSet<Person>(0);//检查人员
	private String strCheckers;//检查人员：文本
	private Person editor;//编辑人员
	private Boolean deleted;//是否删除
	private Department checkedDepartment;//被检部门
	private Set<InconformityItem> inconformityItem=new HashSet<InconformityItem>(0);//不符合项
	private Set<CheckTable> checkTables=new HashSet<CheckTable>(0);//检查表
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="special_check_sn",unique=true,nullable=false)
	public String getSpecialCheckSn() {
		return specialCheckSn;
	}
	public void setSpecialCheckSn(String specialCheckSn) {
		this.specialCheckSn = specialCheckSn;
	}
	@ManyToOne(targetEntity=Standard.class)
	@JoinColumn(name="standard_sn",referencedColumnName="standard_sn")
	public Standard getStandard() {
		return standard;
	}	
	public void setStandard(Standard standard) {
		this.standard = standard;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="check_from",nullable=true)
	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}
	@Column(name="special_check_name")
	public String getSpecialCheckName() {
		return specialCheckName;
	}
	public void setSpecialCheckName(String specialCheckName) {
		this.specialCheckName = specialCheckName;
	}
	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@ManyToMany(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinTable(name="special_check_person",joinColumns=@JoinColumn(name="special_check_sn",referencedColumnName="special_check_sn"),inverseJoinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"))
	@JSON(serialize=false)
	public Set<Person> getCheckers() {
		return checkers;
	}
	public void setCheckers(Set<Person> checkers) {
		this.checkers = checkers;
	}
	@Column(name="str_checkers")
	public String getStrCheckers() {
		return strCheckers;
	}
	public void setStrCheckers(String strCheckers) {
		this.strCheckers = strCheckers;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="editor_id",referencedColumnName="person_id")
	public Person getEditor() {
		return editor;
	}
	public void setEditor(Person editor) {
		this.editor = editor;
	}
	@ManyToMany(targetEntity=Speciality.class,cascade=CascadeType.ALL)
	@JoinTable(name="special_check_speciality",joinColumns=@JoinColumn(name="special_check_sn",referencedColumnName="special_check_sn"),inverseJoinColumns=@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn"))
	@JSON(serialize=false)
	public Set<Speciality> getSpecialities() {
		return specialities;
	}
	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}
	
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="checked_department_sn",referencedColumnName="department_sn")
	public Department getCheckedDepartment() {
		return checkedDepartment;
	}
	public void setCheckedDepartment(Department checkedDepartment) {
		this.checkedDepartment = checkedDepartment;
	}
	
	@OneToMany(targetEntity=InconformityItem.class,mappedBy="specialCheck",cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InconformityItem> getInconformityItem() {
		return inconformityItem;
	}
	public void setInconformityItem(Set<InconformityItem> inconformityItem) {
		this.inconformityItem = inconformityItem;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@OneToMany(targetEntity=CheckTable.class,mappedBy="specialCheck",cascade=CascadeType.ALL)	
	public Set<CheckTable> getCheckTables() {
		return checkTables;
	}
	public void setCheckTables(Set<CheckTable> checkTables) {
		this.checkTables = checkTables;
	}
}
