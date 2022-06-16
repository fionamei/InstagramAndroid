package com.example.instagram;

import android.view.View;

import com.example.instagram.models.Post;

public class PostFavoriteListener implements View.OnClickListener {

    private final PostFavoriteCallback callback;

    public PostFavoriteListener(PostFavoriteCallback callback) {

        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        Post post = (Post) v.getTag();
        if (post.isLiked()) {
            post.unlikePost();
        } else {
            post.likePost();
        }
        callback.onFavoriteSuccess();

    }



}
