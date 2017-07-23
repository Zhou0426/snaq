package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="near_miss")
public class NearMiss implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//��ʶ�У��Զ�����
	private String nearMissSn;//δ���¼����
	private Department department;//��������	
	private String eventName;//�¼�����
	private java.sql.Date happenDate;//��������
	private String happenLocation;//�����ص�
	private NearMissType nearMissType;//�¼����
	private String riskResult;//���պ��
	private RiskLevel riskLevel;//���յȼ�
	private String eventProcess;//�¼�����
	private String reasonAnalysis;//ԭ�����
	private String preventMeasure;//������ʩ
	private NearMissState nearMissState;//�¼�״̬
	private Person reportPerson;//�ϱ���
	private Timestamp reportTime;//�ϱ�ʱ��
	private Boolean deleted;//�Ƿ�ɾ��
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="near_miss_sn")
	public String getNearMissSn() {
		return nearMissSn;
	}
	public void setNearMissSn(String nearMissSn) {
		this.nearMissSn = nearMissSn;
	}
	@ManyToOne(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn",nullable=false)
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="event_name")
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	@Column(name="happen_date")
	public java.sql.Date getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(java.sql.Date happenDate) {
		this.happenDate = happenDate;
	}
	@Column(name="happen_location")
	public String getHappenLocation() {
		return happenLocation;
	}
	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	@Column(name="risk_result",length=500)
	public String getRiskResult() {
		return riskResult;
	}
	public void setRiskResult(String riskResult) {
		this.riskResult = riskResult;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="risk_level",nullable=false)
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	@Column(name="event_process",columnDefinition="TEXT", length = 65535)
	public String getEventProcess() {
		return eventProcess;
	}
	public void setEventProcess(String eventProcess) {
		this.eventProcess = eventProcess;
	}
	@Column(name="reason_analysis",columnDefinition="TEXT", length = 65535)
	public String getReasonAnalysis() {
		return reasonAnalysis;
	}
	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}
	@Column(name="prevent_measure",columnDefinition="TEXT", length = 65535)
	public String getPreventMeasure() {
		return preventMeasure;
	}
	public void setPreventMeasure(String preventMeasure) {
		this.preventMeasure = preventMeasure;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="near_miss_state",nullable=false)
	public NearMissState getNearMissState() {
		return nearMissState;
	}
	public void setNearMissState(NearMissState nearMissState) {
		this.nearMissState = nearMissState;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="report_person_id",referencedColumnName="person_id",nullable=false)
	public Person getReportPerson() {
		return reportPerson;
	}
	public void setReportPerson(Person reportPerson) {
		this.reportPerson = reportPerson;
	}
	@Column(name="report_time")
	public Timestamp getReportTime() {
		return reportTime;
	}
	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}
	@ManyToOne(targetEntity=NearMissType.class,cascade=CascadeType.ALL)
	@JoinColumn(name="near_miss_type_sn",referencedColumnName="near_miss_type_sn",nullable=false)
	public NearMissType getNearMissType() {
		return nearMissType;
	}
	public void setNearMissType(NearMissType nearMissType) {
		this.nearMissType = nearMissType;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}	
}
