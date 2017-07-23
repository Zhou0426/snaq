package cn.jagl.aq.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="near_miss_type")
public class NearMissType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;//��ʶ�У��Զ�����
	private String nearMissTypeSn;//�¼������
	private String nearMissTypeName;//�¼��������
	private Integer showSequence;//��ʾ˳��
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="near_miss_type_sn")
	public String getNearMissTypeSn() {
		return nearMissTypeSn;
	}
	public void setNearMissTypeSn(String nearMissTypeSn) {
		this.nearMissTypeSn = nearMissTypeSn;
	}
	@Column(name="near_miss_type_name")
	public String getNearMissTypeName() {
		return nearMissTypeName;
	}
	public void setNearMissTypeName(String nearMissTypeName) {
		this.nearMissTypeName = nearMissTypeName;
	}
	@Column(name="show_sequence")
	public Integer getShowSequence() {
		return showSequence;
	}
	public void setShowSequence(Integer showSequence) {
		this.showSequence = showSequence;
	}
	
}
