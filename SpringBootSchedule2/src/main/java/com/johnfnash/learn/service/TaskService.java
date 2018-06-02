package com.johnfnash.learn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.johnfnash.learn.dao.QuartzJobRepository;
import com.johnfnash.learn.domain.QuartzJobBean;
import com.johnfnash.learn.jobs.QuartzJobFactory;
import com.johnfnash.learn.jobs.QuartzJobFactoryDisallowConcurrentExecution;
import com.johnfnash.learn.util.TaskUtils;

@Service
public class TaskService {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired     
	private SchedulerFactoryBean schedulerFactoryBean;   
	
	@Autowired
	private QuartzJobRepository repository;
	
	/**    
	 * ��ȡ��������    
	 * @param jobName    
	 * @param jobGroup    
	 * @return    
	 * @throws SchedulerException    
	 */    
	public QuartzJobBean getJob(String jobName,String jobGroup) throws SchedulerException {
		QuartzJobBean job = null;    
		Scheduler scheduler = getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);    
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (null != trigger) {
			job = createJob(jobName, jobGroup, scheduler, trigger);
		}
		
		return job;
	}

	private Scheduler getScheduler() {
		return schedulerFactoryBean.getScheduler();
	}

	private QuartzJobBean createJob(String jobName, String jobGroup, Scheduler scheduler, Trigger trigger)
			throws SchedulerException {
		QuartzJobBean job;
		job = new QuartzJobBean();
		job.setJobName(jobName);    
		job.setJobGroup(jobGroup);    
		job.setDescription("������:" + trigger.getKey()); 
		job.setNextTime(trigger.getNextFireTime());
		job.setPreviousTime(trigger.getPreviousFireTime());
		
		Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
		job.setJobStatus(triggerState.name());
		
		if(trigger instanceof CronTrigger) {
			CronTrigger cronTrigger = (CronTrigger)trigger;
			String cronExpression = cronTrigger.getCronExpression();
			job.setCronExpression(cronExpression);
		}
		return job;
	}
	
	/**    
	 * ��ȡ��������    
	 * @return    
	 * @throws SchedulerException    
	 */    
	public List<QuartzJobBean> getAllJobs() throws SchedulerException{   
		Scheduler scheduler = getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<QuartzJobBean> jobList = new ArrayList<QuartzJobBean>();
		List<? extends Trigger> triggers;
		QuartzJobBean job;
		for (JobKey jobKey : jobKeys) {
			triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				job = createJob(jobKey.getName(), jobKey.getGroup(), scheduler, trigger);
		        jobList.add(job);
			}
		}
		
		return jobList;
	}
	
	/**    
	 * �����������е�job    
	 *     
	 * @return    
	 * @throws SchedulerException    
	 */    
	public List<QuartzJobBean> getRunningJob() throws SchedulerException {
		Scheduler scheduler = getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<QuartzJobBean> jobList = new ArrayList<QuartzJobBean>(executingJobs.size());
		QuartzJobBean job;
		JobDetail jobDetail;
		JobKey jobKey;
		
		for (JobExecutionContext executingJob : executingJobs) {
			jobDetail = executingJob.getJobDetail();
			jobKey = jobDetail.getKey();
			
			job = createJob(jobKey.getName(), jobKey.getGroup(), scheduler, executingJob.getTrigger());
			jobList.add(job);
		}
		
		return jobList;
	}
	
	/**    
	 * �������    
	 *     
	 * @param scheduleJob    
	 * @throws SchedulerException    
	 */    
	public boolean addJob(QuartzJobBean job) throws SchedulerException { 
		if(job == null || !QuartzJobBean.STATUS_RUNNING.equals(job.getJobStatus())) {
			return false;
		}
		
		String jobName = job.getJobName();
		String jobGroup = job.getJobGroup();
		if(!TaskUtils.isValidExpression(job.getCronExpression())) {
			logger.error("ʱ����ʽ����"+jobName+","+jobGroup+"��, "+job.getCronExpression());    
			return false;
		} else {
			Scheduler scheduler = getScheduler();
			// �������ƺ����������ù���    // ���ƣ�task_1 ..    // �� ��group_1 ..
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName,	jobGroup);
			Trigger trigger = scheduler.getTrigger(triggerKey);
			// �����ڣ�����һ��       
			if (null == trigger) { 
				// ���ʽ���ȹ�����
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				// ���µı��ʽ����һ���µ�trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
													 .startAt(job.getStartTime()==null ? (new Date()) : job.getStartTime()) // ����job���������ʱ���������,�͵���trigger��setStartTime����Ч��һ��
													 .withSchedule(scheduleBuilder).build();
				
				//�Ƿ�������ִ��
				JobDetail jobDetail = getJobDetail(job);
				// �� job ��Ϣ�������ݿ�
				job.setStartTime(trigger.getStartTime());
				job.setNextTime(trigger.getNextFireTime());
				job.setPreviousTime(trigger.getPreviousFireTime());
				job = repository.save(job);
				jobDetail.getJobDataMap().put(getJobIdentity(job), job);
				
				scheduler.scheduleJob(jobDetail, trigger);
				
			} else { // trigger�Ѵ��ڣ��������Ӧ�Ķ�ʱ����  
				// ���� job ��Ϣ�����ݿ�
				job.setStartTime(trigger.getStartTime());
				job.setNextTime(trigger.getNextFireTime());
				job.setPreviousTime(trigger.getPreviousFireTime());
				job = repository.save(job);
				getJobDetail(job).getJobDataMap().put(getJobIdentity(job), job);
				
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				// ���µı��ʽ����һ���µ�trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
													 .startAt(job.getStartTime()==null ? (new Date()) : job.getStartTime()) // ����job���������ʱ���������,�͵���trigger��setStartTime����Ч��һ��
													 .withSchedule(scheduleBuilder).build();
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}
		return true;
	}

	private String getJobIdentity(QuartzJobBean job) {
		return "scheduleJob"+(job.getJobGroup() +"_"+job.getJobName());
	}

	private JobDetail getJobDetail(QuartzJobBean job) {
		Class<? extends Job> clazz = QuartzJobBean.CONCURRENT_IS.equals(job.isConcurrent()) 
						? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;		
		JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
		return jobDetail;
	}
	
	/**    
	 * ��ͣ����    
	 * @param job    
	 * @return    
	 */ 
	@Transactional
	public boolean pauseJob(QuartzJobBean job){    
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
		boolean result;
		try {
			scheduler.pauseJob(jobKey);
			
			// ��������״̬�����ݿ�
			job.setJobStatus(QuartzJobBean.STATUS_NOT_RUNNING);
			repository.modifyByStatus(job.getJobStatus(), job.getJobId());
			
			result = true;
		} catch (SchedulerException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	/**    
	 * �ָ�����    
	 * @param job    
	 * @return    
	 */    
	@Transactional
	public boolean resumeJob(QuartzJobBean job){
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
		boolean result;
		try {
			logger.info("resume job : " + (job.getJobGroup() + "_" + job.getJobName()));
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
			// ���ʽ���ȹ�����
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
											.startAt(job.getStartTime()==null ? (new Date()) : job.getStartTime()) // ����job���������ʱ���������,�͵���trigger��setStartTime����Ч��һ��
											.withSchedule(scheduleBuilder).build();
			scheduler.rescheduleJob(triggerKey, trigger);
			scheduler.resumeJob(jobKey);
			
			// ��������״̬�����ݿ�
			job.setJobStatus(QuartzJobBean.STATUS_RUNNING);
			repository.modifyByStatus(job.getJobStatus(), job.getJobId());
			
			result = true;
		} catch (SchedulerException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	/**    
	 * ɾ������    
	 */    
	@Transactional
	public boolean deleteJob(QuartzJobBean job){ 
		Scheduler scheduler = getScheduler();    
		JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());    
		boolean result;
		try{    
			scheduler.deleteJob(jobKey);
			
			// ��������״̬�����ݿ�
			job.setJobStatus(QuartzJobBean.STATUS_DELETED);
			repository.modifyByStatus(job.getJobStatus(), job.getJobId());
						
			result = true;    
		} catch (SchedulerException e) {	
			result = false;
			e.printStackTrace();
		}    
		return result;    
	} 
	
	/**    
	 * ����ִ��һ������    
	 * @param scheduleJob    
	 * @throws SchedulerException    
	 */    
	public void startJob(QuartzJobBean scheduleJob) throws SchedulerException{
		Scheduler scheduler = getScheduler();  
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.triggerJob(jobKey);
	}
	
	/**    
	 * ��������ʱ����ʽ    
	 * @param job    
	 * @throws SchedulerException    
	 */    
	@Transactional
	public void updateCronExpression(QuartzJobBean job) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		//��ȡtrigger������spring�����ļ��ж���� bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		//���ʽ���ȹ�����    
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
		//���µ�cronExpression���ʽ���¹���trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		//���µ�trigger��������jobִ��
		scheduler.rescheduleJob(triggerKey, trigger);
		
		// ���� job ��Ϣ�����ݿ�
		job.setStartTime(trigger.getStartTime());
		job.setNextTime(trigger.getNextFireTime());
		job.setPreviousTime(trigger.getPreviousFireTime());
		job = repository.save(job);
		getJobDetail(job).getJobDataMap().put(getJobIdentity(job), job);
	}
	
	/**
	 * ����job�Ŀ�ʼscheduleʱ��
	 * @param job
	 * @throws SchedulerException
	 */
	@Transactional
	public void updateStartTime(QuartzJobBean job) throws SchedulerException {
		Scheduler scheduler = getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		//��ȡtrigger������spring�����ļ��ж���� bean id="myTrigger"
		CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
		trigger.setStartTime(job.getStartTime());
		//���µ�trigger��������jobִ��
		scheduler.rescheduleJob(triggerKey, trigger);
		
		// ���� job ��Ϣ�����ݿ�
		job.setStartTime(trigger.getStartTime());
		job.setNextTime(trigger.getNextFireTime());
		job.setPreviousTime(trigger.getPreviousFireTime());
		job = repository.save(job);
		getJobDetail(job).getJobDataMap().put(getJobIdentity(job), job);
	}
	
}
