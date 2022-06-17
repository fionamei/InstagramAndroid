package com.example.instagram;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

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
        JSONArray jsonArray = post.getJSONArray("likedBy");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("objectId");
            Log.i(TAG, "string of id is" + id );
            if (id.equals(user.getObjectId())) {
                liked = true;
            }
        }
        save(post);
    }

    public void save(Post post) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        if (liked) {
            post.unlikePost(user);
            liked = false;
            Log.i(TAG, "unliking... liked is " + liked );
        } else {
            post.likePost(user);
            liked = true;
            Log.i(TAG, "liking... liked is " + liked );
        }
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "saving like, adding user in likedBy, liked is " + liked);
                if (liked) {
                    post.addUnique(Post.KEY_LIKED_BY, user);
                } else {
                    post.removeAll(Post.KEY_LIKED_BY, Arrays.asList(user));
                }
                post.saveInBackground();
                callback.onFavoriteSuccess();
            }
        });


//        query.getInBackground(post.getObjectId(), new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (liked) {
//                    post.unlikePost(user);
//                } else {
//                    post.likePost(user);
//                }
//                post.saveInBackground();
//                callback.onFavoriteSuccess();
//            }
//        });
    }



}
