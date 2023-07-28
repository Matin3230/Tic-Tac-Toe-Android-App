package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class form extends AppCompatActivity {
    private String[] game_type = { "Easy", "Medium", "Hard" };
    public TextView player1, player2;
    public Spinner spin;
    public Button submit;
    public Animation scale_down, scale_up;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form);

        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        spin = (Spinner) findViewById(R.id.spinner);
        submit = findViewById(R.id.submit);

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        final MediaPlayer button_clicked = MediaPlayer.create(this, R.raw.button_clicked);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, game_type);
        adapter.setDropDownViewResource(R.layout.dropdown_items);
        spin.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1 = player1.getText().toString().trim();
                String p2 = player2.getText().toString().trim();

                if(!p1.isEmpty() && !p2.isEmpty()) {
                    Intent intent = new Intent(form.this, main_game.class);

                    intent.putExtra("player1", p1);
                    intent.putExtra("player2", p2);
                    intent.putExtra("mode", (CharSequence) spin.getSelectedItem().toString());

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else
                    {
                        if(p1.isEmpty())
                        {
                            p1="Player1";
                        }
                        if(p2.isEmpty())
                        {
                            p2="Player2";
                        }

                        Intent intent = new Intent(form.this, main_game.class);

                        intent.putExtra("player1", p1);
                        intent.putExtra("player2", p2);
                        intent.putExtra("mode", (CharSequence) spin.getSelectedItem().toString());

                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    submit.startAnimation(scale_down);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    submit.startAnimation(scale_up);
                    button_clicked.start();
                    submit.performClick();
                }

                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}