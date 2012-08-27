package com.appglu;

public enum ErrorCode {
	
	GENERIC_SERVER_ERROR				                    ("1"),
	NOT_FOUND					                            ("2"),
	TABLE_DOES_NOT_EXISTS				                    ("3"),
	BAD_SQL_GRAMMAR						                    ("4"),
	PERMISSION_DENIED					                    ("5"),
	NO_PRIMARY_KEY						                    ("6"),
	COMPOSE_PRIMARY_KEY					                    ("7"),
	CANNOT_PARSE_REQUEST_BODY			                    ("8"),
	DUPLICATE_KEY						                    ("9"),
	DATA_INTEGRITY_VIOLATION			                    ("10"),
	WRONG_CONTENT_TYPE					                    ("11"),
	UNCATEGORIZED_SQL					                    ("12"),
	EMPTY_REQUEST_BODY					                    ("13"),
	INCOMPLETE_REQUEST_BODY				                    ("14"),
	MISSING_PARAM_OR_TYPE_MISMATCH		                    ("15"),
	METHOD_NOT_SUPPORTED				                    ("16"),
	UNAUTHORIZED                                            ("20"),
	ANALYTICS_SESSION_NAME_EMPTY                            ("21"),
	ANALYTICS_EVENT_NAME_EMPTY        	                    ("22"),
	ANALYTICS_PARAMETER_NAME_EMPTY 		                    ("23"),
	INVALID_RELATIONSHIP_CONFIGURATION                      ("24"),
	COULD_NOT_READ_UPLOADED_FILE 		                    ("25"),
	MAX_UPLOAD_SIZE_EXCEEDED	 		                    ("26"),
	RELATIONSHIP_NOT_FOUND	 		                        ("27"),
	DEVICE_TOKEN_CANNOT_BE_NULL	 		                    ("28"),
	DEVICE_PLATFORM_NOT_SUPPORTED	                        ("29"),
	TYPE_CONVERSION_EXCEPTION	                            ("38"),
	COLUMN_DOES_NOT_EXISTS	                                ("39"),
	APP_USER_UNAUTHORIZED	                                ("40"),
	APP_USER_USERNAME_ALREADY_USED					        ("41"),
	FACEBOOK_TOKEN_CANNOT_BE_EMPTY                          ("42"),
	FACEBOOK_API_ERROR								        ("43"),
	FACEBOOK_INVALID_AUTHORIZATION					        ("44"),
	FACEBOOK_USER_ALREADY_USED						        ("45"),
	FACEBOOK_EXPIRED_AUTHORIZATION					        ("46"),
	FACEBOOK_REVOKED_AUTHORIZATION					        ("47"), 
	FACEBOOK_INVALID_TOKEN							        ("48"), 
	MESSAGING_SERVER_UNAVAILABLE				            ("51"),
	PUSH_NOTIFICATION_INVALID_SEND_TO_ALL_WITH_TARGETS	    ("52"),
	PUSH_NOTIFICATION_INVALID_NO_TARGETS	                ("53"),
	PUSH_NOTIFICATION_INVALID_ALERT_MESSAGE	                ("54"),
	PUSH_NOT_ENABLED	                                    ("55"),
	STORAGE_ENTRY_ALREADY_EXISTS							("56"),
    STORAGE_ENTRY_DOES_NOT_EXISTS							("57"),
    STORAGE_ENTRY_NEW_PATH_INCONSISTENT						("58"),
    STORAGE_CANNOT_DELETE_ROOT_DIRECTORY					("59"),
    STORAGE_CANNOT_MOVE_ROOT_DIRECTORY						("60"),
    STORAGE_DESTINATION_DOES_NOT_EXIST						("61"),
    STORAGE_CANNOT_MOVE_DIRECTORY_INTO_ITSELF_OR_CHILDREN	("62"),
    STORAGE_NAME_CANNOT_CONTAIN_SLASH_CHARS					("63"),
    STORAGE_NAME_CANNOT_CONTAIN_WHITESPACE					("64"),
    STORAGE_NAME_CANNOT_BE_EMPTY							("65"),
    BAD_URL_ENCODE											("66"),
	INVALID_API_URL					                        ("67"),
	;
	
	private String code;
	
	ErrorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static ErrorCode getErrorCode(String code) {
		for (ErrorCode error : values()) {
			if (error.code.equalsIgnoreCase(code)) {
				return error;
			}
		}
		return null;
	}

}
