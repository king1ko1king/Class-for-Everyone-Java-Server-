package com.wanli.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * �ʼ����͵Ĺ�����
 * 
 * @author wanli
 *
 */
public class MailUtils {

	/**
	 * �����ʼ��ķ���
	 * 
	 * @param to
	 *            :�ռ���
	 * @param code
	 *            :��֤��
	 */
	public static void sendMail(String to, String code) {
		/**
		 * 1.���һ��Session����. 2.����һ�������ʼ��Ķ���Message. 3.�����ʼ�Transport
		 */
		// 1.������Ӷ���
		// ��¼�ٷ��ʺţ�������ע������䷢����֤��
		final String userName = "hwl123me@163.com";
		final String userPwd = "hlq0830wb";
		//���÷����ʼ�������
		Properties props = new Properties();
		//���÷��ͷ�������ʹ�����׵�163������
		props.setProperty("mail.smtp.host", "smtp.163.com");
		//��֤�ٷ��ʺŵ��û���������
		props.put("mail.smtp.auth", "true"); 
		Session session = Session.getInstance(props, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, userPwd);
			}

		});
		// 2.�����ʼ�����:
		Message message = new MimeMessage(session);
		try {
			// ���÷�����:
			message.setFrom(new InternetAddress("hwl123me@163.com"));
			// �����ռ���:RecipientType.TO,����R:ecipientType.CC ����:RecipientType.BCC
			message.addRecipient(RecipientType.TO, new InternetAddress(to));
			// ���ñ���
			message.setSubject("����ClassForEveryone�ٷ�ע���ʼ�");
			// �����ʼ�����:
			message.setContent(
					"<span><font size='16' face='verdana' color='#00FFFF'>Hello.</font></span>"
							+ "<span><font face='verdana' color='#0000FF'>"+ to +"</font></span>"
							+ "<p>��ע��ClassForEveryone����֤���ǣ�</p>"
							+ "<h1>"+ code +"</h1>",
					"text/html;charset=UTF-8");
			// 3.�����ʼ�:
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}

