package com.johnfnash.learn;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

// ���߳�ִ�ж�ʱ����
//���еĶ�ʱ���񶼷���һ���̳߳��У���ʱ��������ʱʹ�ò�ͬ���̡߳�
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		//�趨һ������5�Ķ�ʱ�����̳߳�
		taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
	}

}
