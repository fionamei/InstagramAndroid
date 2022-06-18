package com.example.instagram;

import android.util.Log;
import android.view.View;

import com.example.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class PostFavoriteListener implements View.OnClickListener {

    private final PostFavoriteCallback callback;
    private Post post;
    private Boolean liked = false;
    private ParseUser user = ParseUser.getCurrentUser();
    public static final String TAG = "PostFavoriteListener";

    public PostFavoriteListener(PostFavoriteCallback callback) {

        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        post = (Post) v.getTag();
        try {
            isLiked();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void isLiked() throws JSONException {
        JSONArray jsonArray = post.getJSONArray(Post.KEY_LIKED_BY);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString(Post.KEY_OBJECT_ID);
                Log.i(TAG, "string of id is" + id );
                if (id.equals(user.getObjectId())) {
                    liked = true;
                }
            }
        } else {
            liked = false;
        }

        save(post);
    }

    public void save(Post post) {
        if (liked) {
            post.unlikePost(user);
            liked = false;
        } else {
            post.likePost(user);
            liked = true;
        }
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (liked) {
                    post.addUnique(Post.KEY_LIKED_BY, user);
                } else {
                    post.removeAll(Post.KEY_LIKED_BY, Arrays.asList(user));
                }
                post.saveInBackground();
                callback.onFavoriteSuccess();
            }
        });
    }



}
