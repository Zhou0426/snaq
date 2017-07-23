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
	private int id;//��ʶ�У��Զ�����
	private SystemAudit systemAudit;//��ϵ���
	private String checkTableSn;//������
	private PeriodicalCheck periodicalCheck;//���ڼ��
	private SpecialCheck specialCheck;//ר����
	private Set<StandardIndex> standardIndexes=new HashSet<StandardIndex>();//Ҫ���ı�׼��ֻ��ĩ��ָ��
	private Set<CheckTableChecker> checkTableCheckers=new HashSet<CheckTableChecker>();//���������˹�����ʵ�弯��
	
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
