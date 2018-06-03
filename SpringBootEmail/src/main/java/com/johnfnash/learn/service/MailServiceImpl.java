package com.johnfnash.learn.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${mail.fromMail.add}")
    private String from;
    
	@Override
	public void sendSimpleMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		
		try {
			mailSender.send(message);
			logger.info("���ʼ��Ѿ����͡�");
		} catch (Exception e) {
			logger.error("���ͼ��ʼ�ʱ�����쳣��", e);
		}
	}

	@Override
	public void sendHtmlMail(String to, String subject, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			//true��ʾ��Ҫ����һ��multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
	        helper.setText(content, true);
	        
	        mailSender.send(message);
	        logger.info("html�ʼ����ͳɹ�");
		} catch (MessagingException e) {
			logger.error("����html�ʼ�ʱ�����쳣��", e);
		}
	}

	@Override
	public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper;
			
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
	        helper.setText(content, true);
	        
	        FileSystemResource file = new FileSystemResource(new File(filePath));
	        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	        //��Ӷ����������ʹ�ö��� helper.addAttachment(fileName, file)
	        helper.addAttachment(fileName, file);
	        
	        mailSender.send(message);
	        logger.info("���������ʼ��Ѿ����͡�");
		} catch (MessagingException e) {
			logger.error("���ʹ��������ʼ�ʱ�����쳣��", e);
		}
	}

	@Override
	public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(content, true);
	        
	        FileSystemResource res = new FileSystemResource(new File(rscPath));
	        helper.addInline(rscId, res);
	        
	        mailSender.send(message);
	        
	        logger.info("Ƕ�뾲̬��Դ���ʼ��Ѿ����͡�");
		} catch (MessagingException e) {
			logger.error("����Ƕ�뾲̬��Դ���ʼ�ʱ�����쳣��", e);
		}
	}

}
