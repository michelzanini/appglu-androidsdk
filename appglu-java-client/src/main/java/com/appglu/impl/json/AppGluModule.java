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
import com.appglu.VersionedRow;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

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
		context.setMixInAnnotations(VersionedRow.class, VersionedRowMixin.class);
		context.setMixInAnnotations(VersionedTableBody.class, VersionedTableBodyMixin.class);
		context.setMixInAnnotations(VersionedTableChangesBody.class, VersionedTableChangesBodyMixin.class);
		context.setMixInAnnotations(VersionedTableChanges.class, VersionedTableChangesMixin.class);
		context.setMixInAnnotations(VersionedTable.class, VersionedTableMixin.class);
	}

}