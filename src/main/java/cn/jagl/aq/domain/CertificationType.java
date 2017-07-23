package cn.jagl.aq.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="certification_type")
public class CertificationType implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String certificationTypeSn;//证书类型编号	
	private String certificationTypeName;//证书类型名称
	private Set<Certification> certificationes=new HashSet<Certification>(0);
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="certification_type_sn",unique=true)
	public String getCertificationTypeSn() {
		return certificationTypeSn;
	}
	public void setCertificationTypeSn(String certificationTypeSn) {
		this.certificationTypeSn = certificationTypeSn;
	}
	@Column(name="certification_type_name",unique=true)
	public String getCertificationTypeName() {
		return certificationTypeName;
	}
	public void setCertificationTypeName(String certificationTypeName) {
		this.certificationTypeName = certificationTypeName;
	}
	@OneToMany(targetEntity=Certification.class,mappedBy="certificationType")	
	public Set<Certification> getCertificationes() {
		return certificationes;
	}
	public void setCertificationes(Set<Certification> certificationes) {
		this.certificationes = certificationes;
	}
}
