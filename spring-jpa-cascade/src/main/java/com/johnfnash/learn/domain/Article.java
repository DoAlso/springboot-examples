package com.johnfnash.learn.domain;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Article {

	@Id // ����
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Lob	 // �����ӳ�� MySQL �� Long Text ����
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false)  // ������
	private String content;
	
	//��ѡ����optional=false,��ʾauthor����Ϊ�ա�ɾ�����£���Ӱ���û�
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
	@JoinColumn(name = "author_id") //������article���еĹ����ֶ�(���)
	private Author author;

	public Article() {
		super();
	}
	
	public Article(String title, String content, Author author) {
		super();
		this.title = title;
		this.content = content;
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
}
