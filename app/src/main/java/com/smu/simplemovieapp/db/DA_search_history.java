package com.smu.simplemovieapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.smu.simplemovieapp.model.MovieHeader;
import com.smu.simplemovieapp.model.SearchHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapuser on 12/9/2018.
 */

public class DA_search_history extends DB_base {

    public static final String TABLE_NAME = "search_history";
    public static final String STATEMENT_CREATE = "CREATE TABLE `" + TABLE_NAME + "` (" +
            "`query` VARCHAR(100) NOT NULL, `page` VARCHAR(20) NOT NULL, `numPage` VARCHAR(20) NOT NULL, `flagDone` VARCHAR(50) NOT NULL, " +

            "PRIMARY KEY(`query`));";
    public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS `" + TABLE_NAME + "`;";
    public static final String STATEMENT_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO `" + TABLE_NAME + "` (" +
            "`query`, `page`, `numPage`, `flagDone` " +
            ") VALUES (" +
            "?, ?, ?, ?" +
            ");";

    public DA_search_history(Context context) {
        super(context);
    }

    public int delete() {
        int rtn = 0;
        open();

        try {
            rtn = __sqliteDatabase.delete(TABLE_NAME, null, null);

        } catch(Exception e) {


        } finally {
            close();
        }
        return rtn;
    }

    public int insert_or_replace(SearchHistory value) {
        int rtn = 0;
        open();
        try {
            __sqliteDatabase.beginTransactionNonExclusive();
            SQLiteStatement sqliteStatement = __sqliteDatabase.compileStatement(STATEMENT_INSERT_OR_REPLACE);


                int i = 1;

                sqliteStatement.bindString(i++, value.getQuery());
                sqliteStatement.bindString(i++, value.getPage());
                sqliteStatement.bindString(i++, value.getNumPage());
                sqliteStatement.bindString(i++, "");

                sqliteStatement.executeInsert();
                sqliteStatement.clearBindings();
                rtn += 1;


            __sqliteDatabase.setTransactionSuccessful();

        } catch(Exception e) {
            Log.d("debug_rizal", "error insert : " + e.getMessage());

        } finally {
            __sqliteDatabase.endTransaction();
            close();
        }
        return rtn;
    }

    public int updateFlagDone(String query) {
        int rtn = 0;
        open();

        try {
            String whereClause = "query = '" + query + "'";
            ContentValues values = new ContentValues();
            values.put("flagDone", "X");
            rtn = __sqliteDatabase.update(TABLE_NAME, values, whereClause, null);

        } catch(Exception e) {

        } finally {
            close();
        }
        return rtn;
    }

    public SearchHistory getValue() {
        SearchHistory rtn = new SearchHistory();
        open();

        try {
            String query = "SELECT `query`, `page`, `numPage`, `flagDone` " +

                    " FROM " + TABLE_NAME + " WHERE 1 AND flagDone <> 'X' LIMIT 1";


            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            if(pos.moveToNext()) {
                try {
                    int i = 0;

                    rtn.setQuery(pos.getString(i++));
                    rtn.setPage(pos.getString(i++));
                    rtn.setNumPage(pos.getString(i++));
                    rtn.setFlagDone(pos.getString(i++));



                } catch(Exception e) {

                }
            }
            pos.close();

        } catch(Exception e) {


        } finally {
            close();
        }
        return rtn;
    }

    public SearchHistory getValueById(String id) {
        SearchHistory rtn = new SearchHistory();
        open();

        try {
            String query = "SELECT `query`, `page`, `numPage`, `flagDone` " +

                    " FROM " + TABLE_NAME + " WHERE 1 AND query = '" + id + "'";


            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            if(pos.moveToNext()) {
                try {
                    int i = 0;

                    rtn.setQuery(pos.getString(i++));
                    rtn.setPage(pos.getString(i++));
                    rtn.setNumPage(pos.getString(i++));
                    rtn.setFlagDone(pos.getString(i++));



                } catch(Exception e) {

                }
            }
            pos.close();

        } catch(Exception e) {


        } finally {
            close();
        }
        return rtn;
    }
}
