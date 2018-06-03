package com.johnfnash.learn.service;

public interface MailService {

	/**
	 * ���ͼ��ı��ʼ�
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendSimpleMail(String to, String subject, String content);
	
	/**
	 * ����html��ʽ�ʼ�
	 * @param to
	 * @param subject
	 * @param content
	 */
	public void sendHtmlMail(String to, String subject, String content);
	
	/**
	 * ���ʹ��������ʼ�
	 * @param to
	 * @param subject
	 * @param content
	 * @param filePath
	 */
	public void sendAttachmentsMail(String to, String subject, String content, String filePath);
	
	/**
	 * ���ʹ���̬��Դ���ʼ�
	 * @param to
	 * @param subject
	 * @param content
	 * @param rscPath ��̬��Դ·��
	 * @param rscId  the content ID to use. Can be referenced in HTML source via src="cid:myId" expressions.
	 */
	public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);
	
}
