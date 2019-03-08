package com.sdsu.cs635.assg4.WebPageUpadateNotification;

import io.reactivex.Observer;

public class ObserverFactory {

	public Observer<String> createObserver(String observerType) {
		{
			String notificationType = observerType.split(" ")[1];
			if (notificationType.equalsIgnoreCase("mail")) {
				return new MailObserver();
			} else if (notificationType.equalsIgnoreCase("sms")) {
				return new SMSObserver();
			} else
				return new ConsoleObserver();
		}

	}

}
