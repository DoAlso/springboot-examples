package com.johnfnash.learn.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@ServletComponentScan
@Configuration
public class DruidDBConfig {

	private Logger logger = LoggerFactory.getLogger(DruidDBConfig.class);  
	
	@Value("${spring.datasource.primary.url}")  
    private String dbUrl1;  
  
    @Value("${spring.datasource.primary.username}")  
    private String username1;  
  
    @Value("${spring.datasource.primary.password}")  
    private String password1;  
  
    @Value("${spring.datasource.secondary.username}")  
    private String username2;  
  
    @Value("${spring.datasource.secondary.password}")  
    private String password2;  
  
    @Value("${spring.datasource.secondary.url}")  
    private String dbUrl2;  
  
    @Value("com.mysql.jdbc.Driver")  
    private String driverClassName;  
  
    @Value("5")  
    private int initialSize;  
  
    @Value("5")  
    private int minIdle;  
  
    @Value("20")  
    private int maxActive;  
  
    @Value("60000")  
    private int maxWait;  
  
    /** 
     * ���ü����òŽ���һ�μ�⣬�����Ҫ�رյĿ������ӣ���λ�Ǻ��� 
     */  
    @Value("60000")  
    private int timeBetweenEvictionRunsMillis;  
    /** 
     * ����һ�������ڳ�����С�����ʱ�䣬��λ�Ǻ��� 
     */  
    @Value("300000")  
    private int minEvictableIdleTimeMillis;  
  
    @Value("SELECT 1 FROM DUAL")  
    private String validationQuery;  
  
    @Value("true")  
    private boolean testWhileIdle;  
  
    @Value("false")  
    private boolean testOnBorrow;  
  
    @Value("false")  
    private boolean testOnReturn;  
  
    /** 
     * ��PSCache������ָ��ÿ��������PSCache�Ĵ�С 
     */  
    @Value("true")  
    private boolean poolPreparedStatements;  
  
    @Value("20")  
    private int maxPoolPreparedStatementPerConnectionSize;  
    /** 
     * ���ü��ͳ�����ص�filters��ȥ�����ؽ���sql�޷�ͳ�ƣ�'wall'���ڷ���ǽ 
     */  
    @Value("stat,wall,log4j")  
    private String filters;  
    /** 
     * ͨ��connectProperties��������mergeSql���ܣ���SQL��¼ 
     */  
    @Value("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500")  
    private String connectionProperties;  
	
    @Bean(name = "primaryDataSource")  
    @Qualifier("primaryDataSource")  
    public DataSource dataSource() {  
    	return getDruidDataSource(username1, password1, dbUrl1);
    }
    
    @Bean(name = "secondaryDataSource")  
    @Qualifier("secondaryDataSource")  
    public DataSource secondaryDataSource() {  
    	return getDruidDataSource(username2, password2, dbUrl2);
    }
    
    private DruidDataSource getDruidDataSource(String username, String password, String url) {  
        DruidDataSource datasource = new DruidDataSource();  
  
        datasource.setUrl(url);  
        datasource.setUsername(username);  
        datasource.setPassword(password);  
        datasource.setDriverClassName(driverClassName);  
  
        //configuration  
        datasource.setInitialSize(initialSize);  
        datasource.setMinIdle(minIdle);  
        datasource.setMaxActive(maxActive);  
        datasource.setMaxWait(maxWait);  
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
        datasource.setValidationQuery(validationQuery);  
        datasource.setTestWhileIdle(testWhileIdle);  
        datasource.setTestOnBorrow(testOnBorrow);  
        datasource.setTestOnReturn(testOnReturn);  
        datasource.setPoolPreparedStatements(poolPreparedStatements);  
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        
        try {  
            datasource.setFilters(filters);  
        } catch (SQLException e) {  
            logger.error("druid configuration initialization filter : {0}", e);  
        }  
        datasource.setConnectionProperties(connectionProperties);  
  
        return datasource;  
    }  
    
}
