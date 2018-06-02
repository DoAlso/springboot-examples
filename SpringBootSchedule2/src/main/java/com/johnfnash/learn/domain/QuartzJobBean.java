package com.johnfnash.learn.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"}) 
public class QuartzJobBean implements Serializable {

	private static final long serialVersionUID = 607415834012939242L;
	
	public static final String STATUS_RUNNING = "1";
	public static final String STATUS_NOT_RUNNING = "0";
	public static final String STATUS_DELETED = "2";
	public static final String CONCURRENT_IS = "1";
	public static final String CONCURRENT_NOT = "0";

	/** ����id */
	@Id
	@GeneratedValue
	private long jobId;

	/** �������� */
	private String jobName;

	/** ������飬��������+������Ӧ����Ψһ�� */
	private String jobGroup;

	/** �����ʼ״̬ 0���� 1���� 2ɾ�� */
	private String jobStatus;

	/** �����Ƿ���״̬�������� */
	private String isConcurrent = "1";

	/** ��������ʱ����ʽ */
	private String cronExpression;

	/** �������� */
	private String description;

	/** �����������spring��ע���bean id�����spingId��Ϊ�գ���springId���� */
	private String springId;

	/** �����������������+������ͨ���෴����� �����spingIdΪ�գ���jobClass���� */
	private String jobClass;

	/** ������õķ����� */
	private String methodName;

	/** ����ʱ�� */
	private Date startTime;

	/** ǰһ������ʱ�� */
	private Date previousTime;

	/** �´�����ʱ�� */
	private Date nextTime;
	
	public QuartzJobBean() {
		super();
	}
	
	public QuartzJobBean(String jobName, String jobGroup) {
		super();
		this.jobName = jobName;
		this.jobGroup = jobGroup;
	}

	public QuartzJobBean(String jobName, String jobGroup, String jobStatus, String isConcurrent,
			String cronExpression, String description, String springId, String jobClass, String methodName) {
		super();
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobStatus = jobStatus;
		this.isConcurrent = isConcurrent;
		this.cronExpression = cronExpression;
		this.description = description;
		this.springId = springId;
		this.jobClass = jobClass;
		this.methodName = methodName;
	}



	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String isConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSpringId() {
		return springId;
	}

	public void setSpringId(String springId) {
		this.springId = springId;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getPreviousTime() {
		return previousTime;
	}

	public void setPreviousTime(Date previousTime) {
		this.previousTime = previousTime;
	}

	public Date getNextTime() {
		return nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(120);
		sb.append("QuartzJobBean [jobId=").append(jobId).append(", jobName=").append(jobName)
		  .append(", jobGroup=").append(jobGroup).append(", jobStatus=").append(jobStatus)
		  .append(", isConcurrent=").append(isConcurrent).append(", cronExpression=").append(cronExpression)
		  .append(", jobClass=").append(jobClass).append(", methodName=").append(methodName).append("]");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + jobName.hashCode();
		hash = 31 * hash + jobGroup.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || (obj.getClass() != this.getClass())) {
			return false;
		}
		QuartzJobBean oBean = (QuartzJobBean) obj;
		if(this.jobName.equals(oBean.jobName) && this.jobGroup.equals(oBean.jobGroup)) {
			return true;
		}
		return false;
	}
	
}
