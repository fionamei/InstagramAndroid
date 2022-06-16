package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapters.PostsAdapter;
import com.example.instagram.adapters.ProfilePostsAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView ivProfilePic;
    private TextView tvUsername;
    private ParseUser user;
    private RecyclerView rvPosts;
    public static final int NUM_COLS = 3;
    protected ProfilePostsAdapter adapter;
    protected List<Post> allPosts;
    public static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        user = ParseUser.getCurrentUser();
        populateViews();
        queryPosts();

    }

    private void initViews(View view) {
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUsername = view.findViewById(R.id.tvUsername);
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(getContext(), allPosts);
    }

    private void populateViews() {
        ParseFile profilePic = user.getParseFile("profilePic");
        if (profilePic != null) {
            Glide.with(getContext()).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
        }
        tvUsername.setText(user.getUsername());
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), NUM_COLS));
        rvPosts.setAdapter(adapter);

    }

    private void queryPosts() {
//        int POST_LIMIT = 20;
        // specify type of data - post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit to only 20 items
//        query.setLimit(POST_LIMIT);
        // order by creation date, newest first
        query.whereEqualTo("user", user);
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
            }
        });
    }
}