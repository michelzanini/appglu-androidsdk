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
package com.appglu.android.task;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import com.appglu.AsyncCallback;
import com.appglu.android.AppGlu;
import com.appglu.impl.AsyncExecutor;

/**
 * A {@link AsyncExecutor} implementation that uses {@link AppGluAsyncTask} to provide <strong>asynchronous</strong> execution.
 * 
 * @see com.appglu.android.task.AppGluAsyncTask
 * @see com.appglu.impl.AsyncExecutor
 * 
 * @since 1.0.0
 */
public class AsyncTaskExecutor implements AsyncExecutor {
	
	private static int MIN_NUMBER_OF_ACTIVE_TASKS = 3;
	
	private static int DEFAULT_MAX_NUMBER_OF_ACTIVE_TASKS = Integer.MAX_VALUE;
	
	public static final Executor QUEUE_EXECUTOR;
	
	public static final Executor STACK_EXECUTOR;
	
	private Executor defaultExecutor;
	
	private boolean checkForInternetConnection;
	
	static {
		int maxNumberOfActiveTasks = DEFAULT_MAX_NUMBER_OF_ACTIVE_TASKS;
		
		QUEUE_EXECUTOR = createSerialQueueExecutor(maxNumberOfActiveTasks);
		STACK_EXECUTOR = createSerialStackExecutor(maxNumberOfActiveTasks);
	}
	
	public AsyncTaskExecutor() {
		this(QUEUE_EXECUTOR, true);
	}
	
	public AsyncTaskExecutor(boolean checkForInternetConnection) {
		this(QUEUE_EXECUTOR, checkForInternetConnection);
	}
	
	public AsyncTaskExecutor(Executor defaultExecutor, boolean checkForInternetConnection) {
		this.defaultExecutor = defaultExecutor;
		this.checkForInternetConnection = checkForInternetConnection;
	}

	@Override
	public <Result> void execute(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		if (this.checkForInternetConnection && !AppGlu.hasInternetConnection()) {
			asyncCallback.onNoInternetConnection();
		} else {
			this.executeAsyncTask(asyncCallback, workerThreadCallback);
		}
	}

