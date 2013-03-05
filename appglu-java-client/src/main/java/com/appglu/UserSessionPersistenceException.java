package com.appglu;

/**
 * Happens when there is a problem saving or retrieving the {@link User} from a {@link UserSessionPersistence} implementation.
 * 
 * @since 1.0.0
 */

@SuppressWarnings("serial")
public class UserSessionPersistenceException extends AppGluRestClientException {

	public UserSessionPersistenceException(String msg) {
		super(msg);
	}

}