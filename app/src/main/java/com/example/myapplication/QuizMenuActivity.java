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

public class QuizMenuActivity extends AppCompatActivity
{
    static int record = 0;
    Button start_button;
    TextView record_text;
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz_menu );
        start_button = findViewById(R.id.quiz_menu_start_button);
        record_text = findViewById(R.id.quiz_menu_record_text_view);
        record_text.setText("Current record - " + Integer.toString(record));
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizMenuActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
