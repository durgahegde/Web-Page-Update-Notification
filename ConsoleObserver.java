package com.sdsu.cs635.assg4.WebPageUpadateNotification;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ConsoleObserver implements Observer<String> {

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
		String url = fileInputLine.split(" ")[0];
		System.out.println("A webpage update notification : URL: " + url + " updated");
	}

	

}
