package cn.jagl.aq.domain;

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
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
@Entity
@Table(name="person")
@Indexed
@Analyzer(impl=SmartChineseAnalyzer.class)//分词器
public class Person implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int id;	
	@Field(index=Index.YES,analyze=Analyze.NO,store=Store.NO)
	private String personId;//人员编号
	@Field(index=Index.YES,analyze=Analyze.YES,store=Store.YES)
	private String personName;//人员姓名
	private String password;//密码
	private String idNumber;//身份证号
	private String cellphoneNumber;//移动电话
	private java.sql.Date birthday;//出生日期
	private Education education;//学历
	private Gender gender;//性别
	private PostLevel postLevel;//岗位等级
	private Department department;//所属部门
	private Boolean hasModifiedPwd;//已修改密码
	private String sessionId;//最后一次在线SessionId
	private String ipAddress;//ip地址	
	private Boolean deleted;//是否删除
	private Set<PersonRecord> records=new HashSet<PersonRecord>(0);
	private Set<Role> roles;//拥有的角色集合
	private Set<InteriorWork> interiorWorks=new HashSet<InteriorWork>(0);
	private Set<PeriodicalCheck> periodicalChecks=new HashSet<PeriodicalCheck>(0);	
	private Set<UnsafeAct> unsafeActs=new HashSet<UnsafeAct>(0);//不安全行为
	private Set<Certification> certificationes=new HashSet<Certification>(0);//证件
	private Set<CheckTableChecker> checkTableCheckers=new HashSet<CheckTableChecker>(0);//
	private Set<CheckTaskAppraisals> checkTaskAppraisals=new HashSet<CheckTaskAppraisals>(0);//检查任务考核
	private Set<PersonUseStat> personUseStat=new HashSet<PersonUseStat>(0);//使用统计
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="person_id",length=45,unique=true,nullable=false)
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Column(name="person_name",length=45,nullable=false)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	@Column(name="password",length=32,nullable=true)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="id_number",length=18,nullable=true)
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	@Column(name="cellphone_number",length=11,nullable=true)
	public String getCellphoneNumber() {
		return cellphoneNumber;
	}
	public void setCellphoneNumber(String cellphoneNumber) {
		this.cellphoneNumber = cellphoneNumber;
	}
	@Column(name="birthday",nullable=true)
	public java.sql.Date getBirthday() {
		return birthday;
	}
	public void setBirthday(java.sql.Date birthday) {
		this.birthday = birthday;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="education",nullable=true)
	public Education getEducation() {
		return education;
	}
	public void setEducation(Education education) {
		this.education = education;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="gender",nullable=true)
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn",nullable=false)
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@ManyToMany(targetEntity=Role.class,cascade=CascadeType.ALL)
	@JoinTable(name="person_role",joinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"),inverseJoinColumns=@JoinColumn(name="role_sn",referencedColumnName="role_sn"))
	@JSON(serialize=false)
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@OneToMany(targetEntity=InteriorWork.class,mappedBy="uploader",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InteriorWork> getInteriorWorks() {
		return interiorWorks;
	}
	public void setInteriorWorks(Set<InteriorWork> interiorWorks) {
		this.interiorWorks = interiorWorks;
	}
	@ManyToMany(targetEntity=PeriodicalCheck.class,cascade=CascadeType.ALL)
	@JoinTable(name="periodical_check_person",joinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"),inverseJoinColumns=@JoinColumn(name="periodical_check_sn",referencedColumnName="periodical_check_sn"))
	@JSON(serialize=false)
	public Set<PeriodicalCheck> getPeriodicalChecks() {
		return periodicalChecks;
	}
	public void setPeriodicalChecks(Set<PeriodicalCheck> periodicalChecks) {
		this.periodicalChecks = periodicalChecks;
	}
	@OneToMany(targetEntity=UnsafeAct.class,mappedBy="violator",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<UnsafeAct> getUnsafeActs() {
		return unsafeActs;
	}
	public void setUnsafeActs(Set<UnsafeAct> unsafeActs) {
		this.unsafeActs = unsafeActs;
	}
	@OneToMany(targetEntity=Certification.class,mappedBy="holder")	
	public Set<Certification> getCertificationes() {
		return certificationes;
	}
	public void setCertificationes(Set<Certification> certificationes) {
		this.certificationes = certificationes;
	}
	@Column(name="hasModifiedPwd",columnDefinition = "boolean default false")
	public Boolean getHasModifiedPwd() {
		return hasModifiedPwd;
	}
	public void setHasModifiedPwd(Boolean hasModifiedPwd) {
		this.hasModifiedPwd = hasModifiedPwd;
	}
	@Column(name="ip_address")
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@ManyToOne(targetEntity=PostLevel.class)
	@JoinColumn(name="post_level_sn",referencedColumnName="post_level_sn")
	public PostLevel getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(PostLevel postLevel) {
		this.postLevel = postLevel;
	}
	@OneToMany(targetEntity=CheckTableChecker.class,mappedBy="checker")
	public Set<CheckTableChecker> getCheckTableCheckers() {
		return checkTableCheckers;
	}
	public void setCheckTableCheckers(Set<CheckTableChecker> checkTableCheckers) {
		this.checkTableCheckers = checkTableCheckers;
	}
	public void addCheckTableChecker(CheckTableChecker checkTableChecker){
		this.checkTableCheckers.add(checkTableChecker);
	}
	@OneToMany(targetEntity=CheckTaskAppraisals.class,mappedBy="checker")
	public Set<CheckTaskAppraisals> getCheckTaskAppraisals() {
		return checkTaskAppraisals;
	}
	public void setCheckTaskAppraisals(Set<CheckTaskAppraisals> checkTaskAppraisals) {
		this.checkTaskAppraisals = checkTaskAppraisals;
	}
	@Column(name="session_id")
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@OneToMany(targetEntity=PersonRecord.class,mappedBy="person")
	public Set<PersonRecord> getRecords() {
		return records;
	}
	public void setRecords(Set<PersonRecord> records) {
		this.records = records;
	}
	@OneToMany(targetEntity=PersonUseStat.class,mappedBy="person")
	public Set<PersonUseStat> getPersonUseStat() {
		return personUseStat;
	}
	public void setPersonUseStat(Set<PersonUseStat> personUseStat) {
		this.personUseStat = personUseStat;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}	
}
