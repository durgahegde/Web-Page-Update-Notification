package com.sdsu.cs635.assg4.WebPageUpadateNotification;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MailObserver implements Observer<String> {

	public void onComplete() {
		// when the task gets completed print the message 
	}

	public void onError(Throwable error) {
		error.printStackTrace();
	}

	@Override
	public void onSubscribe(Disposable arg0) {
		// TODO Auto-generated method stub
	}

	public void onNext(String fileInputLine) {
		String aURL = fileInputLine.split(" ")[0];
		String reciverAddress = fileInputLine.split(" ")[2];
		String fromAddress = "webpageupdate18@gmail.com";
		String password = "********";
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromAddress, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciverAddress));
			message.setSubject("A webpage update notification");
			message.setText("URL: " + aURL + " updated");

			// send the message
			Transport.send(message);

		} catch (MessagingException exception) {
			exception.printStackTrace();
		}
	}

}