	private <Result> void executeAsyncTask(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			this.executeUsingNativeAsyncTask(asyncCallback, workerThreadCallback);
		} else {
			this.executeUsingAsyncTaskCompat(asyncCallback, workerThreadCallback);
		}
	}

	/**
	 * For Android versions above HONEYCOMB we use the native AsyncTask implementation.
	 */
	private <Result> void executeUsingNativeAsyncTask(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		NativeAsyncTaskExecutor.execute(defaultExecutor, asyncCallback, workerThreadCallback);
	}
	
	/**
	 * For Android versions bellow HONEYCOMB we have a ported AsyncTask implementation (from Ice Cream sandwich).
	 */
	private <Result> void executeUsingAsyncTaskCompat(AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
		AppGluAsyncTaskCompat<Result> asyncTask = new AppGluAsyncTaskCompat<Result>(asyncCallback, workerThreadCallback);
		asyncTask.executeOnExecutor(defaultExecutor);
	}
	
	/**
	 * Creates a serial Executor (executes one task at a time) with a max limit of queued tasks. The tasks will execute in First In First Out (FIFO) order.
	 * @param maxNumberOfActiveTasks max number of active tasks in the queue
	 */
	public static Executor createSerialQueueExecutor(int maxNumberOfActiveTasks) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			return new NativeQueueExecutor(maxNumberOfActiveTasks);
		} else {
			return new CompatQueueExecutor(maxNumberOfActiveTasks);
		}
	}
	
	/**
	 * Creates a serial Executor (executes one task at a time) with a max limit of queued tasks. The tasks will execute in Last In First Out (LIFO) order.
	 * @param maxNumberOfActiveTasks max number of active tasks in the queue
	 */
	public static Executor createSerialStackExecutor(int maxNumberOfActiveTasks) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			return new NativeStackExecutor(maxNumberOfActiveTasks);
		} else {
			return new CompatStackExecutor(maxNumberOfActiveTasks);
		}
	}
	
	/**
	 * This class will help to hide the <code>executeOnExecutor</code> method from being picked by the VM on older versions of Android.<br>
	 * Older versions do not support <code>executeOnExecutor</code>.
	 */
	protected abstract static class NativeAsyncTaskExecutor {
		
		public static <Result> void execute(Executor executor, AsyncCallback<Result> asyncCallback, Callable<Result> workerThreadCallback) {
			AppGluAsyncCallbackTask<Result> asyncTask = new AppGluAsyncCallbackTask<Result>(asyncCallback, workerThreadCallback);
			asyncTask.executeOnExecutor(executor);
		}
		
	}
	
	/**
	 * Base class for Executors. Limits the amount of Runnable's on a Queue or Stack. 
	 */
	protected abstract static class AbstractSerialExecutor implements Executor {
		
        protected Runnable activeTask;
        
        protected int maxNumberOfActiveTasks = MIN_NUMBER_OF_ACTIVE_TASKS;
        
        public AbstractSerialExecutor(int maxNumberOfActiveTasks) {
        	if (maxNumberOfActiveTasks > MIN_NUMBER_OF_ACTIVE_TASKS) {
    			this.maxNumberOfActiveTasks = maxNumberOfActiveTasks;	
        	}
		}

		public synchronized void execute(final Runnable r) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            };
            
			this.addTask(runnable);
			this.removeOldestTaskIfNecessary();
            
            if (activeTask == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
        	activeTask = this.removeTask();
        	
            if (activeTask != null) {
            	Executor threadPoolExecutor = this.getThreadPoolExecutor();
				threadPoolExecutor.execute(activeTask);
            }
        }

        protected abstract void addTask(Runnable runnable);

        protected abstract Runnable removeTask();
        
        protected abstract void removeOldestTaskIfNecessary();
        
        protected abstract Executor getThreadPoolExecutor();
		
    }
	
	/**
	 * Only available for Android HONEYCOMB and up.
	 */
	protected static class NativeQueueExecutor extends AbstractSerialExecutor {
		
		protected ArrayDeque<Runnable> tasks = new ArrayDeque<Runnable>();
		
		public NativeQueueExecutor(int maxNumberOfActiveTasks) {
			super(maxNumberOfActiveTasks);
		}

		protected void addTask(Runnable runnable) {
			tasks.offerLast(runnable);
		}

        protected Runnable removeTask() {
			 return tasks.pollFirst();
		}
        
        protected void removeOldestTaskIfNecessary() {
			if (!tasks.isEmpty() && tasks.size() > maxNumberOfActiveTasks) {
				activeTask = removeTask();
			}
		}
        
        protected Executor getThreadPoolExecutor() {
			return AppGluAsyncCallbackTask.THREAD_POOL_EXECUTOR;
		}
		
	}
	
	/**
	 * Only available for Android HONEYCOMB and up.
	 */
	protected static class NativeStackExecutor extends NativeQueueExecutor {
		
		public NativeStackExecutor(int maxNumberOfActiveTasks) {
			super(maxNumberOfActiveTasks);
		}

		protected void addTask(Runnable runnable) {
			tasks.offerFirst(runnable);
		}
		
		protected void removeOldestTaskIfNecessary() {
			if (!tasks.isEmpty() && tasks.size() > maxNumberOfActiveTasks) {
				activeTask = tasks.pollLast();
			}
		}

	}

	/**
	 * User by Android versions bellow HONEYCOMB. Using java.util.LinkedList because java.util.ArrayDeque it is not available on FROYO.
	 */
	protected static class CompatQueueExecutor extends AbstractSerialExecutor {
		
		private LinkedList<Runnable> tasks = new LinkedList<Runnable>();
		
		public CompatQueueExecutor(int maxNumberOfActiveTasks) {
			super(maxNumberOfActiveTasks);
		}

		protected void addTask(Runnable runnable) {
			tasks.offer(runnable);
		}

        protected Runnable removeTask() {
			 return tasks.poll();
		}
        
        protected void removeOldestTaskIfNecessary() {
			if (!tasks.isEmpty() && tasks.size() > maxNumberOfActiveTasks) {
				activeTask = removeTask();
			}
		}
        
        protected Executor getThreadPoolExecutor() {
			return AppGluAsyncTaskCompat.THREAD_POOL_EXECUTOR;
		}
		
	}
	
	/**
	 * User by Android versions bellow HONEYCOMB. Using java.util.Stack because java.util.ArrayDeque it is not available on FROYO.
	 */
	protected static class CompatStackExecutor extends AbstractSerialExecutor {
		
		private Stack<Runnable> tasks = new Stack<Runnable>();
		
		public CompatStackExecutor(int maxNumberOfActiveTasks) {
			super(maxNumberOfActiveTasks);
		}

		protected void addTask(Runnable runnable) {
			tasks.push(runnable);
		}

        protected Runnable removeTask() {
        	if (tasks.empty()) {
        		return null;
        	}
			return tasks.pop();
		}
        
        protected void removeOldestTaskIfNecessary() {
			if (!tasks.empty() && tasks.size() > maxNumberOfActiveTasks) {
				activeTask = tasks.remove(0);
			}
		}
        
        protected Executor getThreadPoolExecutor() {
			return AppGluAsyncTaskCompat.THREAD_POOL_EXECUTOR;
		}
		
	}

}