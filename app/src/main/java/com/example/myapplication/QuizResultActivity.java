package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity
{
    TextView resultText;
    Button   againButton, backButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz_result );

        resultText  = findViewById( R.id.quiz_result_text_view );
        againButton = findViewById( R.id.quiz_result_try_again_button );
        backButton  = findViewById( R.id.quiz_result_go_back_button );

        if ( QuizActivity.correctCounter > QuizMenuActivity.record )
        {
            QuizMenuActivity.record = QuizActivity.correctCounter;
        }

        String result = "Your score is " + QuizActivity.correctCounter + "!";
        resultText.setText( result );
        againButton.setOnClickListener( view ->
        {
            Intent intent = new Intent( QuizResultActivity.this, QuizActivity.class );
            startActivity( intent );
        } );

        backButton.setOnClickListener( view ->
        {
            Intent intent = new Intent( QuizResultActivity.this, MainActivity.class );
            startActivity( intent );
        } );
    }
}
