package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="accident_type")
public class AccidentType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private DepartmentType departmentType;
	private String accidentTypeSn;
	private String accidentTypeName;
	private int showSequence;
	private Set<Hazard> hazards=new HashSet<Hazard>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn")
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="accident_type_sn",length=50,unique=true,nullable=false)
	public String getAccidentTypeSn() {
		return accidentTypeSn;
	}
	public void setAccidentTypeSn(String accidentTypeSn) {
		this.accidentTypeSn = accidentTypeSn;
	}
	@Column(name="accident_type_name",length=20,nullable=false)
	public String getAccidentTypeName() {
		return accidentTypeName;
	}
	public void setAccidentTypeName(String accidentTypeName) {
		this.accidentTypeName = accidentTypeName;
	}
	@Column(name="show_sequence",nullable=false)
	public int getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(int showSequence) {
		this.showSequence = showSequence;
	}
	@OneToMany(targetEntity=Hazard.class,mappedBy="accidentType",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<Hazard> getHazards() {
		return hazards;
	}
	public void setHazards(Set<Hazard> hazards) {
		this.hazards = hazards;
	}
}
