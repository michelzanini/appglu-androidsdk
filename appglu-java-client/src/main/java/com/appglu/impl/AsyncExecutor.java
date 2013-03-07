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
package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;

/**
 * A strategy to define how <strong>asynchronous</strong> execution is achieved.<br>
 * For example, on pure Java SE platform, a implementation of {@code AsyncExecutor} could use the <code>java.util.concurrent</code> API.<br>
 * If used on a Android platform, for example, the implementation of {@code AsyncExecutor} could use the Android's <code>AsyncTask</code> class.
 * 
 * @see com.appglu.AsyncCallback
 * @since 1.0.0
 */
public interface AsyncExecutor {
	
	<Result> void execute(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback);

}
