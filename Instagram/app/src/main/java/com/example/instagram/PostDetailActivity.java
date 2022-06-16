package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    Post post;
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTimeAgo;
    private ImageView ivProfilePic;
    private TextView tvLikes;
    private ImageButton ibLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));
        initViews();
        populateViews(post);
        likeListener();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimeAgo = findViewById(R.id.tvTimeAgo);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvLikes = findViewById(R.id.tvLikes);
        ibLike = findViewById(R.id.ibLike);
    }

    private void populateViews(Post post) {
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
        tvTimeAgo.setText(post.getTimeAgo());
        ParseFile profilePic = post.getProfilePic();
        if (profilePic != null) {
            Glide.with(this).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
        }
        Resources res = this.getResources();
        String likes = res.getQuantityString(R.plurals.likes, post.getLikes(), post.getLikes());
        tvLikes.setText(likes);
        ibLike.setTag(post);
    }

    private void likeListener() {
        ibLike.setOnClickListener(new PostFavoriteListener(new PostFavoriteCallback() {
            @Override
            public void onFavoriteSuccess() {
                Resources res = PostDetailActivity.this.getResources();
                String likes = res.getQuantityString(R.plurals.likes, post.getLikes(), post.getLikes());
                tvLikes.setText(likes);
            }
        }));
    }
}