package com.appglu;

@SuppressWarnings("serial")
public class RowMapperException extends AppGluRestClientException {
	
	private Class<?> mappedClass;
	
	public RowMapperException(Class<?> mappedClass, String msg) {
		super(msg);
		this.mappedClass = mappedClass;
	}
	
	public RowMapperException(Class<?> mappedClass, Throwable e) {
		super(e);
		this.mappedClass = mappedClass;
	}
	
	public RowMapperException(Class<?> mappedClass, String msg, Exception e) {
		super(msg, e);
		this.mappedClass = mappedClass;
	}

	public Class<?> getMappedClass() {
		return mappedClass;
	}

	@Override
	public String getMessage() {
		if (mappedClass == null) {
			return super.getMessage();
		}
		return mappedClass.getName() + " - " + super.getMessage();
	}
	
}