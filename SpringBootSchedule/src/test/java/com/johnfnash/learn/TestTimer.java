package com.johnfnash.learn;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {

	public static void main(String[] args) {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task  run:"+ new Date());
			}
		};
		
		Timer timer = new Timer();
		//����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�������ÿ3��ִ��һ��
		timer.schedule(timerTask, 10, 3000);
	}

}
