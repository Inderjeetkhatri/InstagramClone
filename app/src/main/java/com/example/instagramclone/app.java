package com.example.instagramclone;
import com.parse.Parse;
import android.app.Application;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gmDoZJjhHeITa1adjGBoNWQUNE1RH7VuTWG9cshN")
                .clientKey("5FT2xKutuPhKtip8Z1jODigVruibXIqG6XlkDeZW")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
