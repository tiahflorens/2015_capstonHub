package work;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FindPasswd {
	private DBAdapter db;
	private String email;

	public FindPasswd(DBAdapter db, String email) {
		this.db = db;
		this.email = email;
	}

	public String createTempPasswd() {
		String password = "";
		for (int i = 0; i < 8; i++) {
			// char upperStr = (char)(Math.random() * 26 + 65);
			char lowerStr = (char) (Math.random() * 26 + 97);
			if (i % 2 == 0) {
				password += (int) (Math.random() * 10);
			} else {
				password += lowerStr;
			}
		}
		return password;
	}

	public String sendEmail() {
		System.out.println("sending email now");

		final String password = "fuckall0rn0ne";
		String tmpPasswd = createTempPasswd();
		// 보내는 서버 주소
		// 메일 제목 설정
		String subject = "임시 비밀번호 입니다";
		//String subject = "회원 가입을 축하합니다!";
		// 받는사람 이메일 주소
		final String from = "somewayur";
		// 보내는사람 이름
		// 받는사람 이메일주소
		try {
			Properties props = new Properties();
			// SSL 사용하는 경우
			props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
			props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
			props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
			props.put("mail.smtp.port", "465"); // SMTP Port

			// 인증
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			};

			// 메일 세션
			Session session = Session.getInstance(props, auth);

			MimeMessage msg = new MimeMessage(session);
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(from, "관리자"));
			msg.setReplyTo(InternetAddress.parse("no_reply@goodcodes.co.kr",
					false));

			msg.setSubject(subject, "UTF-8");
			msg.setContent(getMailString(tmpPasswd) ,"text/HTML; charset=UTF-8");
			//msg.setContent(getTemp() ,"text/HTML; charset=UTF-8");

			
			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email, false));
			Transport.send(msg); // 만들어진 이메일 전송실행

		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.updateUserInfo(tmpPasswd, email);

		return tmpPasswd;
	}

	public String getTemp(){
		StringBuffer sb = new StringBuffer();
		sb.append("회원 가입을 축하합니다 "+email+ " 님!");
		sb.append("다음 <a href=\"http://192.168.0.2:5001/Beautalk_Server/index2.jsp?category=user?division=confirm?data="+email+ "\"> 링크</a> 를 클릭하여 회원가입을 완료해주세요!! ");

			return sb.toString();

	}
	public String getMailString(String passwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("임시 비밀번호는 : " + passwd);
		sb.append(" 로그인 후 꼭 수정해주세요");
		//sb.append("<a href=\"http://192.168.0.2:5001/Beautalk_Server/index2.jsp?category=user?division=confirm?data="+email+ "\"> click here </a>");

		System.out.println("이메알 내용 : " + sb.toString());
		return sb.toString();
	}

}
