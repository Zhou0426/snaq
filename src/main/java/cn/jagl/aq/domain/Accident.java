package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="accident")
public class Accident implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//��ʶ�У��Զ�����
	private String accidentSn;//�¹ʱ�ţ�����ʱ���Զ�����
	private String accidentName;//�¹�����
	private Department department;//������λ
	private java.time.LocalDateTime happenDateTime;//����ʱ��
	private String happenLocation;//�����ص�
	private AccidentType accidentType;//�¹�����
	private String accidentProcess;//��������
	private Integer deadCount;//��������
	private Integer seriousInjureCount;//��������
	private Integer fleshInjureCount;//��������
	private Float directEconomicLoss;//ֱ�Ӿ�����ʧ
	private Float indirectEconomicLoss;//��Ӿ�����ʧ
	private AccidentLevel accidentLevel;//�¹ʵȼ�
	private String reasonArticle;//������
	private String directReason;//ֱ��ԭ��
	private String indirectReason;//���ԭ��
	private String precautionMeasure;//Ԥ����ʩ
	private Person editor;//�༭��
	private Boolean deleted;//�Ƿ�ɾ��
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="accident_sn",length=20,unique=true)
	public String getAccidentSn() {
		return accidentSn;
	}
	public void setAccidentSn(String accidentSn) {
		this.accidentSn = accidentSn;
	}
	@Column(name="accident_name",length=45)
	public String getAccidentName() {
		return accidentName;
	}
	public void setAccidentName(String accidentName) {
		this.accidentName = accidentName;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="happen_datetime")
	public java.time.LocalDateTime getHappenDateTime() {
		return happenDateTime;
	}
	public void setHappenDateTime(java.time.LocalDateTime happenDateTime) {
		this.happenDateTime = happenDateTime;
	}
	@Column(name="happen_location",length=45)
	public String getHappenLocation() {
		return happenLocation;
	}
	public void setHappenLocation(String happenLocation) {
		this.happenLocation = happenLocation;
	}
	@ManyToOne(targetEntity=AccidentType.class)
	@JoinColumn(name="accident_type_sn",referencedColumnName="accident_type_sn")
	public AccidentType getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(AccidentType accidentType) {
		this.accidentType = accidentType;
	}
	@Basic(fetch = FetchType.LAZY)
	@Column(name="accident_process",columnDefinition="TEXT")
	public String getAccidentProcess() {
		return accidentProcess;
	}
	public void setAccidentProcess(String accidentProcess) {
		this.accidentProcess = accidentProcess;
	}
	@Column(name="dead_count")
	public Integer getDeadCount() {
		return deadCount;
	}
	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}
	@Column(name="serious_injure_count")
	public Integer getSeriousInjureCount() {
		return seriousInjureCount;
	}
	public void setSeriousInjureCount(Integer seriousInjureCount) {
		this.seriousInjureCount = seriousInjureCount;
	}
	@Column(name="flesh_injure_count")
	public Integer getFleshInjureCount() {
		return fleshInjureCount;
	}
	public void setFleshInjureCount(Integer fleshInjureCount) {
		this.fleshInjureCount = fleshInjureCount;
	}
	@Column(name="direct_economic_loss")
	public Float getDirectEconomicLoss() {
		return directEconomicLoss;
	}
	public void setDirectEconomicLoss(Float directEconomicLoss) {
		this.directEconomicLoss = directEconomicLoss;
	}
	@Column(name="indirect_economic_loss")
	public Float getIndirectEconomicLoss() {
		return indirectEconomicLoss;
	}
	public void setIndirectEconomicLoss(Float indirectEconomicLoss) {
		this.indirectEconomicLoss = indirectEconomicLoss;
	}
	@ManyToOne(targetEntity=AccidentLevel.class)
	@JoinColumn(name="accident_level_sn",referencedColumnName="accident_level_sn")
	public AccidentLevel getAccidentLevel() {
		return accidentLevel;
	}
	public void setAccidentLevel(AccidentLevel accidentLevel) {
		this.accidentLevel = accidentLevel;
	}
	@Column(name="reason_article")
	public String getReasonArticle() {
		return reasonArticle;
	}
	public void setReasonArticle(String reasonArticle) {
		this.reasonArticle = reasonArticle;
	}
	@Basic(fetch = FetchType.LAZY)
	@Column(name="direct_reason",columnDefinition="TEXT")
	public String getDirectReason() {
		return directReason;
	}
	public void setDirectReason(String directReason) {
		this.directReason = directReason;
	}
	@Basic(fetch = FetchType.LAZY)
	@Column(name="indirect_reason",columnDefinition="TEXT")
	public String getIndirectReason() {
		return indirectReason;
	}
	public void setIndirectReason(String indirectReason) {
		this.indirectReason = indirectReason;
	}
	@Basic(fetch = FetchType.LAZY)
	@Column(name="precation_measure",columnDefinition="TEXT")
	public String getPrecautionMeasure() {
		return precautionMeasure;
	}
	public void setPrecautionMeasure(String precautionMeasure) {
		this.precautionMeasure = precautionMeasure;
	}
	@ManyToOne(targetEntity=Person.class)
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
}
