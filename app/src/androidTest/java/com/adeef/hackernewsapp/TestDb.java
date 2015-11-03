package com.adeef.hackernewsapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.adeef.hackernewsapp.data.HackerDbHelper;
import com.adeef.hackernewsapp.data.StoryContract.TopStoryEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by Efutures on 11/2/2015.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(HackerDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new HackerDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        HackerDbHelper dbHelper = new HackerDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Add top story value
        ContentValues topStoryValues = createTopStoryValues();

        long weatherRowId;
        weatherRowId = db.insert(TopStoryEntry.TABLE_NAME, null, topStoryValues);
        assert (weatherRowId != -1);
        // A cursor is your primary interface to the query results.
        Cursor storyCursor = db.query(
                TopStoryEntry.TABLE_NAME,  // Table to Query
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        if (storyCursor.moveToFirst()) {
            validateCursor(storyCursor, topStoryValues);

        } else {
            fail("No values returned :(");
        }
        dbHelper.close();
    }

    static ContentValues createTopStoryValues() {
        ContentValues storyValues = new ContentValues();

        storyValues.put(TopStoryEntry.COLUMN_BY, "adeef");
        storyValues.put(TopStoryEntry.COLUMN_DESCENDANTS, 13);
        storyValues.put(TopStoryEntry.COLUMN_STORY_ID, 10482068);
        storyValues.put(TopStoryEntry.COLUMN_KIDS, "[ 10482166, 10482123, 10482155, 10482434, 10483411, 10482365, 10482591, 10482435 ]");
        storyValues.put(TopStoryEntry.COLUMN_SCORE, 159);
        storyValues.put(TopStoryEntry.COLUMN_TIME, 1446275351);
        storyValues.put(TopStoryEntry.COLUMN_TITLE, "Protopiper: Physically Sketching Room-Sized Objects at Actual Scale");
        storyValues.put(TopStoryEntry.COLUMN_TYPE, "story");
        storyValues.put(TopStoryEntry.COLUMN_STORY_URL, "http://hpi.de/baudisch/projects/protopiper.html");


        return storyValues;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}
