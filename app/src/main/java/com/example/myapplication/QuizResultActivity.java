package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizResultActivity extends AppCompatActivity {

    TextView result_text;
    Button again_button, back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        result_text = findViewById(R.id.quiz_result_text_view);
        again_button = findViewById(R.id.quiz_result_try_again_button);
        back_button = findViewById(R.id.quiz_result_go_back_button);
        if (QuizActivity.correct_counter > QuizMenuActivity.record){
            QuizMenuActivity.record = QuizActivity.correct_counter;
        }

        String result = "Your score is " + QuizActivity.correct_counter + "!";
        result_text.setText(result);
        again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizResultActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}