package com.appglu.impl.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleAbstractTypeResolver;
import org.codehaus.jackson.map.module.SimpleModule;

import com.appglu.Error;
import com.appglu.ErrorResponse;
import com.appglu.QueryResult;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.Tuple;
import com.appglu.impl.RowBody;

public class AppgluModule extends SimpleModule {

	public AppgluModule() {
		super("AppgluModule", new Version(1, 0, 0, null));
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(RowBody.class, RowBodyMixin.class);
		context.setMixInAnnotations(Rows.class, RowsMixin.class);
		context.setMixInAnnotations(QueryResult.class, QueryResultMixin.class);
		context.setMixInAnnotations(Error.class, ErrorMixin.class);
		context.setMixInAnnotations(ErrorResponse.class, ErrorResponseMixin.class);
		
		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(Tuple.class, Row.class);
		context.addAbstractTypeResolver(resolver);
	}

}
