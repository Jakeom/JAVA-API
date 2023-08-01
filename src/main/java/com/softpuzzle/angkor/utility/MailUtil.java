package com.softpuzzle.angkor.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;

	public void joinEmail(HashMap<String, Object> params) {

		String setFrom = "support@angkorchats.com"; // email-config에 설정한 자신의 이메일 주소를 입력
		String toMail = (String) params.get("email");
		String title = "AngkorChat Email Code"; // 이메일 제목
		String content = params.get("certNumber") + " : AngkorChat authentication number";
		mailSend(setFrom, toMail, title, content);
	}

	public void mailSend(String setFrom, String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}