package com.appglu.android.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.FileCopyUtils;

import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.Suppress;

import com.appglu.TableVersion;
import com.appglu.android.sync.SQLiteSyncRepository;
import com.appglu.android.sync.SyncService;
import com.appglu.impl.util.IOUtils;

/**
 * We are using @Suppress here to avoid executing this test all the time.
 * Instead, it should be executed from time to time to verify performance issues, etc...
 */

@Suppress
public class StarTrekAcceptanceTest extends AbstractDatabaseHelperTest {
	
	protected String getDatabaseName() {
		return "star_trek_acceptance_test.sqlite";
	}
	
	protected int getDatabaseVersion() {
		return 1;
	}
	
	protected boolean enableForeignKeys() {
		return true;
	}
	
	protected void setUpScript() {
		SQLiteDatabase database = this.syncDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		
		try {
			database.execSQL("DROP TABLE IF EXISTS messages");
			database.execSQL("DROP TABLE IF EXISTS article_image");
			database.execSQL("DROP TABLE IF EXISTS article");
			database.execSQL("DROP TABLE IF EXISTS article_category");
			
			database.execSQL("CREATE TABLE article_category ( " +
				"id int(11) NOT NULL, " + 
				"name varchar(255) DEFAULT NULL, " +
				"PRIMARY KEY (id))");
			
			database.execSQL("CREATE TABLE article ( " +
				"id int(11) NOT NULL, " +
				"category_id int(11) DEFAULT NULL, " +
				"next_id varchar(255) DEFAULT NULL, " +
				"prev_id varchar(255) DEFAULT NULL, " +
				"body varchar DEFAULT NULL, " +
				"clean_body varchar DEFAULT NULL, " +
				"keywords varchar DEFAULT NULL, " +
				"title varchar(255) DEFAULT NULL, " +
				"PRIMARY KEY (id), " +
				"FOREIGN KEY(category_id) REFERENCES article_category(id) ON DELETE CASCADE)");
			
			database.execSQL("CREATE TABLE article_image (" +
				"id int(11) NOT NULL, " +
				"has_path int(11) DEFAULT NULL, " +
				"'order' int(11) DEFAULT NULL, " +
				"article_id int(11) DEFAULT NULL, " +
				"caption varchar(255) DEFAULT NULL, " +
				"path varchar(255) DEFAULT NULL, " +
				"PRIMARY KEY (id), " +
				"FOREIGN KEY(article_id) REFERENCES article(id) ON DELETE CASCADE)");
			
			database.execSQL("CREATE TABLE messages (" +
				"id int(11) NOT NULL, " +
				"title varchar(50) DEFAULT NULL, " +
				"message varchar(255) DEFAULT NULL, " +
				"image varchar(1024) DEFAULT NULL, " +
				"article_id varchar(20) DEFAULT NULL, " +
				"'order' int(11) DEFAULT NULL, " +
				"PRIMARY KEY (id), " +
				"FOREIGN KEY(article_id) REFERENCES article(id) ON DELETE CASCADE)");
			
			database.execSQL("delete from appglu_table_versions");
			database.execSQL("delete from appglu_sync_metadata");
			database.execSQL("delete from appglu_storage_files");
			
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	public void testStarTrekAcceptanceTest() throws IOException {
		Assert.assertEquals(0, this.countTable("appglu_storage_files"));
		Assert.assertEquals(0, this.countTable("article_category"));
		Assert.assertEquals(0, this.countTable("article"));
		Assert.assertEquals(0, this.countTable("article_image"));
		Assert.assertEquals(0, this.countTable("messages"));
		
		MockSyncOperations syncOperations = new MockSyncOperations("star_trek_full_database", null);
		
		SQLiteSyncRepository syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
		SyncService syncService = new SyncService(syncOperations, syncRepository);
		
		syncService.syncDatabase();
		
		Assert.assertEquals(1, this.countTable("appglu_storage_files"));
		Assert.assertEquals(12, this.countTable("article_category"));
		Assert.assertEquals(6872, this.countTable("article"));
		Assert.assertEquals(7454, this.countTable("article_image"));
		Assert.assertEquals(173, this.countTable("messages"));
		
		Assert.assertEquals(14512, this.countTable("appglu_sync_metadata"));
		Assert.assertEquals(5, this.countTable("appglu_table_versions"));
		
		List<TableVersion> versions = syncRepository.versionsForAllTables();
		
		for (TableVersion tableVersion : versions) {
			if ("appglu_storage_files".equals(tableVersion.getTableName())) {
				Assert.assertEquals(7, tableVersion.getVersion());
			}
			if ("article_category".equals(tableVersion.getTableName())) {
				Assert.assertEquals(116, tableVersion.getVersion());
			}
			if ("article".equals(tableVersion.getTableName())) {
				Assert.assertEquals(20014675, tableVersion.getVersion());
			}
			if ("article_image".equals(tableVersion.getTableName())) {
				Assert.assertEquals(14977, tableVersion.getVersion());
			}
			if ("messages".equals(tableVersion.getTableName())) {
				Assert.assertEquals(521, tableVersion.getVersion());
			}
		}
		
		this.copySqlFileToSdCard();
	}
	
	/*
	 * Copying SQLite file to SDCard to make it easy to take the file out of the device and use it in a personal computer
	 */
	protected void copySqlFileToSdCard() throws IOException {
		File databaseFile = new File("/data/data/com.appglu.android.test/databases/" + this.getDatabaseName());
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
		  inputStream = new FileInputStream(databaseFile);
		  outputStream = new FileOutputStream("/mnt/sdcard/" + this.getDatabaseName());
		  
		  FileCopyUtils.copy(inputStream, outputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

}