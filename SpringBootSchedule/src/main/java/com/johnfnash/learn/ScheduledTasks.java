package com.johnfnash.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class ScheduledTasks {

	private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private int fixedDelayCount = 1;
	private int fixedRateCount = 1;
	private int initialDelayCount = 1;
	private int cronCount = 1;
	
	//fixedDelay = 5000��ʾ��ǰ����ִ�����5000ms��Spring scheduling���ٴε��ø÷���
	@Scheduled(fixedDelay = 5000) 
	public void testFixDelay() {
		logger.info("===fixedDelay: ��{}��ִ�з���",fixedDelayCount++);
	}
	
	//fixedRate = 5000��ʾ��ǰ������ʼִ��5000ms��Spring scheduling���ٴε��ø÷���
	@Scheduled(fixedRate = 5000)
	public void testFixedRate() {
		logger.info("===fixedRate: ��{}��ִ�з���", fixedRateCount++);
	}
	
	@Scheduled(initialDelay = 1000, fixedRate = 5000)
	//initialDelay = 1000��ʾ�ӳ�1000msִ�е�һ������
	public void testInitialDelay() {
		 logger.info("===initialDelay: ��{}��ִ�з���", initialDelayCount++);
	}
	
	//cron����cron���ʽ������cron���ʽȷ����ʱ����
	@Scheduled(cron = "0 0/1 * * * ?")
	public void testCron() {
		logger.info("===cron: ��{}��ִ�з���", cronCount++);
	}
	
}
