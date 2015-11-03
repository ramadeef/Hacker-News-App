package com.adeef.hackernewsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adeef.hackernewsapp.R;
import com.adeef.hackernewsapp.model.HackerNewsItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Efutures on 11/1/2015.
 */
public class TopStroyAdapter extends ArrayAdapter<HackerNewsItem> {
    private final Context context;
    private final ArrayList<HackerNewsItem> item;

    public TopStroyAdapter(Context context, ArrayList<HackerNewsItem> item) {
        super(context, android.R.layout.activity_list_item, item);
        this.context = context;
        this.item = item;
    }

    static class ViewHolder {
        public TextView storyPoints;
        public TextView storyTitle;
        public TextView storySubmitter;
        public TextView storyDomain;
        public TextView storyLongAgo;
        public TextView commentCount;
        public TextView jobContent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (null == convertView) {
            // rowView = View.inflate(context,
            // R.layout.put_away_collected_list_item, null);
            rowView = LayoutInflater.from(context).inflate(R.layout.story_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.storyPoints = (TextView) rowView.findViewById(R.id.story_points);
            viewHolder.storyTitle = (TextView) rowView.findViewById(R.id.story_title);
            viewHolder.storySubmitter = (TextView) rowView.findViewById(R.id.story_submitter);
            viewHolder.storyDomain = (TextView) rowView.findViewById(R.id.story_domain);
            viewHolder.storyLongAgo = (TextView) rowView.findViewById(R.id.story_long_ago);
            viewHolder.commentCount = (TextView) rowView.findViewById(R.id.comment_count);
            viewHolder.jobContent = (TextView) rowView.findViewById(R.id.job_content);
            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        HackerNewsItem story = item.get(position);

        holder.storyTitle.setText(String.valueOf(story.getTitle()));
        holder.storySubmitter.setText(String.valueOf(story.getBy()));
        holder.storyPoints.setText(String.valueOf(story.getScore()));
        List kids = story.getKids();
        if (kids != null) {
            int size = kids.size();
            holder.commentCount.setText(size + " comments");
        }

        Date dateTime = new Date(story.getTime() * 1000L);
        CharSequence time = DateUtils.getRelativeDateTimeString(context, dateTime.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0);
        holder.storyLongAgo.setText(time);

        final String domain = story.getUrl();
        if (!TextUtils.isEmpty(domain)) {
            holder.storyDomain.setVisibility(View.VISIBLE);
            SpannableString spanString = new SpannableString(" | " + domain.substring(0, 30 > domain.length() ? domain.length() : 30));
            spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
            holder.storyDomain.setText(spanString);
        } else {
            holder.storyDomain.setVisibility(View.GONE);
        }

        holder.storyDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(domain));
                context.startActivity(i);
            }
        });
        return rowView;
    }
}