package com.appglu.android.analytics;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import com.appglu.AnalyticsSession;

public class AnalyticsServiceTest extends SQLiteTest {
	
	private SQLiteAnalyticsRepository analyticsRepository;
	
	private AnalyticsService analyticsService;
	
	private AnalyticsDispatcher testAnalyticsDispatcher;
	
	private int expectedNumberOfSessionsToBeDispatched;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.testAnalyticsDispatcher = new AnalyticsDispatcher() {
			@Override
			public boolean shouldDispatchSessions(List<AnalyticsSession> sessions) {
				return true;
			}
			
			@Override
			public void dispatchSessions(List<AnalyticsSession> sessions) {
				Assert.assertEquals(expectedNumberOfSessionsToBeDispatched, sessions.size());
			}
		};
		
		this.analyticsRepository = new SQLiteAnalyticsRepository(this.analyticsDatabaseHelper);
		this.analyticsService = new AnalyticsService(this.testAnalyticsDispatcher, this.analyticsRepository, this.deviceInformation);
	}
	
	public void testStartSessionIfNedeed() {
		long sessionId = this.analyticsService.startSessionIfNedeed();
		Assert.assertEquals(1, sessionId);
		
		int sessionsCount = this.countTable("sessions");
		Assert.assertEquals(3, sessionsCount);
		
		this.analyticsRepository.closeSessions(new Date());
		
		long newSessionId = this.analyticsService.startSessionIfNedeed();
		Assert.assertEquals(4, newSessionId);
		
		int newSessionsCount = this.countTable("sessions");
		Assert.assertEquals(4, newSessionsCount);
	}
	
	public void testForceCloseSessions() {
		this.expectedNumberOfSessionsToBeDispatched = 3;
		
		this.analyticsService.forceCloseSessions();
		
		int sessionsCount = this.countTable("sessions");
		Assert.assertEquals(0, sessionsCount);
	}
	
	public void testCloseSessions() {
		this.expectedNumberOfSessionsToBeDispatched = 3;
		
		this.analyticsService.closeSessions(new Date());
		
		int sessionsCount = this.countTable("sessions");
		Assert.assertEquals(0, sessionsCount);
	}
	
	public void testUploadPendingSessions() {
		this.expectedNumberOfSessionsToBeDispatched = 1;
		
		this.analyticsService.dispatchPendingSessions();
		
		int sessionsCount = this.countTable("sessions");
		Assert.assertEquals(2, sessionsCount);
	}

}