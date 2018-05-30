package com.johnfnash.learn.schedule.quartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

// ������
public class TestQuartz extends QuartzJobBean {

	// ִ�ж�ʱ����
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("quartz task " + new Date());
	}

}
