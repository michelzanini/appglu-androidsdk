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
package com.appglu.impl.json.jackson;

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
import com.appglu.impl.json.AnalyticsSessionsBody;
import com.appglu.impl.json.DeviceBody;
import com.appglu.impl.json.QueryParamsBody;
import com.appglu.impl.json.RowBody;
import com.appglu.impl.json.TableChangesBody;
import com.appglu.impl.json.TableVersionBody;
import com.appglu.impl.json.UserBody;

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
