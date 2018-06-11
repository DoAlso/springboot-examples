package com.johnfnash.learn.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 20, unique = true)
    private String username; // �û��˺ţ��û���¼ʱ��Ψһ��ʶ

	@Column(length = 100)
	private String password; // ��¼ʱ����
	
	@ManyToMany
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id"))
	//1����ϵά���ˣ������Զ��ϵ�İ󶨺ͽ��
    //2��@JoinTableע���name����ָ������������֣�joinColumnsָ����������֣���������ϵά����(User)
    //3��inverseJoinColumnsָ����������֣�Ҫ�����Ĺ�ϵ��ά����(Authority)
    //4����ʵ���Բ�ʹ��@JoinTableע�⣬Ĭ�����ɵĹ���������Ϊ�������+�»���+�ӱ������
    //������Ϊuser_authority
    //������������������������+�»���+�����е���������,��user_id
    //�������ӱ������������������ڹ�����������+�»���+�ӱ����������,��authority_id
    //������ǹ�ϵά���˶�Ӧ�ı��ӱ���ǹ�ϵ��ά���˶�Ӧ�ı�
	private List<Authority> authorityList;

	public User() {
		super();
	}

	public User(String username, String password, List<Authority> authorityList) {
		super();
		this.username = username;
		this.password = password;
		this.authorityList = authorityList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Authority> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List<Authority> authorityList) {
		this.authorityList = authorityList;
	}

	@Override
	public String toString() {
//		return "User [id=" + id + ", username=" + username + ", password=" + password + ", authorityList="
//				+ authorityList + "]";
		return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
	
}
