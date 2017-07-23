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
@Table(name="accident_level")
public class AccidentLevel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private String accidentLevelSn;//事故等级编号
	private String accidentLevelName;//事故等级名称
	private String color;//显示颜色
	private Set<Accident> accidents=new HashSet<Accident>(0);//对应的事故
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="accident_level_sn",length=50,unique=true,nullable=false)
	public String getAccidentLevelSn() {
		return accidentLevelSn;
	}
	public void setAccidentLevelSn(String accidentLevelSn) {
		this.accidentLevelSn = accidentLevelSn;
	}
	@Column(name="accident_level_name",length=20,nullable=false)
	public String getAccidentLevelName() {
		return accidentLevelName;
	}
	public void setAccidentLevelName(String accidentLevelName) {
		this.accidentLevelName = accidentLevelName;
	}
	@OneToMany(targetEntity=Accident.class,mappedBy="accidentLevel",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<Accident> getAccidents() {
		return accidents;
	}
	public void setAccidents(Set<Accident> accidents) {
		this.accidents = accidents;
	}
	@Column(name="color",length=20)
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
