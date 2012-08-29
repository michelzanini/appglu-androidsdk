package com.appglu.impl.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleAbstractTypeResolver;
import org.codehaus.jackson.map.module.SimpleModule;

import com.appglu.Device;
import com.appglu.Error;
import com.appglu.ErrorResponse;
import com.appglu.QueryResult;
import com.appglu.Row;
import com.appglu.Rows;
import com.appglu.Tuple;

public class AppGluModule extends SimpleModule {

	public AppGluModule() {
		super("AppgluModule", new Version(1, 0, 0, null));
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(RowBody.class, RowBodyMixin.class);
		context.setMixInAnnotations(Rows.class, RowsMixin.class);
		context.setMixInAnnotations(QueryParamsBody.class, QueryParamsBodyMixin.class);
		context.setMixInAnnotations(QueryResult.class, QueryResultMixin.class);
		context.setMixInAnnotations(Device.class, DeviceMixin.class);
		context.setMixInAnnotations(DeviceBody.class, DeviceBodyMixin.class);
		context.setMixInAnnotations(Error.class, ErrorMixin.class);
		context.setMixInAnnotations(ErrorResponse.class, ErrorResponseMixin.class);
		
		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(Tuple.class, Row.class);
		context.addAbstractTypeResolver(resolver);
	}

}
