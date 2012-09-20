package com.appglu;

import java.util.List;

public interface AsyncAnalyticsOperations {
	
	void uploadSessionInBackground(AnalyticsSession session, AsyncCallback<Void> callback);
	
	void uploadSessionsInBackground(List<AnalyticsSession> sessions, AsyncCallback<Void> callback);

}
