package com.adeef.hackernewsapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adeef.hackernewsapp.R;
import com.adeef.hackernewsapp.adapter.CommentAdapter;
import com.adeef.hackernewsapp.model.HackerNewsItem;
import com.adeef.hackernewsapp.network.HackerNewsManager;
import com.adeef.hackernewsapp.util.Configuration;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentCommentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {
    private static final String TAG = CommentFragment.class.toString();

    private static final String ARG_ID = "id";
    private static final String ARG_TTILE = "title";
    private static final String ARG_KIDS = "kids";
    private static final String ARG_URL = "url";

    private int mId;
    private String title;
    private List<Integer> mComments;
    private String url;

    private OnFragmentCommentInteractionListener mListener;
    private ListView commentListView;
    private ArrayList<HackerNewsItem> items;
    private CommentAdapter adapter;
    private TextView titleTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id
     * @param title
     * @param kids
     * @param url
     * @return A new instance of fragment CommentFragment.
     */
    public static CommentFragment newInstance(int id, String title, ArrayList<Integer> kids, String url) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        Log.d("ARG ID ", String.valueOf(id));
        args.putString(ARG_TTILE, title);
        args.putIntegerArrayList(ARG_KIDS, kids);
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            title = getArguments().getString(ARG_TTILE);
            mComments = getArguments().getIntegerArrayList(ARG_KIDS);
            url = getArguments().getString(ARG_URL);
        }
        items = new ArrayList<HackerNewsItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        setUpView(rootView);
        return rootView;
    }

    private void setUpView(View rootView) {

        commentListView = (ListView) rootView.findViewById(R.id.listView);

        titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        SpannableString spanString = new SpannableString(title);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        titleTextView.setText(spanString);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        adapter = new CommentAdapter(getActivity(), items);
        commentListView.setAdapter(adapter);

        adapter.clear();
        if (mComments.size() > Configuration.COMMENT_COUNT) {
            for (int i = 0; i < Configuration.COMMENT_COUNT; i++) {
                getComment(mComments.get(i), 0);
            }
        } else {
            for (int id : mComments) {
                getComment(id, 0);
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Refresh after 5000ms
                sortAdapter();
            }
        }, Configuration.REFRESH_DELAY_SORT);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mListener = (OnFragmentCommentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentCommentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void getComment(int id, final int level) {
        HackerNewsManager.getInstance().getItem(id, new Callback<HackerNewsItem>() {
            @Override
            public void success(HackerNewsItem hackerNewsItem, Response response) {
                onGetCommentSuccess(hackerNewsItem, level);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onGetCommentSuccess(HackerNewsItem item, int level) {
        item.setLevel(level);
        Log.d("Item id  ->", String.valueOf(item.getId()) + " level" + String.valueOf(level));
        adapter.add(item);
        if (item.getKids() != null) {
            if (item.getKids().size() > 0) {
                level = level + 1;
                for (int id : item.getKids()) {
                    getComment(id, level);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                sortAdapter();
                return true;
            default:
                break;
        }

        return false;
    }

    private void sortAdapter() {
        for (int i = 0; i < items.size() - 1; i++) {

            int childItems = 0;

            for (int j = i + 1; j < items.size(); j++) {

                if (items.get(i).getId().equals(items.get(j).getParent())) {
                    if (childItems == 0) {
                        HackerNewsItem temp = items.get(j);
                        items.remove(j);
                        items.add(i + 1, temp);

                    } else {

                        HackerNewsItem temp = items.get(j);
                        items.remove(j);
                        items.add(i + 1 + childItems, temp);
                    }
                    childItems = childItems + 1;

                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
