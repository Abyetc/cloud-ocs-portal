package com.cloud.ocs.portal.utils.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cloud.ocs.portal.properties.AlarmEmailProperties;

/**
 * 用于发送电子邮件的工具类
 * 
 * @author Wang Chao
 * 
 * @date 2015-4-5 下午10:33:57
 * 
 */
public class MailSender {

	/**
	 * 发送邮件
	 * 
	 * @param toAddress
	 *            收件人地址
	 * @param subject
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 */
	public static void sendMail(String toAddress, String subject, String content) {
		Properties props = new Properties();
		// 开启debug调试
		// props.setProperty("mail.debug", "true");

		// 设置邮件服务器主机名
		props.setProperty("mail.smtp.host", AlarmEmailProperties.getMailSmtpHost());
		// 发送服务器需要身份验证
		props.setProperty("mail.smtp.auth", AlarmEmailProperties.getMailSmtpAuth());
		// 发送邮件协议名称
		props.setProperty("mail.transport.protocol", AlarmEmailProperties.getMailTransportProtocol());

		// 设置环境信息
		Session session = Session.getInstance(props);

		// 创建邮件对象
		Message msg = new MimeMessage(session);
		
		try {
			msg.setSubject(subject);
			// 设置邮件内容
			msg.setText(content);
			// 设置发件人
			msg.setFrom(new InternetAddress(AlarmEmailProperties.getFromAddress()));

			Transport transport = session.getTransport();
			// 连接邮件服务器
			transport.connect(AlarmEmailProperties.getFromUser(), AlarmEmailProperties.getFromPassword());
			// 发送邮件
			transport.sendMessage(msg, new Address[] { new InternetAddress(
					toAddress) });
			// 关闭连接
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
