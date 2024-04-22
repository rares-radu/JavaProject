package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity
{
    Button                searchButton;
    FragmentContainerView fragmentContainerView;
    SwitchCompat          switchCompat;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );
        searchButton          = findViewById( R.id.searchButton );
        fragmentContainerView = findViewById( R.id.fragment_container_view );
        switchCompat          = findViewById( R.id.search_switch );
        FragmentManager fragmentManager = getSupportFragmentManager();
        switchCompat.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked )
            {
                if ( isChecked )
                {
                    fragmentManager.beginTransaction()
                            .replace( R.id.fragment_container_view, MunicipalityBriefInfoFragment.class, null )
                            .setReorderingAllowed( true )
                            .addToBackStack( "" )
                            .commit();
                    switchCompat.setText( "Brief" );
                }
                else
                {
                    fragmentManager.beginTransaction()
                            .replace( R.id.fragment_container_view, MunicipalityInfoFragment.class, null )
                            .setReorderingAllowed( true )
                            .addToBackStack( "" )
                            .commit();
                    switchCompat.setText( "Detailed" );
                }
            }
        } );
    }

    // I tried to make the graph look as close as possible to the "LineChart (gradient fill)" shown in their examples
    // because I liked it.
    private void drawGraph( Municipality municipality )
    {
        LineChart graph = findViewById( R.id.populationGraph );

        ArrayList< Entry > values = new ArrayList<>();
        for ( Map.Entry< Short, Integer > set : municipality.getPopulationData().entrySet() )
        {
            values.add( new Entry( set.getKey(), set.getValue() ) );
        }

        LineDataSet dataSet = new LineDataSet( values, "Population" );
        dataSet.setAxisDependency( YAxis.AxisDependency.LEFT );
        dataSet.setColor( Color.BLACK );
        dataSet.setLineWidth( 1.0f );
        dataSet.enableDashedLine( 10.0f, 5.0f, 0.0f );
        dataSet.setDrawCircles( true );
        dataSet.setDrawCircleHole( false );
        dataSet.setCircleColor( Color.BLACK );
        dataSet.setCircleRadius( 3.0f );

        // Create a nice red-white gradient for our graph.
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ Color.RED, Color.WHITE }
        );
        dataSet.setFillDrawable( gradient );
        dataSet.setDrawFilled( true );

        // Remove useless information we don't need.
        graph.getDescription().setEnabled( false );
        graph.getAxisLeft().setDrawGridLines( false );
        graph.getXAxis().setDrawGridLines( false );
        graph.getAxisRight().setDrawGridLines( false );
        graph.getLegend().setEnabled( false );

        LineData lineData = new LineData( dataSet );
        graph.setData( lineData );
        graph.invalidate(); // refresh
    }
}
