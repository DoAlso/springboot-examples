package com.johnfnash.learn.config;

import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactorysecondary", transactionManagerRef = "transactionManagerSecondary", basePackages = {
		"com.johnfnash.learn.dao.secondary" })
public class SecondaryConfig {

	@Resource
	@Qualifier("secondaryDataSource")
	private DataSource secondaryDataSource;

	@Resource
	private JpaProperties jpaProperties;
	
	@Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

	private Map<String, Object> getVendorProperties() {
		return jpaProperties.getHibernateProperties(new HibernateSettings());
	}
	
	// ע��
	@Bean(name = "entityManagerFactoryBuilderSecondary")
	public EntityManagerFactoryBuilder entityManagerFactoryBuilderSecondary() {
		AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		return new EntityManagerFactoryBuilder(adapter, getVendorProperties(), persistenceUnitManager);
	}

    @Bean(name = "entityManagersecondary")  
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {  
		return entityManagerFactorysecondary(builder).getObject().createEntityManager();
	}

	/** 
     * ����ʵ��������λ�� 
     */  
    @Bean(name = "entityManagerFactorysecondary")  
	public LocalContainerEntityManagerFactoryBean entityManagerFactorysecondary(EntityManagerFactoryBuilder builder) { 
		return builder  
                .dataSource(secondaryDataSource)  
                .packages("com.johnfnash.learn.entity.secondary")  
                .persistenceUnit("secondaryPersistenceUnit")  
                .properties(getVendorProperties())  
                .build();  
	}
    
    @Bean(name = "transactionManagerSecondary")  
    public PlatformTransactionManager transactionManagersecondary(EntityManagerFactoryBuilder builder) {
    	return new JpaTransactionManager(entityManagerFactorysecondary(builder).getObject());  
    }

}
