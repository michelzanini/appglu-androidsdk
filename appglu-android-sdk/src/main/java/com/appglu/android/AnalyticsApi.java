package com.appglu.android;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.os.Handler;

import com.appglu.AnalyticsSessionEvent;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsRepository;
import com.appglu.android.analytics.AnalyticsService;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public final class AnalyticsApi {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private static final int DEFAULT_CLOSE_SESSIONS_DELAY = 10 * 1000;
	
	private final AnalyticsService analyticsService;
	
	private final AnalyticsApiThread analyticsApiThread = new AnalyticsApiThread();
	
	private final LinkedBlockingQueue<Runnable> runnableQueue = new LinkedBlockingQueue<Runnable>();
	
	private final Handler handler = new Handler();
	
	private Date lastOnActivityStopDate;
	
	private Runnable closeSessionsRunnable = new Runnable() {
		public void run() {
			closeSessions(lastOnActivityStopDate);
		}
	};
	
	public AnalyticsApi(AnalyticsDispatcher analyticsDispatcher, AnalyticsRepository analyticsRepository, DeviceInformation deviceInformation) {
		this.analyticsService = new AnalyticsService(analyticsDispatcher, analyticsRepository, deviceInformation);
		this.analyticsApiThread.start();
		
		//on initialization we need to close sessions that may be left open
		this.forceCloseSessions();
	}
	
	public void setSessionCallback(AnalyticsSessionCallback sessionCallback) {
		analyticsService.setSessionCallback(sessionCallback);
	}
	
	public void removeSessionCallback() {
		analyticsService.setSessionCallback(null);
	}

	public void onActivityResume(final Activity activity) {
		if (activity != null) {
			logger.debug("Executing AnalyticsApi.onActivityResume() on %s", activity.getComponentName().getClassName());
		}
		
		handler.removeCallbacks(this.closeSessionsRunnable);
		this.startSessionIfNeeded();
	}

	public void onActivityPause(final Activity activity) {
		if (activity != null) {
			logger.debug("Executing AnalyticsApi.onActivityPause() on %s", activity.getComponentName().getClassName());
		}
		
		this.lastOnActivityStopDate = new Date();
		handler.postDelayed(this.closeSessionsRunnable, DEFAULT_CLOSE_SESSIONS_DELAY);
	}

	protected void startSessionIfNeeded() {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.startSessionIfNedeed();
			}
		});
	}
	
	protected void forceCloseSessions() {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.forceCloseSessions();
			}
		});
	}
	
	protected void closeSessions(final Date closeDate) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.closeSessions(closeDate);
			}
		});
	}

	public void setSessionParameter(final String name, final String value) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.setSessionParameter(name, value);	
			}
		});
	}
	
	public void removeSessionParameter(final String name) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.removeSessionParameter(name);	
			}
		});
	}
	
	public void logEvent(final String name) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.logEvent(name);	
			}
		});
	}
	
	public void logEvent(final String name, final Map<String, String> parameters) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.logEvent(name, parameters);	
			}
		});
	}
	
	public void logEvent(final AnalyticsSessionEvent event) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.logEvent(event);	
			}
		});
	}
	
	private class AnalyticsApiThread extends Thread {
		AnalyticsApiThread() {
			super("AppGluAnalyticsThread");
		}

		@Override
		public void run() {
			while (true) {
				Runnable runnable;
				try {
					runnable = runnableQueue.take();
					runnable.run();
				} catch (Throwable e) {
					logger.error(e);
				}
			}
		}
	}

}