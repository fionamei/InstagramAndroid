package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("doYv0Fg7hlWwmynA99sNC9PIEOeNyp7WdaLA4BhA")
                .clientKey("PKam6yQhJIUXTBiR9rCapfEr80o153VqGx906H5V")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
