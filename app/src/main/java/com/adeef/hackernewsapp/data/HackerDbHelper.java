package com.adeef.hackernewsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adeef.hackernewsapp.data.StoryContract.TopStoryEntry;

/**
 * Created by Efutures on 11/2/2015.
 */
public class HackerDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hackernews.db";

    public HackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create
        final String SQL_CREATE_TOP_STORY_TABLE = "CREATE TABLE " + StoryContract.TopStoryEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                TopStoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                TopStoryEntry.COLUMN_BY + " TEXT NOT NULL, " +
                TopStoryEntry.COLUMN_DESCENDANTS + " INTEGER, " +
                TopStoryEntry.COLUMN_STORY_ID + " INTEGER NOT NULL, " +
                TopStoryEntry.COLUMN_KIDS + " TEXT," +

                TopStoryEntry.COLUMN_SCORE + " INTEGER, " +
                TopStoryEntry.COLUMN_TIME + " INTEGER NOT NULL, " +

                TopStoryEntry.COLUMN_TITLE + " TEXT, " +
                TopStoryEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                TopStoryEntry.COLUMN_STORY_URL + " TEXT, " +

                // To assure story entry replace when story id is equal
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + TopStoryEntry.COLUMN_STORY_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TOP_STORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TopStoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
