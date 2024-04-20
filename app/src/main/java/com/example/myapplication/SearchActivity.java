package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchActivity extends AppCompatActivity
{
    final FixedSizeStack searchHistory = new FixedSizeStack( 4 );

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        EdgeToEdge.enable( this );
        setContentView( R.layout.activity_search );
        ViewCompat.setOnApplyWindowInsetsListener( findViewById( R.id.main ), ( v, insets ) ->
        {
            Insets systemBars = insets.getInsets( WindowInsetsCompat.Type.systemBars() );
            v.setPadding( systemBars.left, systemBars.top, systemBars.right, systemBars.bottom );
            return insets;
        } );

        SearchView searchView   = findViewById( R.id.searchView );
        Button     searchButton = findViewById( R.id.searchButton );

        // Add query to search history when the user presses the search button
        searchButton.setOnClickListener( v -> searchHistory.push( searchView.getQuery().toString() ) );
    }
}
