package com.adeef.hackernewsapp;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import com.adeef.hackernewsapp.adapter.TopStroyAdapter;
import com.adeef.hackernewsapp.model.HackerNewsItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Efutures on 11/2/2015.
 */
public class TopStoryAdapterTest extends AndroidTestCase {
    private TopStroyAdapter mAdapter;

    private HackerNewsItem mStory1;
    private HackerNewsItem mStory2;

    public TopStoryAdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<HackerNewsItem> data = new ArrayList<HackerNewsItem>();

        mStory1 = new HackerNewsItem();
        mStory1.setBy("fisherjeff");
        mStory1.setId(10482068);
        mStory1.setKids(new ArrayList<Integer>(Arrays.asList(10482166, 10482123, 10482155, 10482434, 10483411, 10482365, 10482591, 10482435)));
        mStory1.setDescendants(13);
        mStory1.setScore(158);
        mStory1.setTitle("title 1");
        mStory1.setTime(1446275351);
        mStory1.setType("story");


        mStory2 = new HackerNewsItem();
        mStory1.setBy("fisherjeff");
        mStory1.setId(10482078);
        mStory1.setKids(new ArrayList<Integer>(Arrays.asList(10482266, 10482123, 10482155, 10482434, 10483411, 10482365, 10482591, 10482435)));
        mStory1.setDescendants(12);
        mStory1.setScore(157);
        mStory1.setTitle("title 2");
        mStory1.setTime(1446295351);
        mStory1.setType("story");
        data.add(mStory1);
        data.add(mStory2);
        mAdapter = new TopStroyAdapter(getContext(), data);
    }


    public void testGetItem() {
        assertEquals("modeless was expected.", mStory1.getBy(),
                ((HackerNewsItem) mAdapter.getItem(0)).getBy());
    }

    public void testGetItemId() {
        assertEquals("Wrong ID.", 0, mAdapter.getItemId(0));
    }

    public void testGetCount() {
        assertEquals("Contacts amount incorrect.", 2, mAdapter.getCount());
    }

    // I have 3 views on my adapter, name, number and photo
    public void testGetView() {
        View view = mAdapter.getView(0, null, null);
        TextView storySubmitter = (TextView) view
                .findViewById(R.id.story_submitter);

        TextView storyTitle = (TextView) view
                .findViewById(R.id.story_title);

        TextView storyPoints = (TextView) view
                .findViewById(R.id.story_points);


        //On this part you will have to test it with your own views/data
        assertNotNull("View is null. ", view);
        assertNotNull("Name TextView is null. ", storySubmitter);
        assertNotNull("content TextView is null. ", storyTitle);
        assertNotNull("time TextView is null. ", storyPoints);


        assertEquals("Names doesn't match.", mStory1.getBy(), storySubmitter.getText());
        assertEquals("Story doesn't match.", mStory1.getTitle(),
                storyTitle.getText());
    }
}
