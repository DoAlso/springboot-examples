package com.johnfnash.learn;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledExecutorService {

	public static void main(String[] args) {
		ScheduledExecutorService service = 
				Executors.newSingleThreadScheduledExecutor();
		// ������1�������� 2���״�ִ�е���ʱʱ��
        //      3������ִ�м�� 4�����ʱ�䵥λ
		service.scheduleAtFixedRate(()->System.out.println("task ScheduledExecutorService "+new Date()), 
						0, 3, TimeUnit.SECONDS);
	}

}
