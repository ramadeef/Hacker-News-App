package com.adeef.hackernewsapp.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adeef.hackernewsapp.R;
import com.adeef.hackernewsapp.model.HackerNewsItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Efutures on 11/1/2015.
 */
public class CommentAdapter extends ArrayAdapter<HackerNewsItem> {
    private final Context context;
    private final ArrayList<HackerNewsItem> item;

    public CommentAdapter(Context context, ArrayList<HackerNewsItem> item) {
        super(context, android.R.layout.activity_list_item, item);
        this.context = context;
        this.item = item;
    }

    static class ViewHolder {
        public TextView commentContent;
        public TextView commentSubmissionTime;
        public TextView commentSubmitter;
        public ImageButton overflow;
        public View commentsContainer;
        public View colorCode;
        public TextView hiddenCommentCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (null == convertView) {
            // rowView = View.inflate(context,
            // R.layout.put_away_collected_list_item, null);
            rowView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.commentContent = (TextView) rowView.findViewById(R.id.comment_content);
            viewHolder.commentSubmissionTime = (TextView) rowView.findViewById(R.id.comment_submission_time);
            viewHolder.commentSubmitter = (TextView) rowView.findViewById(R.id.comment_submitter);
            viewHolder.overflow = (ImageButton) rowView.findViewById(R.id.comment_overflow);
            viewHolder.commentsContainer = (View) rowView.findViewById(R.id.comments_container);
            viewHolder.colorCode = (View) rowView.findViewById(R.id.color_code);
            viewHolder.hiddenCommentCount = (TextView) rowView.findViewById(R.id.hidden_comment_count);
            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        HackerNewsItem comment = item.get(position);
        if (comment.getText() != null) {
            Spanned commentContent = Html.fromHtml(comment.getText());
            holder.commentContent.setMovementMethod(LinkMovementMethod.getInstance());
            holder.commentContent.setText(commentContent);
        }


        holder.commentSubmitter.setText(String.valueOf(comment.getBy()));

        List kids = comment.getKids();
        if (kids != null) {
            int size = kids.size();
            if (size == 0) {
                holder.hiddenCommentCount.setVisibility(View.GONE);
            } else {
                holder.hiddenCommentCount.setVisibility(View.VISIBLE);
                holder.hiddenCommentCount.setText("+" + size);
            }
        } else {
            holder.hiddenCommentCount.setVisibility(View.GONE);
        }

        Date dateTime = new Date(comment.getTime() * 1000L);
        CharSequence time = DateUtils.getRelativeDateTimeString(context, dateTime.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0);
        holder.commentSubmissionTime.setText(time);

        int colorCodeLeftMargin = (int) (context.getResources().getDimension(R.dimen.color_code_left_margin) * comment.getLevel());
        int contentLeftMargin = (int) context.getResources().getDimension(R.dimen.comment_content_left_margin);

        if (comment.getLevel() != 0) {
            FrameLayout.LayoutParams commentsContainerLayoutParams = new FrameLayout.LayoutParams(holder.commentsContainer.getLayoutParams());
            commentsContainerLayoutParams.setMargins(contentLeftMargin,
                    commentsContainerLayoutParams.topMargin,
                    commentsContainerLayoutParams.rightMargin,
                    commentsContainerLayoutParams.bottomMargin);
            holder.commentsContainer.setLayoutParams(commentsContainerLayoutParams);

            FrameLayout.LayoutParams colorCodeLayoutParams = new FrameLayout.LayoutParams(holder.colorCode.getLayoutParams());
            colorCodeLayoutParams.setMargins(colorCodeLeftMargin,
                    colorCodeLayoutParams.topMargin,
                    colorCodeLayoutParams.rightMargin,
                    colorCodeLayoutParams.bottomMargin);
            holder.colorCode.setLayoutParams(colorCodeLayoutParams);

        } else {
            FrameLayout.LayoutParams commentsContainerLayoutParams = new FrameLayout.LayoutParams(holder.commentsContainer.getLayoutParams());
            commentsContainerLayoutParams.setMargins(0,
                    commentsContainerLayoutParams.topMargin,
                    commentsContainerLayoutParams.rightMargin,
                    commentsContainerLayoutParams.bottomMargin);
            holder.commentsContainer.setLayoutParams(commentsContainerLayoutParams);

            FrameLayout.LayoutParams colorCodeLayoutParams = new FrameLayout.LayoutParams(holder.colorCode.getLayoutParams());
            colorCodeLayoutParams.setMargins(0,
                    colorCodeLayoutParams.topMargin,
                    colorCodeLayoutParams.rightMargin,
                    colorCodeLayoutParams.bottomMargin);
            holder.colorCode.setLayoutParams(colorCodeLayoutParams);
        }
        switch (comment.getLevel() % 8) {
            case 0:
                holder.colorCode.setBackgroundResource(R.color.holo_blue_bright);
                break;
            case 1:
                holder.colorCode.setBackgroundResource(R.color.holo_green_light);
                break;
            case 2:
                holder.colorCode.setBackgroundResource(R.color.holo_red_light);
                break;
            case 3:
                holder.colorCode.setBackgroundResource(R.color.holo_orange_light);
                break;
            case 4:
                holder.colorCode.setBackgroundResource(R.color.holo_purple);
                break;
            case 5:
                holder.colorCode.setBackgroundResource(R.color.holo_green_dark);
                break;
            case 6:
                holder.colorCode.setBackgroundResource(R.color.holo_red_dark);
                break;
            case 7:
                holder.colorCode.setBackgroundResource(R.color.holo_orange_dark);
                break;
        }

        return rowView;
    }
}
