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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="standard_index_audit_method")
public class StandardIndexAuditMethod implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//自动增长，唯一	
	private StandardIndex standardIndex;//对应指标
	private String auditMethodSn;//评分方法编号
	private String auditMethodContent;//评分方法内容
	private Float deduction;//扣分
	private StandardIndex indexDeducted;//扣哪个指标
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=StandardIndex.class,cascade=CascadeType.ALL)
	@JoinColumns(value={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	public StandardIndex getStandardIndex() {
		return standardIndex;
	}
	public void setStandardIndex(StandardIndex standardIndex) {
		this.standardIndex = standardIndex;
	}
	@Column(name="audit_method_sn",length=100,nullable=false)
	public String getAuditMethodSn() {
		return auditMethodSn;
	}
	public void setAuditMethodSn(String auditMethodSn) {
		this.auditMethodSn = auditMethodSn;
	}
	@Column(name="audit_method_content",length=200,nullable=false)
	public String getAuditMethodContent() {
		return auditMethodContent;
	}
	public void setAuditMethodContent(String auditMethodContent) {
		this.auditMethodContent = auditMethodContent;
	}
	@Column(name="deduction")
	public Float getDeduction() {
		return deduction;
	}
	public void setDeduction(Float deduction) {
		this.deduction = deduction;
	}
	@ManyToOne(targetEntity=StandardIndex.class)
	@JoinColumn(name="index_sn_deducted",referencedColumnName="index_sn")
	public StandardIndex getIndexDeducted() {
		return indexDeducted;
	}
	public void setIndexDeducted(StandardIndex indexDeducted) {
		this.indexDeducted = indexDeducted;
	}	
}
