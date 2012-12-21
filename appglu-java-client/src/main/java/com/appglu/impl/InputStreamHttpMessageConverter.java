package com.appglu.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class InputStreamHttpMessageConverter implements HttpMessageConverter<Object> {
	
	private HttpMessageConverter<Object> delegate;
	
	public InputStreamHttpMessageConverter(HttpMessageConverter<Object> delegate) {
		this.delegate = delegate;
	}

	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return InputStream.class.equals(clazz);
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return delegate.canWrite(clazz, mediaType);
	}

	public List<MediaType> getSupportedMediaTypes() {
		return delegate.getSupportedMediaTypes();
	}

	public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		return inputMessage.getBody();
	}

	public void write(Object object, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		delegate.write(object, contentType, outputMessage);
	}

}