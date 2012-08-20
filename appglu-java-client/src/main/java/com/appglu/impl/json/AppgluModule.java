package com.appglu.impl.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;

import com.appglu.ErrorResponse;
import com.appglu.Row;
import com.appglu.Error;
import com.appglu.Rows;

public class AppgluModule extends SimpleModule {

	public AppgluModule() {
		super("AppgluModule", new Version(1, 0, 0, null));
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(Row.class, RowMixin.class);
		context.setMixInAnnotations(Rows.class, RowsMixin.class);
		context.setMixInAnnotations(Error.class, ErrorMixin.class);
		context.setMixInAnnotations(ErrorResponse.class, ErrorResponseMixin.class);
	}

}
