package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTimeAgo;
    private ImageView ivProfpilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));
        initViews();
        populateViews(post);
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTimeAgo = findViewById(R.id.tvTimeAgo);
        ivProfpilePic = findViewById(R.id.ivProfilePic);
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
            Glide.with(this).load(profilePic.getUrl()).circleCrop().into(ivProfpilePic);
        }
    }
}