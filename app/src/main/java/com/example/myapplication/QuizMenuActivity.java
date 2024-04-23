package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizMenuActivity extends AppCompatActivity
{
    static int record = 0;
    Button   startButton;
    TextView recordText;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz_menu );
        startButton = findViewById( R.id.quiz_menu_start_button );
        recordText  = findViewById( R.id.quiz_menu_record_text_view );
        recordText.setText( "Current record - " + record );
        startButton.setOnClickListener( view ->
        {
            Intent intent = new Intent( QuizMenuActivity.this, QuizActivity.class );
            startActivity( intent );
        } );
    }
}
