package com.example.newspoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView tvSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvSplash = findViewById(R.id.tvSplash);
        String text = tvSplash.getText().toString();
        SpannableString span = new SpannableString(text);
        int start = text.indexOf(" Discover");
        int end = start + text.length();
        span.setSpan(new BackgroundColorSpan(Color.parseColor("#F04B37")), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        int start2 = text.indexOf("News");
        int end2 = start2 + "News Point".length();
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#5AD7FF")),start2, end2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvSplash.setText(span);

        Thread td = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        };td.start();
    }
}