package com.appglu.impl.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.Device;
import com.appglu.Error;
import com.appglu.ErrorResponse;
import com.appglu.QueryResult;
import com.appglu.Rows;
import com.appglu.User;
import com.appglu.RowChanges;
import com.appglu.TableVersion;
import com.appglu.TableChanges;

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
		context.setMixInAnnotations(AnalyticsSession.class, AnalyticsSessionMixin.class);
		context.setMixInAnnotations(AnalyticsSessionEvent.class, AnalyticsSessionEventMixin.class);
		context.setMixInAnnotations(AnalyticsSessionsBody.class, AnalyticsSessionsBodyMixin.class);
		context.setMixInAnnotations(Error.class, ErrorMixin.class);
		context.setMixInAnnotations(ErrorResponse.class, ErrorResponseMixin.class);
		context.setMixInAnnotations(User.class, UserMixin.class);
		context.setMixInAnnotations(UserBody.class, UserBodyMixin.class);
		context.setMixInAnnotations(RowChanges.class, RowChangesMixin.class);
		context.setMixInAnnotations(TableVersionBody.class, TableVersionBodyMixin.class);
		context.setMixInAnnotations(TableChangesBody.class, TableChangesBodyMixin.class);
		context.setMixInAnnotations(TableChanges.class, TableChangesMixin.class);
		context.setMixInAnnotations(TableVersion.class, TableVersionMixin.class);
	}

}