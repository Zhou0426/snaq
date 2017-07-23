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
	private int id;//�������Զ�����
	private String serialNumber;//��ˮ�ţ����Ե���RandomUtil.getRandmDigital20()�����ֺͷ��͸�����ƽ̨��serialNumberһ��	
	private String userNumber;//�������ֻ���
	private String messageContent;//��������	
	private int resultCode;//�����룬0��ʾ�ɹ�����0��ʾʧ��
	private java.sql.Timestamp successTimestamp;//�ɹ�����ʱ�䣬��������Ϊ0ʱ��ȡ����ʱ�伴��
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
