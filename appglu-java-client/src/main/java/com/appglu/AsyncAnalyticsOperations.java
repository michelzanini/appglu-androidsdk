package com.appglu;

import java.util.List;

public interface AsyncAnalyticsOperations {
	
	void createSessionInBackground(AnalyticsSession session, AsyncCallback<Void> callback);
	
	void createSessionsInBackground(List<AnalyticsSession> sessions, AsyncCallback<Void> callback);

}
