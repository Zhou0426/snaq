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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="speciality")
public class Speciality implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	private DepartmentType departmentType;//部门类型
	private String specialitySn;//专业编号
	private String specialityName;//专业名称
	private Boolean deleted;//是否删除
	private Set<StandardIndex> indexes=new HashSet<StandardIndex>(0);//属于该专业的指标集合
	private Set<SpecialCheck> specialChecks=new HashSet<SpecialCheck>(0);
	@ManyToMany(targetEntity=SpecialCheck.class,cascade=CascadeType.ALL)
	@JoinTable(name="special_check_speciality",joinColumns=@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn"),inverseJoinColumns=@JoinColumn(name="special_check_sn",referencedColumnName="special_check_sn"))
	@JSON(serialize=false)
	public Set<SpecialCheck> getSpecialChecks() {
		return specialChecks;
	}
	public void setSpecialChecks(Set<SpecialCheck> specialChecks) {
		this.specialChecks = specialChecks;
	}
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
	@Column(name="speciality_sn",length=45,unique=true,nullable=false)
	public String getSpecialitySn() {
		return specialitySn;
	}
	public void setSpecialitySn(String specialitySn) {
		this.specialitySn = specialitySn;
	}
	@Column(name="speciality_name",length=45,nullable=false)
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	@Column(name="deleted",nullable=false)
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=StandardIndex.class,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="standard_index_speciality",joinColumns=@JoinColumn(name="speciality_sn",referencedColumnName="speciality_sn"),inverseJoinColumns={@JoinColumn(name="index_sn",referencedColumnName="index_sn")})
	@JSON(serialize=false)
	public Set<StandardIndex> getIndexes() {
		return indexes;
	}
	public void setIndexes(Set<StandardIndex> indexes) {
		this.indexes = indexes;
	}
}
