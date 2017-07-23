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
@Table(name="supervise_item")
public class SuperviseItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//监管内容编号
	private DepartmentType departmentType;//部门类型
	private String superviseItemSn;//监管项编号
	private String superviseContentName;//监管内容名称
	private Set<SuperviseItem> child=new HashSet<SuperviseItem>(0);//子级监管内容
	private SuperviseItem parent;//父级监管内容
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(targetEntity=DepartmentType.class,cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn",nullable=false)
	@JSON(serialize=false)
	public DepartmentType getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(DepartmentType departmentType) {
		this.departmentType = departmentType;
	}
	@Column(name="supervise_item_sn",nullable=false,unique=true, length=20)
	public String getSuperviseItemSn() {
		return superviseItemSn;
	}
	public void setSuperviseItemSn(String superviseItemSn) {
		this.superviseItemSn = superviseItemSn;
	}
	@Column(name="supervise_item_name",nullable=false,length=20)
	public String getSuperviseContentName() {
		return superviseContentName;
	}
	public void setSuperviseContentName(String superviseContentName) {
		this.superviseContentName = superviseContentName;
	}
	@OneToMany(targetEntity=SuperviseItem.class,mappedBy="parent",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<SuperviseItem> getChild() {
		return child;
	}
	public void setChild(Set<SuperviseItem> child) {
		this.child = child;
	}
	@ManyToOne(targetEntity=SuperviseItem.class,cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="parent_supervise_item_sn",referencedColumnName="supervise_item_sn")
	@JSON(serialize=false)
	public SuperviseItem getParent() {
		return parent;
	}
	public void setParent(SuperviseItem parent) {
		this.parent = parent;
	}	
}
