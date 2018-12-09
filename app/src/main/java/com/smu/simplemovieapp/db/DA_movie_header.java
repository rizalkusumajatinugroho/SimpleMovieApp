package com.smu.simplemovieapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.smu.simplemovieapp.model.MovieHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapuser on 12/8/2018.
 */

public class DA_movie_header extends DB_base {

    public static final String TABLE_NAME = "movie_header";
    public static final String STATEMENT_CREATE = "CREATE TABLE `" + TABLE_NAME + "` (" +
            "`title` VARCHAR(100) NOT NULL, `year` VARCHAR(20) NOT NULL, `imdbID` VARCHAR(20) NOT NULL, `type` VARCHAR(50) NOT NULL, " +
            "`poster` VARCHAR(200) NOT NULL, " +
            "PRIMARY KEY(`imdbID`));";
    public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS `" + TABLE_NAME + "`;";
    public static final String STATEMENT_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO `" + TABLE_NAME + "` (" +
            "`title`, `year`, `imdbID`, `type`, `poster` " +
            ") VALUES (" +
            "?, ?, ?, ?, ?" +
            ");";

    public DA_movie_header(Context context) {
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

    public int insert_or_replace(List<MovieHeader> vectors) {
        int rtn = 0;
        open();
        try {
            __sqliteDatabase.beginTransactionNonExclusive();
            SQLiteStatement sqliteStatement = __sqliteDatabase.compileStatement(STATEMENT_INSERT_OR_REPLACE);

            for(MovieHeader value : vectors) {
                int i = 1;

                Log.d("debug_rizal", "imdbid : "+ value.getImdbID() +" ; title : " + value.getTitle());
                sqliteStatement.bindString(i++, value.getTitle());
                sqliteStatement.bindString(i++, value.getYear());
                sqliteStatement.bindString(i++, value.getImdbID());
                sqliteStatement.bindString(i++, value.getType());
                sqliteStatement.bindString(i++, value.getPoster());

                sqliteStatement.executeInsert();
                sqliteStatement.clearBindings();
                rtn += 1;
            }

            __sqliteDatabase.setTransactionSuccessful();

        } catch(Exception e) {
            Log.d("debug_rizal", "insert header error : " + e.getMessage());

        } finally {
            __sqliteDatabase.endTransaction();
            close();
        }
        return rtn;
    }

    public List<MovieHeader> getListSearch(String search_key, String offset) {
        List<MovieHeader> rtn = new ArrayList<MovieHeader>();
        try {
            open();
            String query = "SELECT `title`, `year`, `imdbID`, `type`, `poster`  " +
                    " FROM " + TABLE_NAME + " c  " +
                    " WHERE (c.title LIKE '%" + search_key + "%' ) ORDER BY year DESC, title ASC" +
                    " LIMIT 10 OFFSET " + offset;


            Log.d("debug_rizal", "query get search : " + query);
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            while (pos.moveToNext()) {
                try {
                    int i = 0;
                    MovieHeader rec = new MovieHeader();

                    rec.setTitle(pos.getString(i++));
                    rec.setYear(pos.getString(i++));
                    rec.setImdbID(pos.getString(i++));
                    rec.setType(pos.getString(i++));
                    rec.setPoster(pos.getString(i++));

                    rtn.add(rec);
                } catch (Exception e) {
                }
            }
            pos.close();

        } catch (Exception e) {

        } finally {
            close();
        }
        return rtn;
    }

    public int countListSearch(String search_key) {
        int rtn = 0;
        try {
            open();
            String query = "SELECT COUNT(*)  " +
                    " FROM " + TABLE_NAME + " c  " +
                    " WHERE (c.title LIKE '%" + search_key + "%' ) ";


            Log.d("debug_rizal", "query count search : " + query);
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            if (pos.moveToNext()) {
                try {
                    int i = 0;
                    rtn = pos.getInt(i++);
                } catch (Exception e) {
                }
            }
            pos.close();

        } catch (Exception e) {

        } finally {
            close();
        }
        return rtn;
    }
}
