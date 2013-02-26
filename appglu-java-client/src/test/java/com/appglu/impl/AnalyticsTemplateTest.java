package com.appglu.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.impl.util.DateUtils;

public class AnalyticsTemplateTest extends AbstractAppGluApiTest {

	private AnalyticsOperations analyticsOperations;
	
	@Before
	public void setup() {
		
		super.setup();
		analyticsOperations = appGluTemplate.analyticsOperations();
	}
	
	private AnalyticsSession session() throws ParseException {
		Date date = DateUtils.parseDate("2010-01-15T12:10:00+0000");
		
		AnalyticsSession session = new AnalyticsSession();
		
		session.setStartDate(date);
		session.setEndDate(date);
		
		Map<String, String> sessionParameters = new HashMap<String, String>();
		
		sessionParameters.put("session1ParameterName", "session1ParameterValue");
		sessionParameters.put("session2ParameterName", "session2ParameterValue");
		
		session.setParameters(sessionParameters);
		
		AnalyticsSessionEvent eventOne = new AnalyticsSessionEvent();
		
		Map<String, String> eventParameters = new HashMap<String, String>();
		
		eventParameters.put("event1ParameterName1", "event1ParameterValue1");
		eventParameters.put("event1ParameterName2", "event1ParameterValue2");
		
		eventOne.setName("sessionEvent1");
		eventOne.setDate(date);
		eventOne.setParameters(eventParameters);
		
		AnalyticsSessionEvent eventTwo = new AnalyticsSessionEvent();
		
		eventTwo.setName("sessionEvent2");
		eventTwo.setDate(date);
		eventTwo.setParameters(eventParameters);
		
		List<AnalyticsSessionEvent> events = new ArrayList<AnalyticsSessionEvent>();
		events.add(eventOne);
		events.add(eventTwo);
		
		session.setEvents(events);
		
		return session;
	}

	@Test
	public void noSessions() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/analytics"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/analytics_no_sessions")))
			.andRespond(withStatus(HttpStatus.CREATED).body("").headers(responseHeaders));
		
		analyticsOperations.uploadSessions(new ArrayList<AnalyticsSession>());
		
		mockServer.verify();
	}
	
	@Test
	public void emptySession() {
		mockServer.expect(requestTo("http://localhost/appglu/v1/analytics"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/analytics_empty_session")))
			.andRespond(withStatus(HttpStatus.CREATED).body("").headers(responseHeaders));
		
		analyticsOperations.uploadSession(new AnalyticsSession());
		
		mockServer.verify();
	}
	
	@Test
	public void uploadSession() throws ParseException {
		mockServer.expect(requestTo("http://localhost/appglu/v1/analytics"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/analytics_session")))
			.andRespond(withStatus(HttpStatus.CREATED).body("").headers(responseHeaders));
		
		analyticsOperations.uploadSession(session());
		
		mockServer.verify();
	}
	
	@Test
	public void uploadSessions() throws ParseException {
		mockServer.expect(requestTo("http://localhost/appglu/v1/analytics"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(header("Content-Type", jsonMediaType.toString()))
			.andExpect(content().string(compactedJson("data/analytics_sessions")))
			.andRespond(withStatus(HttpStatus.CREATED).body("").headers(responseHeaders));
		
		List<AnalyticsSession> sessions = new ArrayList<AnalyticsSession>();
		sessions.add(session());
		sessions.add(session());
		analyticsOperations.uploadSessions(sessions);
		
		mockServer.verify();
	}
	
}
