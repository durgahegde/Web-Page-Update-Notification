package com.sdsu.cs635.assg4.WebPageUpadateNotification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;

public class ObserverPatternDemo {

	static Map<URL, Long> lastUpdateMap = new HashMap<>();

	public static void main(String[] args) {
		String fileName = "urls.txt";
		File directory = new File(".");
		try {
			File inputFile = new File(directory.getCanonicalPath() + File.separator + fileName);
			FileInputStream aFileInputStream = new FileInputStream(inputFile);
			BufferedReader aBufferedReader = new BufferedReader(new InputStreamReader(aFileInputStream));
			String line;
			while ((line = aBufferedReader.readLine()) != null) {
				ObserverFactory observerFactory = new ObserverFactory();
				subscribe(line, observerFactory);
			}
			aBufferedReader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void subscribe(String line, ObserverFactory factoryObject) {
		try {
			URL url = new URL(line.split(" ")[0]);
			Observer<String> factoryObserver = factoryObject.createObserver(line);
			subscribe(url, factoryObserver, line);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void subscribe(URL url, Observer<String> observer, String line) {
		if (!lastUpdateMap.containsKey(url)) {
			lastUpdateMap.put(url, 0L);
		}
		ObservableOnSubscribe<String> observableOnSubscribe = getObservableOnScuscribe(url, line);
		Observable<String> observable = Observable.create(observableOnSubscribe);
		observable.subscribe(observer);
	}

	private static ObservableOnSubscribe<String> getObservableOnScuscribe(URL url, String line) {
		return emitter -> {
			ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			ScheduledFuture<?> scheduledfuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
				if (isWebPageUpdated(url)) {
					emitter.onNext(line);
				}
			}, 5, 10, TimeUnit.SECONDS);
			emitter.setCancellable(() -> scheduledfuture.cancel(false));
		};
	}

	private static boolean isWebPageUpdated(URL url) {
		try {
			long prevLastmodified = lastUpdateMap.get(url);
			URLConnection urlConnection = url.openConnection();
			long lastModified = urlConnection.getLastModified();
			if (prevLastmodified < lastModified) {
				lastUpdateMap.put(url, lastModified);
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

}
