package com.example.instagram;

import android.view.View;

import com.example.instagram.models.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PostFavoriteListener implements View.OnClickListener {

    private final PostFavoriteCallback callback;

    public PostFavoriteListener(PostFavoriteCallback callback) {

        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        Post post = (Post) v.getTag();
        save(post);
    }

    public void save(Post post) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.getInBackground(post.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (post.isLiked()) {
                    post.unlikePost();
                } else {
                    post.likePost();
                }
                post.saveInBackground();
                callback.onFavoriteSuccess();
            }
        });
    }



}
