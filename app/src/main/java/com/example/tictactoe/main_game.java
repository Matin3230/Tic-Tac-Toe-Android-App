package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class main_game extends AppCompatActivity {

    public String player1, player2, mode;
    public Dialog dialog;
    public Animation scale_down, scale_up;
    public int ifwin = 1;

    CountDownTimer countDownTimer;
    private long mTimeLeftInMillis;
    public int player1_score = 0;
    public int player2_score = 0;

    boolean gameActive = true;

    int activePlayer = 0;
    int[] gameState = {2, 2 , 2, 2, 2, 2, 2, 2, 2};
    //    State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    int[][] winPositions =  {{3,4,5}, {6,7,8},
                            {0,3,6}, {1,4,7}, {2,5,8},
                            {2,4,6}, {0,1,2}, {0,4,8}};

    public void playerTap(View view){
        MediaPlayer button_clicked = MediaPlayer.create(getApplicationContext(), R.raw.button_clicked);
        button_clicked.start();

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        int cnt = 0;

        if(!gameActive) {
            gameReset(view);
        }

        if(gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;

            img.setScaleX(0f);
            img.setScaleY(0f);

            if (activePlayer == 0) {
                img.setImageResource(R.drawable.ic_x);
                activePlayer = 1;
                TextView status = findViewById(R.id.status);
                status.setText(player2 + "'s Turn");
            } else {
                img.setImageResource(R.drawable.ic_o);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);
                status.setText(player1 + "'s Turn");
            }

            img.animate().scaleXBy(1f).scaleYBy(1f).setInterpolator(new OvershootInterpolator()).setDuration(300);
        }

        // To cancel old CountDownTimer
        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        // Check mode for countdown timing
        if(mode.equals("Medium")) {
            mTimeLeftInMillis = 5000;
            startCountdown(5000);
        }else if(mode.equals("Hard")) {
            mTimeLeftInMillis = 3000;
            startCountdown(3000);
        }

        // Check Content of all Images
        for(int i = 0; i < gameState.length; i++) {
            if(gameState[i] != 2) {
                cnt++;
            }
        }

        // Check if any player has won
        for(int[] winPosition: winPositions) {
            if(gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]]!=2){

                // Somebody has won! - Find out who!
                String winnerStr;
                gameActive = false;
                if(gameState[winPosition[0]] == 0) {
                    winnerStr = player1 + " has won";
                    player1_score++;

                    // To cancel old CountDownTimer
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }

                    show_popup("win", player1);
                }
                else{
                    winnerStr = player2 + " has won";

                    player2_score++;

                    // To cancel old CountDownTimer
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }

                    show_popup("win", player2);
                }

                // Update the status bar for winner announcement
                TextView status = findViewById(R.id.status);
                status.setText(winnerStr);

                TextView timer = findViewById(R.id.timer);
                timer.setText("00:00");
                TextView score = findViewById(R.id.score);
                score.setText(player1_score + ":" + player2_score);
                ifwin = 1;
                break;
            } else {
                if(cnt == 9 && gameActive) {
                    ifwin = 0;
                    gameActive = false;
                }
            }
        }

        if(ifwin == 0) {
            show_popup("lose", "");
            ifwin = 1;

            // To cancel old CountDownTimer
            if(countDownTimer != null){
                countDownTimer.cancel();
            }
        }
    }

    // Function to start CountDownTimer for medium and hard mode
    public void startCountdown(long start) {
        countDownTimer = new CountDownTimer(start, 1000) {
            int counter = (int) start / 1000;

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                TextView timer = findViewById(R.id.timer);
                int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timer.setText(timeLeftFormatted);
                counter--;
            }

            @Override
            public void onFinish() {

                if (counter == 0) {
                    TextView status = findViewById(R.id.status);

                    if (activePlayer == 0) {
                        status.setText(player2 + " has won");
                        show_popup("win", player2);
                        player2_score++;
                        TextView score = findViewById(R.id.score);
                        score.setText(player1_score + ":" + player2_score);
                    } else {
                        status.setText(player1 + " has won");
                        show_popup("win", player1);
                        player1_score++;
                        TextView score = findViewById(R.id.score);
                        score.setText(player1_score + ":" + player2_score);
                    }
                    gameActive = false;
                }
            }
        }.start();
    }

    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;

        // To cancel old CountDownTimer
        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        for(int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        ((ImageView)findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);

        TextView status = findViewById(R.id.status);
        status.setText(player1 + "'s Turn - Tap to play");

        dialog.dismiss();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void show_popup(String str, String winner) {
        if(str.equals("win")) {
            dialog.setContentView(R.layout.win_popup);
        } else {
            dialog.setContentView(R.layout.lose_popup);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        Window window = dialog.getWindow();
        window.getAttributes().windowAnimations = R.style.popupAnimation;
        dialog.setCancelable(false);
        dialog.show();

        TextView text = dialog.findViewById(R.id.text);
        if(str.equals("win")) {
            text.setText(winner + " Won");
        } else {
            text.setText("Match Draw");
        }

        Button popup_home = dialog.findViewById(R.id.popup_home);
        Button popup_reset = dialog.findViewById(R.id.popup_reset);

        MediaPlayer button_clicked = MediaPlayer.create(this, R.raw.button_clicked);

        popup_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    popup_home.startAnimation(scale_down);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    popup_home.startAnimation(scale_up);
                    button_clicked.start();
                    popup_home.performClick();
                }

                return true;
            }
        });

        popup_reset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    popup_reset.startAnimation(scale_down);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    popup_reset.startAnimation(scale_up);
                    button_clicked.start();
                    popup_reset.performClick();
                }

                // To cancel old CountDownTimer
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }

                return true;
            }
        });
    }

    public void goto_home(View view) {
        Intent intent = new Intent(main_game.this, homepage.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_game);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            player1 = bundle.getString("player1");
            player2 = bundle.getString("player2");
            mode = bundle.getString("mode");

            TextView p1 = findViewById(R.id.player1);
            TextView p2 = findViewById(R.id.player2);

            p1.setText(player1);
            p2.setText(player2);

            TextView status = findViewById(R.id.status);
            status.setText(player1 + "'s Turn - Tap to play");

            MediaPlayer button_clicked = MediaPlayer.create(this, R.raw.button_clicked);

            Button reset = findViewById(R.id.reset);
            Button home = findViewById(R.id.home);

            dialog = new Dialog(this);

            scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

            home.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        home.startAnimation(scale_down);
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        home.startAnimation(scale_up);
                        button_clicked.start();
                        home.performClick();
                    }

                    return true;
                }
            });

            reset.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        reset.startAnimation(scale_down);
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        reset.startAnimation(scale_up);
                        button_clicked.start();
                        reset.performClick();
                    }

                    // To cancel old CountDownTimer
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }

                    return true;
                }
            });

            // Check mode for countdown timing
            if(mode.equals("Easy")){
                TextView timer = findViewById(R.id.timer);
                timer.setVisibility(View.INVISIBLE);
            }
//            if(mode.equals("Medium")){
//                mTimeLeftInMillis = 5000;
//                startCountdown(5000);
//            }else if(mode.equals("Hard")){
//                mTimeLeftInMillis = 3000;
//                startCountdown(3000);
//            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}