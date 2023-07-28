package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME = 4000;

    private ImageView circle, lines;
    private TextView title;
    private Animation circle_anim, line_anim, text_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        circle = findViewById(R.id.circle);
        lines = findViewById(R.id.lines);
        title = findViewById(R.id.text);

        circle_anim = AnimationUtils.loadAnimation(this, R.anim.circle_animation);
        line_anim = AnimationUtils.loadAnimation(this, R.anim.line_animation);
        text_anim = AnimationUtils.loadAnimation(this, R.anim.up_animation);

        circle.setAnimation(circle_anim);
        lines.setAnimation(line_anim);
        title.setAnimation(text_anim);

        circle.animate().scaleX(0).scaleY(0).setDuration(1000).setStartDelay(3000);
        lines.animate().translationY(-1600).setDuration(1000).setStartDelay(3000);
        title.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, SPLASH_TIME);
    }
}