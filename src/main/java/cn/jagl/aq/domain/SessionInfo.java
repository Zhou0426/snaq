package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="session_info")
public class SessionInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//标识列，自动增长
	private String jsessionId;//sessionId
	private Department department;	
	private Person operator;//操作人
	private java.time.LocalDateTime loginDateTime;//登陆时间
	private java.time.LocalDateTime lastOperationDateTime;//最后一次操作时间	
	private String internalIp;//内网Ip
	private String internetIp;//外网Ip
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="jsession_id",length=32,unique=true)
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	@ManyToOne(targetEntity=Department.class)
	@JoinColumn(name="department_sn",referencedColumnName="department_sn")
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Column(name="login_datetime")
	public java.time.LocalDateTime getLoginDateTime() {
		return loginDateTime;
	}
	public void setLoginDateTime(java.time.LocalDateTime loginDateTime) {
		this.loginDateTime = loginDateTime;
	}
	@Column(name="last_operation_datetime")
	public java.time.LocalDateTime getLastOperationDateTime() {
		return lastOperationDateTime;
	}
	public void setLastOperationDateTime(java.time.LocalDateTime lastOperationDateTime) {
		this.lastOperationDateTime = lastOperationDateTime;
	}
	@ManyToOne(targetEntity=Person.class)
	@JoinColumn(name="operator_id",referencedColumnName="person_id")
	public Person getOperator() {
		return operator;
	}
	public void setOperator(Person operator) {
		this.operator = operator;
	}
	@Column(name="internal_ip")
	public String getInternalIp() {
		return internalIp;
	}
	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}
	@Column(name="internet_ip")
	public String getInternetIp() {
		return internetIp;
	}
	public void setInternetIp(String internetIp) {
		this.internetIp = internetIp;
	}		
}
