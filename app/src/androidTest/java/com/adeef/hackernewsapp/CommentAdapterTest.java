package com.adeef.hackernewsapp;

import android.test.AndroidTestCase;
import android.view.View;

import com.adeef.hackernewsapp.adapter.CommentAdapter;
import com.adeef.hackernewsapp.model.HackerNewsItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Efutures on 11/2/2015.
 */
public class CommentAdapterTest extends AndroidTestCase {
    private CommentAdapter mAdapter;

    private HackerNewsItem mComment1;
    private HackerNewsItem mComment2;

    public CommentAdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        ArrayList<HackerNewsItem> data = new ArrayList<HackerNewsItem>();

        mComment1 = new HackerNewsItem();
        mComment1.setBy("modeless");
        mComment1.setId(10482166);
        mComment1.setKids(new ArrayList<Integer>(Arrays.asList(10482869, 10482185, 10482639, 10482258, 10482245)));
        mComment1.setParent(10482068);
        mComment1.setText("Comment 1");
        mComment1.setTime(1446279306);
        mComment1.setType("comment");
        mComment1.setLevel(0);

        mComment2 = new HackerNewsItem();
        mComment1.setBy("avn2109");
        mComment1.setId(10482869);
        mComment1.setParent(10482166);
        mComment1.setText("Comment 2");
        mComment1.setTime(1446304366);
        mComment1.setType("comment");
        mComment1.setLevel(1);
        data.add(mComment1);
        data.add(mComment2);
        mAdapter = new CommentAdapter(getContext(), data);
    }


    public void testGetItem() {
        assertEquals("modeless was expected.", mComment1.getBy(),
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
//        TextView commentSubmitter = (TextView) view
//                .findViewById(R.id.comment_submitter);
//
//        TextView commentContent = (TextView) view
//                .findViewById(R.id.comment_content);
//
//        TextView commentSubmissionTime = (TextView) view
//                .findViewById(R.id.comment_submission_time);
//
//
//        //On this part you will have to test it with your own views/data
//        assertNotNull("View is null. ", view);
//        assertNotNull("Name TextView is null. ", commentSubmitter);
//        assertNotNull("content TextView is null. ", commentContent);
//        assertNotNull("time TextView is null. ", commentSubmissionTime);
//
//
//        assertEquals("Names doesn't match.", mComment1.getBy(), commentSubmitter.getText());
//        assertEquals("Content doesn't match.", mComment1.getText(),
//                commentContent.getText());
    }
}
