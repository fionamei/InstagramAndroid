package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.EndlessRecyclerViewScrollListener;
import com.example.instagram.models.Post;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedFragment extends Fragment {

    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    public static final String TAG = "FeedActivity";
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Date lastPostDate;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        queryPosts();
        swipeListener();
        scrollListener(linearLayoutManager);
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

    }



    private void queryPosts() {
        int POST_LIMIT = 5;
        // specify type of data - post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit to only 20 items
        query.setLimit(POST_LIMIT);
        // order by creation date, newest first
        query.addDescendingOrder("createdAt");
        // start async call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                lastPostDate= allPosts.get(POST_LIMIT-1).getCreatedAt();
            }
        });
    }

    private void swipeListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeedAsync();
            }
        });
    }

    private void fetchFeedAsync() {
        queryPosts();
        adapter.clear();
        adapter.addAll(allPosts);
        swipeContainer.setRefreshing(false);
    }

    private void scrollListener(LinearLayoutManager linearLayoutManager) {
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(lastPostDate);
            }
        };
    }

    private void loadNextDataFromApi(Date date) {
        loadMorePosts(date);
    }

    private void loadMorePosts(Date createdAt) {
        int POST_LIMIT = 5;
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder("createdAt");
        query.whereLessThan("createdAt", createdAt);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                lastPostDate= allPosts.get(allPosts.size()-1).getCreatedAt();
            }
        });
    }
}