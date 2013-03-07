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
package com.appglu;

/**
 * A code identifying the error the server has returned. Use the code to search through AppGlu's documentation to find out more details about these error. 
 * 
 * @since 1.0.0
 */
public enum ErrorCode {
	
	/* This code is not returned by the server - it is used only in the client side */
	CANNOT_PARSE_RESPONSE_BODY                              (null),
	
	GENERIC_SERVER_ERROR				            		("1"),
	NOT_FOUND					                    		("2"),
	TABLE_DOES_NOT_EXISTS						            ("3"),
	BAD_SQL_GRAMMAR								            ("4"),
	PERMISSION_DENIED							            ("5"),
	NO_PRIMARY_KEY								            ("6"),
	COMPOSE_PRIMARY_KEY										("7"),
	CANNOT_PARSE_REQUEST_BODY			            		("8"),
	DUPLICATE_KEY								            ("9"),
	DATA_INTEGRITY_VIOLATION					            ("10"),
	WRONG_CONTENT_TYPE										("11"),
	UNCATEGORIZED_SQL										("12"),
	EMPTY_REQUEST_BODY							            ("13"),
	INCOMPLETE_REQUEST_BODY						            ("14"),
	MISSING_PARAM_OR_TYPE_MISMATCH				            ("15"),
	METHOD_NOT_SUPPORTED									("16"),
	QUERY_NAME_CANNOT_NOT_BE_NULL							("17"),
	QUERY_NAME_CONTAINS_WHITESPACE		            		("18"),
	STATEMENT_CANNOT_BE_EMPTY 			            		("19"),
	UNAUTHORIZED                                    		("20"),
	ANALYTICS_SESSION_NAME_EMPTY                    		("21"),
	ANALYTICS_EVENT_NAME_EMPTY        	            		("22"),
	ANALYTICS_PARAMETER_NAME_EMPTY 		            		("23"),
	INVALID_RELATIONSHIP_CONFIGURATION              		("24"),
	COULD_NOT_READ_UPLOADED_FILE 		            		("25"),
	MAX_UPLOAD_SIZE_EXCEEDED	 		            		("26"),
	RELATIONSHIP_NOT_FOUND	 		               			("27"),
	DEVICE_TOKEN_CANNOT_BE_NULL	 		            		("28"),
	DEVICE_PLATFORM_NOT_SUPPORTED	                		("29"),
	PUSH_CAMPAIGN_INVALID_NAME	                    		("30"),
	PUSH_CAMPAIGN_INVALID_SEGMENT							("31"),
	PUSH_CAMPAIGN_INVALID_REPEAT_RATE	            		("32"),
	PUSH_CAMPAIGN_INVALID_REPEAT_TIMES	            		("33"),
	TYPE_CONVERSION_EXCEPTION	                    		("38"),
	COLUMN_DOES_NOT_EXISTS	                        		("39"),
	APP_USER_UNAUTHORIZED	                        		("40"),
	APP_USER_USERNAME_ALREADY_USED							("41"),
	FACEBOOK_TOKEN_CANNOT_BE_EMPTY                  		("42"),
	FACEBOOK_API_ERROR										("43"),
	FACEBOOK_INVALID_AUTHORIZATION							("44"),
	FACEBOOK_USER_ALREADY_USED								("45"),
	FACEBOOK_EXPIRED_AUTHORIZATION							("46"),
	FACEBOOK_REVOKED_AUTHORIZATION							("47"), 
	FACEBOOK_INVALID_TOKEN									("48"), 
	DASHBOARD_INVALID_PASSWORD								("49"),
	TABLE_DOES_NOT_ALLOW_MODIFICATION						("50"),
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
    DATABASE_MIGRATION_DIFFERENT_SCHEMA_ERROR				("68"),
    DATABASE_MIGRATION_DATABASE_DOESNT_EXIST_ERROR			("69"),
    DATABASE_MIGRATION_NO_PRIMARY_KEY_ERROR					("70"),
    DATABASE_MIGRATION_COMPOUND_PRIMARY_KEY_ERROR			("71"),
    DATABASE_MIGRATION_UNKNOWN_ERROR 						("72"),
    DATABASE_MIGRATION_DATABASES_DIFFERENT_SERVER_INSTANCE	("73"),
    DATABASE_MIGRATION_DUPLICATE_KEY_ERROR					("74"),
    DATABASE_MIGRATION_ENVIRONMENT_DOESNT_EXIST				("75"),
	PUSH_CAMPAIGN_CANNOT_BE_UPDATED							("76"),	
	DATABASE_MIGRATION_SAME_DATABASE_SCHEMA					("77"), 
	SEGMENT_WITHOUT_TOKEN_RESULT							("78"), 
	QUERY_STATEMENT_CANNOT_BE_NULL							("79"),
	FIELD_CANNOT_BE_BLANK									("80"), 
	EMAIL_ALREADY_EXISTS									("81"),
	QUERY_NAME_ALREADY_USED									("82"),
	STORAGE_MIGRATION_ERROR_MOVING_FILES					("83"),
	STORAGE_MIGRATION_ERROR_REVERTING_FILES					("84"),
	STORAGE_MIGRATION_ERROR_CREATING_BUCKET					("85"),
	STORAGE_MIGRATION_ERROR_DELETING_BUCKET					("86"),
	MIGRATION_ALREADY_IN_PROGRESS							("87"),
	MIGRATION_INCONSISTENCY_ERROR							("88"),
	APP_IDENTIFIER_CANNOT_BE_NULL                           ("89"),
	INCOMPATIBLE_CLIENT_VERSION								("90"),
	DATABASE_MIGRATION_MISSING_TABLE_IN_DATABASE			("91"),
	DATABASE_MIGRATION_COLUMNS_MISMATCH_FOR_TABLE			("92"),
	STORAGE_MIGRATION_STORAGE_BUCKET_DOESNT_EXIST			("93"),
	STORAGE_MIGRATION_BACKUP_BUCKET_NOT_CONFIGURED			("94"),
	STORAGE_FILE_NAME_CONFLICTS_WITH_DIRECTORY				("95"), 
	APP_NAME_ALREADY_EXIST									("96"),
	APP_DATABASE_SCHEMA_CREATION_ERROR						("97"),
	APP_DATABASE_USER_CREATION_ERROR 						("98"),
	APP_DATABASE_USER_GRANTED_PERMISSION_ERROR				("99"),
	APP_DATABASE_APPLYING_MIGRATION_ERROR					("100"),
	DATABASE_CHANGE_ERROR									("101"), 
	TABLE_NAME_NOT_ALLOWED									("102"), 
	CLIENT_DATABASE_INFO_NOT_FOUND							("103"),
	TABLE_NAME_EXCEEDS_MAX_LENGTH							("104"), 
	COLUMN_TYPE_NOT_FOUND									("105"),
	APP_NAME_NOT_ALLOWED									("106"),
	USER_CANNOT_BE_DELETED									("107"),
	RELATIONSHIP_ALREADY_EXISTS								("108"),
	SCHEMA_ITEM_NAME_NOT_ALLOWED							("109"), 
	RELATIONSHIP_COLUMN_TYPES_MISMATCH						("110"), 
	RELATIONSHIP_CREATE_ERROR								("111"),
	RELATIONSHIP_UPDATE_ERROR								("112"),
	CANNOT_UPDATE_COLUMN_USED_IN_RELATIONSHIP				("113"),
	CANNOT_UPDATE_COLUMN_TRUNCATION_ERROR					("114"),
	CANNOT_DELETE_COLUMN_USED_IN_RELATIONSHIP				("115"), 
	SCHEMA_ITEM_NAME_ALREADY_USED							("116"),
	TABLE_IS_READ_ONLY										("117"), 
	ID_VALUE_EXCEEDS_MAX_LENGTH								("118"), 
	CANNOT_ACTIVATE_SYNC_FOR_TABLE							("119"), 
	STORAGE_CANNOT_UPDATE_ROOT_DIRECTORY					("120")
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
			if (error.code != null && error.code.equalsIgnoreCase(code)) {
				return error;
			}
		}
		return null;
	}

}
