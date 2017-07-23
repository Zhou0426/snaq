package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="check_table")
public class CheckTable implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private SystemAudit systemAudit;//体系审核
	private String checkTableSn;//检查表编号
	private PeriodicalCheck periodicalCheck;//定期检查
	private SpecialCheck specialCheck;//专项检查
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>();//要检查的标准：只存末级指标
	private Set<CheckTableChecker> checkTableCheckers=new HashSet<CheckTableChecker>();//检查表与检查人关联的实体集合
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="check_table_sn",unique=true,nullable=false)
	public String getCheckTableSn() {
		return checkTableSn;
	}
	public void setCheckTableSn(String checkTableSn) {
		this.checkTableSn = checkTableSn;
	}
	@ManyToOne(targetEntity=SystemAudit.class)
	@JoinColumn(name="audit_sn",referencedColumnName="audit_sn")
	public SystemAudit getSystemAudit() {
		return systemAudit;
	}
	public void setSystemAudit(SystemAudit systemAudit) {
		this.systemAudit = systemAudit;
	}
	@ManyToOne(targetEntity=PeriodicalCheck.class)
	@JoinColumn(name="periodical_check_sn",referencedColumnName="periodical_check_sn")
	public PeriodicalCheck getPeriodicalCheck() {
		return periodicalCheck;
	}
	public void setPeriodicalCheck(PeriodicalCheck periodicalCheck) {
		this.periodicalCheck = periodicalCheck;
	}
	@ManyToOne(targetEntity=SpecialCheck.class)
	@JoinColumn(name="special_check_sn",referencedColumnName="special_check_sn")
	public SpecialCheck getSpecialCheck() {
		return specialCheck;
	}
	public void setSpecialCheck(SpecialCheck specialCheck) {
		this.specialCheck = specialCheck;
	}
	@ManyToMany(targetEntity=StandardIndex.class)
	@JoinTable(name="check_table_index",joinColumns=@JoinColumn(name="check_table_sn",referencedColumnName="check_table_sn"),inverseJoinColumns=@JoinColumn(name="index_sn",referencedColumnName="index_sn"))
	public Set<StandardIndex> getStandardIndexes() {
		return standardIndexes;
	}
	public void setStandardIndexes(Set<StandardIndex> standardIndexes) {
		this.standardIndexes = standardIndexes;
	}
	@OneToMany(targetEntity=CheckTableChecker.class,mappedBy="checkTable")
	public Set<CheckTableChecker> getCheckTableCheckers() {
		return checkTableCheckers;
	}
	public void setCheckTableCheckers(Set<CheckTableChecker> checkTableCheckers) {
		this.checkTableCheckers = checkTableCheckers;
	}
	public void addCheckTableChecker(CheckTableChecker checkTableChecker){
		this.checkTableCheckers.add(checkTableChecker);
	}	
}
