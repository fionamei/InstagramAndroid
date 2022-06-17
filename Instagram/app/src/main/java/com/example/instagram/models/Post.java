package com.example.instagram.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_IS_LIKED = "isLiked";
    public static final String KEY_COMMENTS = "comments";
    public static final String KEY_LIKED_BY = "likedBy";

    public Post() {}

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseFile getProfilePic() {
        ParseUser user = getParseUser(KEY_USER);
        assert user != null;
        return user.getParseFile(KEY_PROFILE_PIC);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public void likePost(ParseUser currentUser) {
        put(KEY_LIKES, getLikes() + 1);

    }

    public void unlikePost(ParseUser currentUser) {
        put(KEY_LIKES, getLikes() - 1);
    }

    public String getTimeAgo() {
        return calculateTimeAgo(getCreatedAt());
    }

    public static String calculateTimeAgo(Date createdAt) {
        long MINUTE_MILLIS = TimeUnit.MINUTES.toMillis(1L);
        long HOUR_MILLIS = TimeUnit.HOURS.toMillis(1L);
        long DAY_MILLIS = TimeUnit.DAYS.toMillis(1L);

        int MINUTE_AGO_MULTIPLIER = 2;
        int MINS_MULTIPLIER = 50;
        double HOUR_AGO_MULTIPLIER = 1.5;
        int HOURS_MULTIPLIER = 24;
        int YESTERDAY_MULTIPLIER = 2;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < MINUTE_AGO_MULTIPLIER * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < MINS_MULTIPLIER * MINUTE_MILLIS) {
                return TimeUnit.MILLISECONDS.toMinutes(diff) + "m";
            } else if (diff < HOUR_AGO_MULTIPLIER * HOUR_MILLIS) {
                return "an hour ago";
            } else if (diff < HOURS_MULTIPLIER * HOUR_MILLIS) {
                return TimeUnit.MILLISECONDS.toHours(diff) + "h";
            } else if (diff < YESTERDAY_MULTIPLIER * DAY_MILLIS) {
                return "yesterday";
            } else {
                return TimeUnit.MILLISECONDS.toDays(diff) + "d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}


