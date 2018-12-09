package com.smu.simplemovieapp.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class DB_sqlite_helper extends SQLiteOpenHelper {

	public static final String DB_NAME = "imdb.db";
	public static final int    DB_VERSION = 10100;

	DB_sqlite_helper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		upgradeDatabase(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public void createDatabase(SQLiteDatabase db){
		db.execSQL(DA_movie_header.STATEMENT_CREATE);
		db.execSQL(DA_movie_detail.STATEMENT_CREATE);
		db.execSQL(DA_search_history.STATEMENT_CREATE);
	}

	public void upgradeDatabase(SQLiteDatabase db){
		db.execSQL(DA_movie_header.STATEMENT_DROP);
		db.execSQL(DA_movie_detail.STATEMENT_DROP);
		db.execSQL(DA_search_history.STATEMENT_DROP);
		onCreate(db);
	}
}