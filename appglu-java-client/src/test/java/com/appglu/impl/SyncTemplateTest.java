package com.appglu.impl;

import org.junit.Before;

import com.appglu.SyncOperations;

public class SyncTemplateTest extends AbstractAppGluApiTest {
	
	@SuppressWarnings("unused")
	private SyncOperations syncOperations;
	
	@Before
	public void setup() {
		super.setup();
		syncOperations = appGluTemplate.syncOperations();
	}
	
}