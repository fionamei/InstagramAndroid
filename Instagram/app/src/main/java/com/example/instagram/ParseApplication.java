package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //register parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("doYv0Fg7hlWwmynA99sNC9PIEOeNyp7WdaLA4BhA")
                .clientKey("PKam6yQhJIUXTBiR9rCapfEr80o153VqGx906H5V")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
