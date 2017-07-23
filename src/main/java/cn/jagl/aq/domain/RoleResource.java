package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="role_resource")
public class RoleResource implements Serializable {
	private static final long serialVersionUID = 1L;
	private Role role;
	private Resource resource;
	private Set<Department> departments=new HashSet<Department>(0);
	private Set<DepartmentType> departmentTypes=new HashSet<DepartmentType>(0);
	@ManyToMany(targetEntity=Department.class,cascade=CascadeType.ALL)
	@JoinTable(name="role_resource_department",joinColumns={@JoinColumn(name="role_sn",referencedColumnName="role_sn"),@JoinColumn(name="resource_sn",referencedColumnName="resource_sn")},inverseJoinColumns=@JoinColumn(name="department_sn",referencedColumnName="department_sn"))
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	@ManyToMany(targetEntity=DepartmentType.class,cascade=CascadeType.ALL)
	@JoinTable(name="role_resource_department_type",joinColumns={@JoinColumn(name="role_sn",referencedColumnName="role_sn"),@JoinColumn(name="resource_sn",referencedColumnName="resource_sn")},inverseJoinColumns=@JoinColumn(name="department_type_sn",referencedColumnName="department_type_sn"))
	public Set<DepartmentType> getDepartmentTypes() {
		return departmentTypes;
	}
	public void setDepartmentTypes(Set<DepartmentType> departmentTypes) {
		this.departmentTypes = departmentTypes;
	}
	@Id
	@ManyToOne(targetEntity=Role.class,cascade=CascadeType.ALL)
	@JoinColumn(name="role_sn",columnDefinition="VARCHAR(45)",referencedColumnName="role_sn")
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Id
	@ManyToOne(targetEntity=Resource.class,cascade=CascadeType.ALL)
	@JoinColumn(name="resource_sn",columnDefinition="VARCHAR(45)", referencedColumnName="resource_sn")
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
