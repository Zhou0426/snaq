package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sms")
public class Sms implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//主键，自动增长
	private String serialNumber;//流水号，可以调用RandomUtil.getRandmDigital20()，保持和发送给短信平台的serialNumber一致	
	private String userNumber;//接收人手机号
	private String messageContent;//短信内容	
	private int resultCode;//返回码，0表示成功，非0表示失败
	private java.sql.Timestamp successTimestamp;//成功发送时间，当返回码为0时，取本地时间即可
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="serial_number",unique=true)
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	@Column(name="user_number",length=11)
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	@Column(name="message_content",length=300)
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	@Column(name="result_code")
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	@Column(name="success_timestamp")
	public java.sql.Timestamp getSuccessTimestamp() {
		return successTimestamp;
	}
	public void setSuccessTimestamp(java.sql.Timestamp successTimestamp) {
		this.successTimestamp = successTimestamp;
	}
}
