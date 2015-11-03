package com.adeef.hackernewsapp.data;

import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Efutures on 11/2/2015.
 */
public class StoryContract {

    public static String getKidsString(ArrayList<Integer> kids){
        Gson gson = new Gson();
        String numbersJson = gson.toJson(kids);
        return numbersJson;
    }

    public static ArrayList<Integer> getKidsArray(String kids){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> numbersArray = gson.fromJson(kids, type);
        return numbersArray;
    }

    public static final class TopStoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "TopStory";

        // Column with the author of the top story
        public static final String COLUMN_BY = "author";

        // Date, stored as integer
        public static final String COLUMN_DESCENDANTS = "descendants";

        // Story id by get from hacker news
        public static final String COLUMN_STORY_ID = "story_id";

        // kids will be stores as json string.
        public static final String COLUMN_KIDS = "kids";

        // Point for the story
        public static final String COLUMN_SCORE = "score";

        // time will be stored as long sqlite can handle 8bit long in integer
        public static final String COLUMN_TIME = "time";

        // Story title
        public static final String COLUMN_TITLE = "title";

        // type
        public static final String COLUMN_TYPE = "type";

        // url store as string
        public static final String COLUMN_STORY_URL = "url";

    }
}
