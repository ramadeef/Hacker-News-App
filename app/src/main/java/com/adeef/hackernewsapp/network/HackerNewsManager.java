package com.adeef.hackernewsapp.network;

import com.adeef.hackernewsapp.model.HackerNewsItem;
import com.adeef.hackernewsapp.util.Configuration;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Efutures on 11/1/2015.
 */
public class HackerNewsManager {
    private static final HackerNewsManager INSTANCE = new HackerNewsManager();
    private final RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(Configuration.BASE_URL).build();
    private final HackerNewsApi mApi = mRestAdapter.create(HackerNewsApi.class);

    public static final HackerNewsManager getInstance() {
        return INSTANCE;
    }

    public void getTopStories(Callback<List<Integer>> response) {
        if (response != null) {
            mApi.getTopStories(response);
        }
    }

    public void getItem(int id, Callback<HackerNewsItem> response){
        if(response != null){
            mApi.getItem(id, response);
        }
    }
}
