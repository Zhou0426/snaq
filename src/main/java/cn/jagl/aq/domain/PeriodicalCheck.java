package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Date;
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

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="periodical_check")
public class PeriodicalCheck implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String periodicalCheckSn;//���ڼ����
	private Standard standard;//���ݱ�׼
	private CheckerFrom checkerFrom;//���������
	private PeriodicalCheckType periodicalCheckType;//���ڼ����������
	private String periodicalCheckName;//���ڼ������
	private Date startDate;//��ʼ����
	private Date endDate;//��������
	private Set<Person> checkers=new HashSet<Person>(0);//�����Ա
	private String strCheckers;//�����Ա���ı�
	private Person editor;//�༭��Ա
	private Boolean deleted;//�Ƿ�ɾ��
	private Department checkedDepartment;//���첿��
	private Set<InconformityItem> inconformityItem=new HashSet<InconformityItem>(0);//��������
	private Set<CheckTable> checkTables=new HashSet<CheckTable>(0);//����
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="periodical_check_sn",unique=true,nullable=false)
	public String getPeriodicalCheckSn() {
		return periodicalCheckSn;
	}
	public void setPeriodicalCheckSn(String periodicalCheckSn) {
		this.periodicalCheckSn = periodicalCheckSn;
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
	@Enumerated(EnumType.ORDINAL)
	@Column(name="perioidical_check_type",nullable=true)
	public PeriodicalCheckType getPeriodicalCheckType() {
		return periodicalCheckType;
	}
	public void setPeriodicalCheckType(PeriodicalCheckType periodicalCheckType) {
		this.periodicalCheckType = periodicalCheckType;
	}
	@Column(name="periodical_check_name")
	public String getPeriodicalCheckName() {
		return periodicalCheckName;
	}
	public void setPeriodicalCheckName(String periodicalCheckName) {
		this.periodicalCheckName = periodicalCheckName;
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
	@JoinTable(name="periodical_check_person",joinColumns=@JoinColumn(name="periodical_check_sn",referencedColumnName="periodical_check_sn"),inverseJoinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"))
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
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="checked_department_sn",referencedColumnName="department_sn")
	public Department getCheckedDepartment() {
		return checkedDepartment;
	}
	public void setCheckedDepartment(Department checkedDepartment) {
		this.checkedDepartment = checkedDepartment;
	}
	
	@OneToMany(targetEntity=InconformityItem.class,mappedBy="periodicalCheck",cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<InconformityItem> getInconformityItem() {
		return inconformityItem;
	}
	public void setInconformityItem(Set<InconformityItem> inconformityItem) {
		this.inconformityItem = inconformityItem;
	}
	@OneToMany(targetEntity=CheckTable.class,mappedBy="periodicalCheck",cascade=CascadeType.ALL)	
	public Set<CheckTable> getCheckTables() {
		return checkTables;
	}
	public void setCheckTables(Set<CheckTable> checkTables) {
		this.checkTables = checkTables;
	}	
}
