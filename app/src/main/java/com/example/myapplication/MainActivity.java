package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Button searchButton = findViewById( R.id.main_button_search );
        searchButton.setOnClickListener( this::switchToSearch );

        Button quizButton = findViewById( R.id.main_button_quiz );
        quizButton.setOnClickListener( this::switchToQuiz );
    }

    private void switchToSearch( View view )
    {
        Intent intent = new Intent( this, SearchActivity.class );
        startActivity( intent );
    }

    private void switchToQuiz( View view )
    {
        Intent intent = new Intent( this, QuizActivity.class );
        startActivity( intent );
    }
}
