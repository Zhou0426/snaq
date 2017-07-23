package cn.jagl.aq.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name="notice")
public class Notice implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;//标题
	private Person author;//作者
	private Timestamp dateTime;//时间
	private String content;//内容
	private Boolean deleted;//是否删除
	private Set<NoticeAttachment> attachments=new HashSet<NoticeAttachment>(0);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="title",nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@ManyToOne(targetEntity=Person.class,cascade=CascadeType.ALL)
	@JoinColumn(name="author",referencedColumnName="person_id",nullable=false)
	public Person getAuthor() {
		return author;
	}
	public void setAuthor(Person author) {
		this.author = author;
	}
	@Column(name="date_time")
	public Timestamp getDateTime() {
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	@Column(name="content", length = 16777216)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="deleted")
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	@OneToMany(targetEntity=NoticeAttachment.class,
				mappedBy="notice",
				fetch=FetchType.LAZY,
				cascade=CascadeType.ALL)
	@JSON(serialize=false)
	public Set<NoticeAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<NoticeAttachment> attachments) {
		this.attachments = attachments;
	}
	
	
	
	

}
