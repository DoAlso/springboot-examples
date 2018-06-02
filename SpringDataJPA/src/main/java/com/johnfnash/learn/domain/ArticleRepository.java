package com.johnfnash.learn.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

	//��ȡ�����б���status��id����
	@Query("select a.title as title, a.content as content, c.name as name"
			+ " from Article a left outer join a.articleCategoryList ac left join ac.category c")
	Page<ArticleInfo> findAllByOrderByStatusDescIdDesc(Pageable pageable);
	
}
