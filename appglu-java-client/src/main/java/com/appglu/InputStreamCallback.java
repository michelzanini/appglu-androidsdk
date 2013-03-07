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

import java.io.IOException;
import java.io.InputStream;

/**
 * A callback to have direct access over the <code>InputStream</code>.<br>
 * Usually used when downloading a large file or JSON.
 * 
 * @since 1.0.0
 */
public interface InputStreamCallback {
	
	void doWithInputStream(InputStream inputStream) throws IOException;

}
