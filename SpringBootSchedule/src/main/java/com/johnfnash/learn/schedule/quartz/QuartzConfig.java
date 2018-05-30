package com.johnfnash.learn.schedule.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ������
@Configuration
public class QuartzConfig {

	// ���ö�ʱ����
	@Bean
	public JobDetail testQuartzDetail() {
		return JobBuilder.newJob(TestQuartz.class).withIdentity("testQuartz")
						.storeDurably().build();
	}
	
	// ���ö�ʱ����Ĵ�������Ҳ����ʲôʱ�򴥷�ִ�ж�ʱ����
	@Bean(name = "jobTrigger")
	public Trigger testQuartzTrigger() {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(10)  // ����ʱ�����ڵ�λ���룩
				.repeatForever();
		return TriggerBuilder.newTrigger().forJob(testQuartzDetail())
					.withIdentity("testQuartz")
					.withSchedule(scheduleBuilder)
					.build();
	}
}
