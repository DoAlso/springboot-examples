package com.johnfnash.learn.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	//�Զ���򵥲�ѯ
	User findByUserName(String userName);
	
	User findByUserNameOrEmail(String userName, String email);
	
	Long countByUserName(String userName);
	
	List<User> findByEmailLike(String userName);
	
	List<User> findByUserNameOrderByEmailDesc(String email);
	
	List<User> findByUserNameStartingWith(String email);
	
	// ��ҳ��ѯ�����Ʋ�ѯ
	Page<User> findByUserName(String userName, Pageable pageable);
	
	User findTopByOrderByUserNameDesc();

	List<User> findFirst10ByUserName(String userName, Sort sort);

	// �Զ���SQL��ѯ
	@Modifying
	@Query("update User u set u.userName = ?1 where u.id = ?2")
	int modifyByIdAndUserId(String userName, Long id);
	
	//@Transactional //@Transactional���붨����service��,���ұ����, JPA ǿ��Ҫ��update/delete������
	@Modifying
	@Query("delete from User where id = ?1")
	void deleteByUserId(Long id);
	
}
