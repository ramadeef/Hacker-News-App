package com.adeef.hackernewsapp.network;

import com.adeef.hackernewsapp.model.HackerNewsItem;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Efutures on 11/1/2015.
 */
public interface HackerNewsApi {

    @GET("/topstories.json")
    void getTopStories(Callback<List<Integer>> cb);

    @GET("/item/{id}.json")
    void getItem(@Path("id") int id, Callback<HackerNewsItem> cb);

}

