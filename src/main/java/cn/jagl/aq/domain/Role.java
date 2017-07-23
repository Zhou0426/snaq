package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role implements Serializable{	
	private static final long serialVersionUID = 1L;
	private int id;
	private String roleSn;//��ɫ���
	private String roleName;//��ɫ����
	private RoleType roleType;//��ɫ����
	private Boolean deleted;//�Ƿ�ɾ��
	private Set<Person> persons=new HashSet<Person>(0);//���ڸý�ɫ����Ա����
	private Set<RoleResource> roleResources=new HashSet<RoleResource>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="role_sn",length=45,unique=true,nullable=false)
	public String getRoleSn() {
		return roleSn;
	}
	public void setRoleSn(String roleSn) {
		this.roleSn = roleSn;
	}
	@Column(name="role_name",length=45,nullable=false)
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Enumerated(EnumType.ORDINAL)
	@Column(name="role_type")
	public RoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	@Column(name="deleted",nullable=false)
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@ManyToMany(targetEntity=Person.class)
	@JoinTable(name="person_role",joinColumns=@JoinColumn(name="role_sn",referencedColumnName="role_sn"),inverseJoinColumns=@JoinColumn(name="person_id",referencedColumnName="person_id"))
	public Set<Person> getPersons() {
		return persons;
	}
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	@OneToMany(targetEntity=RoleResource.class,mappedBy="role",cascade=CascadeType.ALL)
	public Set<RoleResource> getRoleResources() {
		return roleResources;
	}
	public void setRoleResources(Set<RoleResource> roleResources) {
		this.roleResources = roleResources;
	}	
}
