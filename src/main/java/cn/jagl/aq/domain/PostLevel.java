package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name="post_level")
public class PostLevel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String postLevelSn;
	private String postLevelName;
	private Set<Person> persons=new HashSet<Person>(0);
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="post_level_sn")
	public String getPostLevelSn() {
		return postLevelSn;
	}
	public void setPostLevelSn(String postLevelSn) {
		this.postLevelSn = postLevelSn;
	}
	@Column(name="post_level_name")
	public String getPostLevelName() {
		return postLevelName;
	}
	public void setPostLevelName(String postLevelName) {
		this.postLevelName = postLevelName;
	}
	@OneToMany(targetEntity=Person.class,mappedBy="postLevel",fetch=FetchType.LAZY)
	@JSON(serialize=false)
	public Set<Person> getPersons() {
		return persons;
	}
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
}
