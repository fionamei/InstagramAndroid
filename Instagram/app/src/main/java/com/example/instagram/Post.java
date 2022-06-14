package com.example.instagram;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
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


