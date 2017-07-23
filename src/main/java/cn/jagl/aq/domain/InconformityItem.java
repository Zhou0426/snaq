package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
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
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

@Entity
@DiscriminatorColumn(name="inconformity_item_type",discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("不符合项")
@Table(name="inconformity_item")
public class InconformityItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private SystemAudit systemAudit;//所属于的审核
	private CheckType checkType;//检查类型：动态检查、定期检查、专项检查	
	private CheckerFrom checkerFrom;//检查人来自
	private String inconformityItemSn;//不符合项编号
	private PeriodicalCheck periodicalCheck;//定期检查
	private SpecialCheck specialCheck;//专项检查	
	private Set<Person> checkers=new HashSet<Person>(0);//检查人员
	private String strCheckers;//检查人员：文本
	private Department checkedDepartment;//被检部门
	private Timestamp  checkDateTime;//检查时间
	private  Date editorDateTime;//录入时间
	private String checkLocation;//检查地点
	private InconformityItemNature inconformityItemNature;//不符合项性质
	private Speciality speciality;//所属专业
	private InconformityLevel inconformityLevel;//不符合项等级
	private Map<String,Integer> auditMethods=new HashMap<String,Integer>(0);
	private Set<InconformityAttachment> attachments=new HashSet<InconformityAttachment>(0);
	private Person editor;//录入人	
	private Boolean deleted;//是否删除	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=SystemAudit.class)
	@JoinColumn(name="audit_sn",referencedColumnName="audit_sn")
	public SystemAudit getSystemAudit() {
		return systemAudit;
	}
	public void setSystemAudit(SystemAudit systemAudit) {
		this.systemAudit = systemAudit;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="check_type",nullable=true)
	public CheckType getCheckType() {
		return checkType;
	}
	public void setCheckType(CheckType checkType) {
		this.checkType = checkType;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="check_from",nullable=true)
	public CheckerFrom getCheckerFrom() {
		return checkerFrom;
	}
	public void setCheckerFrom(CheckerFrom checkerFrom) {
		this.checkerFrom = checkerFrom;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="checked_department_sn",referencedColumnName="department_sn")
	public Department getCheckedDepartment() {
		return checkedDepartment;
	}
	public void setCheckedDepartment(Department checkedDepartment) {
		this.checkedDepartment = checkedDepartment;
	}
	@Column(name="inconformity_item_sn",length=45,unique=true,nullable=false)
	public String getInconformityItemSn() {
		return inconformityItemSn;
	}
	public void setInconformityItemSn(String inconformityItemSn) {
		this.inconformityItemSn = inconformityItemSn;
	}
	@ManyToOne(targetEntity=PeriodicalCheck.class,cascade=CascadeType.ALL)
	@JoinColumn(name="periodical_check_sn",referencedColumnName="periodical_check_sn")
	public PeriodicalCheck getPeriodicalCheck() {
		return periodicalCheck;
	}
	public void setPeriodicalCheck(PeriodicalCheck periodicalCheck) {
		this.periodicalCheck = periodicalCheck;
	}
	@ManyToOne(targetEntity=SpecialCheck.class,cascade=CascadeType.ALL)
	@JoinColumn(name="special_check_sn",referencedColumnName="special_check_sn")
	public SpecialCheck getSpecialCheck() {
		return specialCheck;
	}
	public void setSpecialCheck(SpecialCheck specialCheck) {
		this.specialCheck = specialCheck;
	}
	@ManyToMany(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinTable(name="inconformity_item_checker",joinColumns=@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn"),inverseJoinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"))	
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
	@Column(name="check_datetime")
	public Timestamp getCheckDateTime() {
		return checkDateTime;
	}
	public void setCheckDateTime(Timestamp checkDateTime) {
		this.checkDateTime = checkDateTime;
	}
	
	@Column(name="editor_datetime")
	public Date getEditorDateTime() {
		return editorDateTime;
	}
	public void setEditorDateTime(Date editorDateTime) {
		this.editorDateTime = editorDateTime;
	}
	
	@Column(name="check_location",length=45)
	public String getCheckLocation() {
		return checkLocation;
	}
	public void setCheckLocation(String checkLocation) {
		this.checkLocation = checkLocation;
	}
	@OneToMany(targetEntity=InconformityAttachment.class,mappedBy="inconformityItem",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InconformityAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<InconformityAttachment> attachments) {
		this.attachments = attachments;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="inconformity_item_nature",nullable=true)
	public InconformityItemNature getInconformityItemNature() {
		return inconformityItemNature;
	}
	public void setInconformityItemNature(InconformityItemNature inconformityItemNature) {
		this.inconformityItemNature = inconformityItemNature;
	}
	@ManyToOne(targetEntity=Speciality.class,cascade=CascadeType.ALL)
	@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn")
	public Speciality getSpeciality() {
		return speciality;
	}
	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="inconformity_level",nullable=true)
	public InconformityLevel getInconformityLevel() {
		return inconformityLevel;
	}
	public void setInconformityLevel(InconformityLevel inconformityLevel) {
		this.inconformityLevel = inconformityLevel;
	}
	@ElementCollection(targetClass=Integer.class)
	@CollectionTable(name="inconformity_item_audit_record_count",joinColumns=@JoinColumn(name="inconformity_item_sn",referencedColumnName="inconformity_item_sn",nullable=false))
	@MapKeyColumn(name="audit_method_sn")	
	@MapKeyClass(String.class)
	@Column(name="occur_count")
	public Map<String, Integer> getAuditMethods() {
		return auditMethods;
	}
	public void setAuditMethods(Map<String, Integer> auditMethods) {
		this.auditMethods = auditMethods;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="editor_id",referencedColumnName="person_id")
	public Person getEditor() {
		return editor;
	}
	public void setEditor(Person editor) {
		this.editor = editor;
	}
}
