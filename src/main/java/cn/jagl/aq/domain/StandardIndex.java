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

@Indexed
@Entity
@Table(name="standard_index")
@FullTextFilterDefs({
	@FullTextFilterDef(name="standardFilter",impl=StandardIndexFilterFactory.class),
	@FullTextFilterDef(name="standardTypeFilter",impl=StandardTypeFilterFactory.class)	
})
@Analyzer(impl=SmartChineseAnalyzer.class)//分词器
public class StandardIndex implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//自动增长，唯一
	@IndexedEmbedded
	private Standard standard;//所属标准
	@Field(index=Index.YES,analyze=Analyze.YES,store=Store.YES)
	private String indexSn;//指标编号
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String indexName;//指标名称
	private Pdca pdca;//PDCA阶段
	private String auditKeyPoints;//审核要点
	private Set<StandardIndexAuditMethod> auditMethods=new HashSet<StandardIndexAuditMethod>(0);//审核方法
	private Float percentageScore;//百分制分数
	private Integer integerScore;//整数分数
	private Boolean isKeyIndex;//是关键指标
	private String jointIndexIdCode;//共享指标标识
	private Set<Speciality> specialities=new HashSet<Speciality>(0);//所属专业
	private Float anDeduction;//单次扣分
	private int zeroTimes;//几次扣完，得分为零
	private Integer showSequence;//显示顺序
	private Boolean deleted;//是否删除
	private Set<DocumentTemplate> documentTemplates=new HashSet<DocumentTemplate>(0);//相关文档模板
	private Set<Hazard> hazards=new HashSet<Hazard>(0);//关联的危险源
	private Set<InteriorWork> interiorWorks=new HashSet<InteriorWork>(0);
	private StandardIndex parent;//父指标
	private Set<StandardIndex> children=new HashSet<StandardIndex>(0);//子指标
	private Set<CheckTable> checkTables=new HashSet<CheckTable>(0);//存在于哪些检查表
	private Set<SystemAudit> notInScoredSystemAudits=new HashSet<SystemAudit>(0);//不参与哪些体系审核计分
	private Set<HazardReported> hazardReporteds=new HashSet<HazardReported>(0);//关联的危险源上报
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=Standard.class,cascade=CascadeType.ALL)
	@JoinColumn(name="standard_sn",referencedColumnName="standard_sn",nullable=false)
	public Standard getStandard() {
		return standard;
	}
	public void setStandard(Standard standard) {
		this.standard = standard;
	}
	@Column(name="index_sn",length=45,unique=true,nullable=false)
	public String getIndexSn() {
		return indexSn;
	}
	public void setIndexSn(String indexSn) {
		this.indexSn = indexSn;
	}
	@Column(name="index_name",length=2000)
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Enumerated(EnumType.STRING)
	@Column(name="pdca")
	public Pdca getPdca() {
		return pdca;
	}
	public void setPdca(Pdca pdca) {
		this.pdca = pdca;
	}
	@Column(name="audit_key_points",length=300,nullable=true)
	public String getAuditKeyPoints() {
		return auditKeyPoints;
	}
	public void setAuditKeyPoints(String auditKeyPoints) {
		this.auditKeyPoints = auditKeyPoints;
	}
	@OneToMany(targetEntity=StandardIndexAuditMethod.class,mappedBy="standardIndex",cascade=CascadeType.ALL)
	public Set<StandardIndexAuditMethod> getAuditMethods() {
		return auditMethods;
	}
	public void setAuditMethods(Set<StandardIndexAuditMethod> auditMethods) {
		this.auditMethods = auditMethods;
	}
	@Column(name="percentage_score",nullable=true)
	public Float getPercentageScore() {
		return percentageScore;
	}
	public void setPercentageScore(Float percentageScore) {
		this.percentageScore = percentageScore;
	}
	@Column(name="integer_score",nullable=true)
	public Integer getIntegerScore() {
		return integerScore;
	}
	public void setIntegerScore(Integer integerScore) {
		this.integerScore = integerScore;
	}
	@Column(name="is_key_index")
	public Boolean getIsKeyIndex() {
		return isKeyIndex;
	}
	public void setIsKeyIndex(Boolean isKeyIndex) {
		this.isKeyIndex = isKeyIndex;
	}
	@Column(name="joint_index_id_code")
	public String getJointIndexIdCode() {
		return jointIndexIdCode;
	}
	public void setJointIndexIdCode(String jointIndexIdCode) {
		this.jointIndexIdCode = jointIndexIdCode;
	}
	@ManyToMany(targetEntity=Speciality.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="standard_index_speciality",joinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")},inverseJoinColumns=@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn"))
	public Set<Speciality> getSpecialities() {
		return specialities;
	}
	public void setSpecialities(Set<Speciality> specialities) {
		this.specialities = specialities;
	}
	@Column(name="a_deduction")
	public Float getAnDeduction() {
		return anDeduction;
	}
	public void setAnDeduction(Float anDeduction) {
		this.anDeduction = anDeduction;
	}
	@Column(name="zero_times")
	public Integer getZeroTimes() {
		return zeroTimes;
	}
	public void setZeroTimes(int zeroTimes) {
		this.zeroTimes = zeroTimes;
	}
	@Column(name="show_sequence")
	public Integer getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(Integer showSequence) {
		this.showSequence = showSequence;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=DocumentTemplate.class,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_document_template",joinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")},inverseJoinColumns=@JoinColumn(name="document_template_sn",referencedColumnName="document_template_sn"))
	@JSON(serialize=false)
	public Set<DocumentTemplate> getDocumentTemplates() {
		return documentTemplates;
	}
	public void setDocumentTemplates(Set<DocumentTemplate> documentTemplates) {
		this.documentTemplates = documentTemplates;
	}
	@ManyToMany(targetEntity=Hazard.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_hazard",joinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")},inverseJoinColumns=@JoinColumn(name="hazard_sn",referencedColumnName="hazard_sn"))
	public Set<Hazard> getHazards() {
		return hazards;
	}
	public void setHazards(Set<Hazard> hazards) {
		this.hazards = hazards;
	}
	@ManyToMany(targetEntity=InteriorWork.class,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_interior_work",joinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")},inverseJoinColumns=@JoinColumn(name="interior_work_sn",referencedColumnName="interior_work_sn"))
	@JSON(serialize=false)
	public Set<InteriorWork> getInteriorWorks() {
		return interiorWorks;
	}
	public void setInteriorWorks(Set<InteriorWork> interiorWorks) {
		this.interiorWorks = interiorWorks;
	}
	@ManyToOne(targetEntity=StandardIndex.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_index_sn",referencedColumnName="index_sn")
	@JSON(serialize=false)
	public StandardIndex getParent() {
		return parent;
	}
	public void setParent(StandardIndex parent) {
		this.parent = parent;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
	@JSON(serialize=false)
	public Set<StandardIndex> getChildren() {
		return children;
	}
	public void setChildren(Set<StandardIndex> children) {
		this.children = children;
	}
	@ManyToMany(targetEntity=CheckTable.class)
	@JoinTable(name="check_table_index",joinColumns=@JoinColumn(name="index_sn",referencedColumnName="index_sn"),inverseJoinColumns=@JoinColumn(name="check_table_sn",referencedColumnName="check_table_sn"))
	public Set<CheckTable> getCheckTables() {
		return checkTables;
	}
	public void setCheckTables(Set<CheckTable> checkTables) {
		this.checkTables = checkTables;
	}
	@ManyToMany(targetEntity=SystemAudit.class,cascade=CascadeType.ALL)
	@JoinTable(name="system_audit_not_scored_index",joinColumns=@JoinColumn(name="index_sn",referencedColumnName="index_sn"),inverseJoinColumns=@JoinColumn(name="audit_sn",referencedColumnName="audit_sn"))
	@JSON(serialize=false)
	public Set<SystemAudit> getNotInScoredSystemAudits() {
		return notInScoredSystemAudits;
	}
	public void setNotInScoredSystemAudits(Set<SystemAudit> notInScoredSystemAudits) {
		this.notInScoredSystemAudits = notInScoredSystemAudits;
	}
	@ManyToMany(targetEntity=HazardReported.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_hazard_reported",joinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")},inverseJoinColumns=@JoinColumn(name="report_sn",referencedColumnName="report_sn"))
	public Set<HazardReported> getHazardReporteds() {
		return hazardReporteds;
	}
	public void setHazardReporteds(Set<HazardReported> hazardReporteds) {
		this.hazardReporteds = hazardReporteds;
	}
}
