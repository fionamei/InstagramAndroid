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

import java.util.Arrays;
import java.util.List;

public class PostFavoriteListener implements View.OnClickListener {

    private final PostFavoriteCallback callback;
    private Post post;
    private Boolean liked;
    private ParseUser user = ParseUser.getCurrentUser();
    public static final String TAG = "PostFavoriteListener";

    public PostFavoriteListener(PostFavoriteCallback callback) {

        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        post = (Post) v.getTag();
        isLiked();
    }

    public void isLiked() {
        ParseRelation<ParseObject> relation = post.getRelation("likedBy");
        ParseQuery<ParseObject> query = relation.getQuery();
        query.whereEqualTo("objectId", user.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
                Log.i(TAG, "done query-ing");
                if (e != null) {
                    Log.e(TAG, "theres an error checking if liked" + e);
                } else {
                    Log.i(TAG, "contents of objects is" + objects);
                    if (objects.isEmpty()) {
                        liked = false;
                    } else {
                        liked = true;
                    }
                    save(post);
                }
           }
        });
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
