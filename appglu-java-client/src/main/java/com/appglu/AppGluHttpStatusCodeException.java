package com.appglu;

@SuppressWarnings("serial")
public class AppGluHttpStatusCodeException extends AppGluRestClientException {
	
	private final int statusCode;

	private final Error error;
	
	public AppGluHttpStatusCodeException(int statusCode, Error error) {
		super("");
		this.statusCode = statusCode;
		this.error = error;
	}

	public int getStatusCode() {
		return this.statusCode;
	}
	
	public boolean hasStatusCode() {
		return this.statusCode != 0;
	}

	public Error getError() {
		return error;
	}
	
	public boolean hasError() {
		return error != null;
	}
	
	@Override
	public String getMessage() {
		StringBuilder message = new StringBuilder();
		
		if (this.hasStatusCode()) {
			message.append("StatusCode [" + statusCode + "]");
		} else  {
			message.append("StatusCode [null]");
		}
		
		message.append(" ");
		
		if (this.hasError()) {
			message.append(error.toString());
		} else  {
			message.append("Error [null]");
		}
		
		return message.toString();
	}

}
