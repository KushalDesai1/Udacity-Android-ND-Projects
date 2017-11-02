package com.example.android.newsworld;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.resource;
import static android.R.attr.thickness;

/**
 * Created by kushaldesai on 28/10/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getName();
    private List<NewsProvider> newsList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txt_newsHeadline, txt_newsContributor, txt_newsSection, txt_newsPublishDate, txt_newsContent;
        public ImageView img_newsThumb;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_newsThumb = (ImageView) itemView.findViewById(R.id.news_Image_Thumb);

            txt_newsHeadline = (TextView) itemView.findViewById(R.id.news_Headline);
            txt_newsContributor = (TextView) itemView.findViewById(R.id.news_Contributor);
            txt_newsSection = (TextView) itemView.findViewById(R.id.news_Section);
            txt_newsPublishDate = (TextView) itemView.findViewById(R.id.news_PublishDate);
            txt_newsContent = (TextView) itemView.findViewById(R.id.news_Content);
        }
    }

    public void addAll(List<NewsProvider> newsProviderList)
    {
        this.newsList.clear();
        this.newsList = newsProviderList;
        this.notifyDataSetChanged();
    }

    public void clearData()
    {
        int size = this.newsList.size();
        this.newsList.clear();
        notifyItemRangeRemoved(0, size);
    }


    public NewsAdapter(List<NewsProvider> newsList) {
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        Log.i(LOG_TAG,"onCreateViewHolder called");
        View itemListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_row, parent, false);
        return new MyViewHolder(itemListView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.i(LOG_TAG,"onBindViewHolder called");
        NewsProvider currentNews = newsList.get(position);

        Picasso.with(context).load(currentNews.getNewsImage()).into(holder.img_newsThumb);

        holder.txt_newsHeadline.setText(currentNews.getNewsHeadline());
        holder.txt_newsContributor.setText(currentNews.getNewsContributor());
        holder.txt_newsSection.setText(currentNews.getNewsSection());
        holder.txt_newsPublishDate.setText(currentNews.getNewsPublishDate());
        holder.txt_newsContent.setText(currentNews.getNewsContent());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
