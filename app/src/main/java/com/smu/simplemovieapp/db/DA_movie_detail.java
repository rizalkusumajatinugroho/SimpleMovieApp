package com.smu.simplemovieapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.smu.simplemovieapp.model.MovieDetail;
import com.smu.simplemovieapp.model.MovieHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapuser on 12/9/2018.
 */

public class DA_movie_detail extends DB_base {

    public static final String TABLE_NAME = "movie_detail";
    public static final String STATEMENT_CREATE = "CREATE TABLE `" + TABLE_NAME + "` (" +
            "`title` VARCHAR(100) NOT NULL, `year` VARCHAR(20) NOT NULL, `imdbID` VARCHAR(20) NOT NULL, `type` VARCHAR(50) NOT NULL, " +
            "`plot` VARCHAR(200) NOT NULL, `actors` VARCHAR(200) NOT NULL, `writer` VARCHAR(200) NOT NULL,`director` VARCHAR(200) NOT NULL,`genre` VARCHAR(200) NOT NULL,`release` VARCHAR(200) NOT NULL,`rated` VARCHAR(200) NOT NULL," +
            "PRIMARY KEY(`imdbID`));";
    public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS `" + TABLE_NAME + "`;";
    public static final String STATEMENT_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO `" + TABLE_NAME + "` (" +
            "`title`, `year`, `imdbID`, `type`, `plot`, " +
            "`actors`, `writer`, `director`, `genre`, `release`, `rated`" +
            ") VALUES (" +
            "?, ?, ?, ?, ?," +
            "?, ?, ?, ?, ?, ?" +
            ");";

    public DA_movie_detail(Context context) {
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

    public int insert_or_replace(MovieDetail value) {
        int rtn = 0;
        open();
        try {
            __sqliteDatabase.beginTransactionNonExclusive();
            SQLiteStatement sqliteStatement = __sqliteDatabase.compileStatement(STATEMENT_INSERT_OR_REPLACE);


                int i = 1;
                Log.d("debug_rizal", "detail imdbid : "+ value.getImdbID() +" ; title : " + value.getTitle());
                sqliteStatement.bindString(i++, value.getTitle());
                sqliteStatement.bindString(i++, value.getYear());
                sqliteStatement.bindString(i++, value.getImdbID());
                sqliteStatement.bindString(i++, value.getType());
                sqliteStatement.bindString(i++, value.getPlot());

                sqliteStatement.bindString(i++, value.getActors());
                sqliteStatement.bindString(i++, value.getWriter());
                sqliteStatement.bindString(i++, value.getDirector());
                sqliteStatement.bindString(i++, value.getGenreVal());
                sqliteStatement.bindString(i++, value.getReleased());
                sqliteStatement.bindString(i++, value.getRated());

                sqliteStatement.executeInsert();
                sqliteStatement.clearBindings();
                rtn += 1;


            __sqliteDatabase.setTransactionSuccessful();

        } catch(Exception e) {
            Log.d("debug_rizal", "insert detail error : " + e.getMessage());

        } finally {
            __sqliteDatabase.endTransaction();
            close();
        }
        return rtn;
    }

    public MovieDetail getDetail(String id) {
        MovieDetail rtn = new MovieDetail();
        try {
            open();
            String query = "SELECT `title`, `year`, `imdbID`, `type`, `plot`,  " +
                    "`actors`, `writer`, `director`, `genre`, `release`, `rated` " +
                    " FROM " + TABLE_NAME + " c  " +
                    " WHERE c.imdbID = '" + id + "'";


            Log.d("debug_rizal", "query getDetail : " + query);
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            if (pos.moveToNext()) {
                try {
                    int i = 0;


                    rtn.setTitle(pos.getString(i++));
                    rtn.setYear(pos.getString(i++));
                    rtn.setImdbID(pos.getString(i++));
                    rtn.setType(pos.getString(i++));
                    rtn.setPlot(pos.getString(i++));

                    rtn.setActors(pos.getString(i++));
                    rtn.setWriter(pos.getString(i++));
                    rtn.setDirector(pos.getString(i++));
                    rtn.setGenre(pos.getString(i++));
                    rtn.setReleased(pos.getString(i++));
                    rtn.setRated(pos.getString(i++));

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
