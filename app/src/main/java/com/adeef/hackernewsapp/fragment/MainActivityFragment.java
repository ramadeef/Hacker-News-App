package com.adeef.hackernewsapp.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.adeef.hackernewsapp.HackerNewsApplication;
import com.adeef.hackernewsapp.R;
import com.adeef.hackernewsapp.adapter.TopStroyAdapter;
import com.adeef.hackernewsapp.data.HackerDbHelper;
import com.adeef.hackernewsapp.data.StoryContract;
import com.adeef.hackernewsapp.data.StoryContract.TopStoryEntry;
import com.adeef.hackernewsapp.model.HackerNewsItem;
import com.adeef.hackernewsapp.network.HackerNewsManager;
import com.adeef.hackernewsapp.util.Configuration;
import com.adeef.hackernewsapp.util.Prefs;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivityFragment.class.toString();
    private ListView storyListView;
    private ArrayList<HackerNewsItem> items;
    private TopStroyAdapter adapter;
    private PullToRefreshView mPullToRefreshView;
    private OnFragmentInteractionListener mListener;
    private Prefs mPrefs;
    private HackerDbHelper helper;
    private SQLiteDatabase database;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = new Prefs(getActivity());
        items = new ArrayList<HackerNewsItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        storyListView = (ListView)rootView.findViewById(R.id.listView);

        adapter = new TopStroyAdapter(getActivity(),items);
        storyListView.setAdapter(adapter);

        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HackerNewsItem story = adapter.getItem(position);
                onListViewPressed(story);
            }
        });

        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Refresh","Refresh Fired");
                        getTopStories();
                    }
                }, Configuration.REFRESH_DELAY);
            }
        });
        if(HackerNewsApplication.isOnline() && !mPrefs.isDataCached()){
            Log.d("Refresh","Load from Online");
            getTopStories();
        }else{
            Log.d("Refresh","Load from Database");
            populateFromDb();
        }
        return rootView;
    }

    private void populateFromDb() {


            items.clear();
            helper = new HackerDbHelper(getActivity());
            database = helper.getWritableDatabase();
            String[] FROM = {TopStoryEntry.COLUMN_BY, TopStoryEntry.COLUMN_DESCENDANTS, TopStoryEntry.COLUMN_STORY_ID,
                    TopStoryEntry.COLUMN_KIDS, TopStoryEntry.COLUMN_SCORE, TopStoryEntry.COLUMN_TIME ,
                    TopStoryEntry.COLUMN_TITLE, TopStoryEntry.COLUMN_TYPE, TopStoryEntry.COLUMN_STORY_URL };
            Cursor cursor = database.query(TopStoryEntry.TABLE_NAME , FROM, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.d("Author ", cursor.getString(0));
                HackerNewsItem hnItem = new HackerNewsItem();
                hnItem.setBy(cursor.getString(0));
                hnItem.setDescendants(cursor.getInt(1));
                hnItem.setId(cursor.getInt(2));
                hnItem.setKids(StoryContract.getKidsArray(cursor.getString(3)));
                hnItem.setScore(cursor.getInt(4));
                hnItem.setTime(cursor.getInt(5));
                hnItem.setTitle(cursor.getString(6));
                hnItem.setType(cursor.getString(7));
                hnItem.setUrl(cursor.getString(8));
                items.add(hnItem);
                cursor.moveToNext();
            }

            adapter.notifyDataSetChanged();
            // make sure to close the cursor
            cursor.close();
            database.close();
    }

    public void onListViewPressed(HackerNewsItem item) {
        if (mListener != null) {
            mListener.onFragmentInteraction(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

            try {
                mListener = (OnFragmentInteractionListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnFragmentCommentInteractionListener");
            }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getTopStories() {
        mPullToRefreshView.setRefreshing(true);
        adapter.clear();
        HackerNewsManager.getInstance().getTopStories(new Callback<List<Integer>>() {
            @Override
            public void success(List<Integer> result, Response response) {
                Log.d(TAG, "success : result size " + result.size());
                onGetTopStoriesSuccess(result);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "failure : " + error.getLocalizedMessage());
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                mPullToRefreshView.setRefreshing(false);
            }
        });
    }

    private void onGetTopStoriesSuccess(List<Integer> result) {
        // get 20 first
        for (int i = 0; i < Configuration.STORY_COUNT; i++) {
            getHackerNewsItem(result.get(i));
        }
    }

    private void getHackerNewsItem(int id) {
        HackerNewsManager.getInstance().getItem(id, new Callback<HackerNewsItem>() {
            @Override
            public void success(HackerNewsItem hackerNewsItem, Response response) {
                onGetHackerNewsItemSuccess(hackerNewsItem);
                mPullToRefreshView.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                mPullToRefreshView.setRefreshing(false);
            }
        });
    }

    private void onGetHackerNewsItemSuccess(HackerNewsItem item) {
        adapter.add(item);
        adapter.notifyDataSetChanged();
        if(adapter.getCount()==20){
            Log.d("Save fired","Save fired");
            helper = new HackerDbHelper(getActivity());
            database = helper.getWritableDatabase();
            int rows = database.delete(TopStoryEntry.TABLE_NAME, null, null);
            try{
                saveToDb();
            }catch (Exception e){
                Log.d("Refresh",e.toString());
                mPrefs.setDataCached(false);
            }
        }
    }

    private void saveToDb() {
        Iterator<HackerNewsItem> ir = items.iterator();
        while (ir.hasNext()) {
            HackerNewsItem temp = ir.next();
            String title = temp.getTitle();
            String url = temp.getUrl();
            if(title!=null)
                title = DatabaseUtils.sqlEscapeString(temp.getTitle());
            if(url!=null)
                url = DatabaseUtils.sqlEscapeString(temp.getUrl());

            String insertrecord = "INSERT OR IGNORE INTO " + TopStoryEntry.TABLE_NAME + "(" + TopStoryEntry.COLUMN_BY + ", "
                    + TopStoryEntry.COLUMN_DESCENDANTS + ", " + TopStoryEntry.COLUMN_STORY_ID + ", " + TopStoryEntry.COLUMN_KIDS + ", "
                    + TopStoryEntry.COLUMN_SCORE + ", " + TopStoryEntry.COLUMN_TIME + ", " + TopStoryEntry.COLUMN_TITLE + ", "
                    + TopStoryEntry.COLUMN_TYPE + ", "
                    + TopStoryEntry.COLUMN_STORY_URL + ") VALUES ('" + temp.getBy() + "', "
                    + temp.getDescendants() + ", " + temp.getId() + ", '"+ StoryContract.getKidsString(temp.getKids()) + "', "
                    + temp.getScore() + ", " + temp.getTime() + ", "+ title + ", '"
                    + temp.getType() + "', " + url + " )";
            database.execSQL(insertrecord);
        }
        database.close();
        mPrefs.setDataCached(true);
        Log.d("Refresh", "Saved Content");
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(HackerNewsItem item);
    }
}
