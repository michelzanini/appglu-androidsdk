/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android.analytics;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.os.Handler;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.AsyncAnalyticsOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * {@code AnalyticsApi} is used to log events to AppGlu allowing it to collect mobile app usage statistics.<br>
 * 
 * @see AppGlu
 * @see AnalyticsOperations
 * @see AsyncAnalyticsOperations
 * @since 1.0.0
 */
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
	
	public AnalyticsApi(AnalyticsDispatcher analyticsDispatcher, AnalyticsRepository analyticsRepository) {
		this.analyticsService = new AnalyticsService(analyticsDispatcher, analyticsRepository);
		this.analyticsApiThread.start();
		
		//on initialization we need to close sessions that may be left open
		this.forceCloseSessions();
	}
	
	/**
	 * Set this callback to receive the {@link com.appglu.AnalyticsSession} objects before they saved or sent to the server.<br>
	 * This will allow you to change them before they are sent to the REST API.
	 */
	public void setSessionCallback(AnalyticsSessionCallback sessionCallback) {
		analyticsService.setSessionCallback(sessionCallback);
	}
	
	/**
	 * Remove the callback to stop receiving its events.
	 */
	public void removeSessionCallback() {
		analyticsService.setSessionCallback(null);
	}

	/**
	 * Should be called from every {@link Activity#onResume()} of your application.<br>
	 * Another alternative is to extend from any AppGlu super class that already does this for you.
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsExpandableListActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsFragmentActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsListActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsPreferenceActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsTabActivity
	 * @param activity the Activity that wants to sent analytics events
	 */
	public void onActivityResume(final Activity activity) {
		if (activity != null && logger.isVerboseEnabled()) {
			logger.verbose("Executing AnalyticsApi.onActivityResume() on %s", activity.getComponentName().getClassName());
		}
		
		handler.removeCallbacks(this.closeSessionsRunnable);
		this.startSessionIfNeeded();
	}

	/**
	 * Should be called from every {@link Activity#onPause()} of your application.
	 * Another alternative is to extend from any AppGlu super class that already does this for you.
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsExpandableListActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsFragmentActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsListActivity
	 * @see com.appglu.android.analytics.activity.AppGluAnalyticsPreferenceActivity
	 * @param activity the Activity that wants to sent analytics events
	 */
	public void onActivityPause(final Activity activity) {
		if (activity != null && logger.isVerboseEnabled()) {
			logger.verbose("Executing AnalyticsApi.onActivityPause() on %s", activity.getComponentName().getClassName());
		}
		
		this.lastOnActivityStopDate = new Date();
		handler.postDelayed(this.closeSessionsRunnable, DEFAULT_CLOSE_SESSIONS_DELAY);
	}

	private void startSessionIfNeeded() {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.startSessionIfNedeed();
			}
		});
	}
	
	private void forceCloseSessions() {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.forceCloseSessions();
			}
		});
	}
	
	private void closeSessions(final Date closeDate) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.closeSessions(closeDate);
			}
		});
	}

	/**
	 * Adds or updates a parameter of {@link AnalyticsSession}.
	 * @see AnalyticsSession
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	public void setSessionParameter(final String name, final String value) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.setSessionParameter(name, value);	
			}
		});
	}
	
	/**
	 * Removes a parameter of {@link AnalyticsSession}.
	 * @see AnalyticsSession
	 * @param name the parameter name
	 */
	public void removeSessionParameter(final String name) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.removeSessionParameter(name);	
			}
		});
	}
	
	/**
	 * Creates a new {@link AnalyticsSessionEvent} event with the current date and no parameters.
	 * @param name the event name
	 */
	public void logEvent(final String name) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.logEvent(name);	
			}
		});
	}
	
	/**
	 * Creates a new {@link AnalyticsSessionEvent} event with the current date.
	 * @param name the event name
	 * @param parameters name and value of all parameters to be added to this event
	 */
	public void logEvent(final String name, final Map<String, String> parameters) {
		this.runnableQueue.add(new Runnable() {
			public void run() {
				analyticsService.logEvent(name, parameters);	
			}
		});
	}
	
	/**
	 * Creates a new {@link AnalyticsSessionEvent} event.
	 * @param event the {@link AnalyticsSessionEvent} object
	 */
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
				try {
					Runnable runnable = runnableQueue.take();
					runnable.run();
				} catch (Throwable e) {
					logger.error(e);
				}
			}
		}
	}

}
