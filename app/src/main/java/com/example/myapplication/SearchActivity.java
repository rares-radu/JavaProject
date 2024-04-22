package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements MunicipalityInfoFragment.OnButtonClickListener, MunicipalityBriefInfoFragment.OnButtonClickListenerBrief
{


    Button                searchButton;
    FragmentContainerView fragmentContainerView;
    SwitchCompat          switchCompat;
    EditText              searchEditText;
    static Municipality          currentValueMunicipality;
    static Weather               currentValueWeather;

    @Override
    public void onButtonClick( Municipality municipality, Weather weather )
    {
        MunicipalityInfoFragment fragment = ( MunicipalityInfoFragment )getSupportFragmentManager().findFragmentById( R.id.fragment_container_view );
        if ( fragment != null && fragment.isAdded() )
        {
            fragment.updateTextView( municipality, weather );
        }
    }
    @Override
    public void onButtonClickBrief( Municipality municipality, Weather weather )
    {
        MunicipalityBriefInfoFragment fragment = ( MunicipalityBriefInfoFragment )getSupportFragmentManager().findFragmentById( R.id.fragment_container_view );
        if ( fragment != null && fragment.isAdded() )
        {
            fragment.updateTextViewBrief( municipality, weather );
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );
        searchEditText        = findViewById( R.id.search_edit_text );
        searchButton          = findViewById( R.id.searchButton );
        fragmentContainerView = findViewById( R.id.fragment_container_view );
        switchCompat          = findViewById( R.id.search_switch );


        searchButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                String prompt = searchEditText.getText().toString();
                MunicipalityRetriever.getMunicipality( prompt, new RetrieverListener< Municipality >()
                {
                    @Override
                    public void onSuccess( Municipality valueMunicipality )
                    {
                        WeatherRetriever.getWeather( prompt, new RetrieverListener< Weather >()
                        {
                            @Override
                            public void onSuccess( Weather valueWeather )
                            {
                                if ( switchCompat.isChecked() )
                                {
                                    // Second fragment is visible
                                    if ( ( valueWeather == null ) || ( valueMunicipality == null ) )
                                    {
                                        fragmentContainerView.setVisibility( View.INVISIBLE );
                                        Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                    }
                                    else
                                    {
                                        currentValueMunicipality = valueMunicipality;
                                        currentValueWeather      = valueWeather;
                                        onButtonClickBrief( valueMunicipality, valueWeather );
                                        fragmentContainerView.setVisibility( View.VISIBLE );
                                    }
                                }
                                else
                                {
                                    // First fragment is visible
                                    if ( ( valueWeather == null ) || ( valueMunicipality == null ) )
                                    {
                                        fragmentContainerView.setVisibility( View.INVISIBLE );
                                        Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                                    }
                                    else
                                    {
                                        currentValueMunicipality = valueMunicipality;
                                        currentValueWeather      = valueWeather;
                                        onButtonClick( valueMunicipality, valueWeather );
                                        fragmentContainerView.setVisibility( View.VISIBLE );
                                    }
                                }
                            }

                            @Override
                            public void onFailure( Exception e )
                            {
                                fragmentContainerView.setVisibility( View.INVISIBLE );
                                Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                            }
                        } );
                    }

                    @Override
                    public void onFailure( Exception e )
                    {
                        fragmentContainerView.setVisibility( View.INVISIBLE );
                        Toast.makeText( SearchActivity.this, "Try another municipality name", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );
        switchCompat.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged( CompoundButton compoundButton, boolean isChecked )
            {
                if (isChecked) {
                    MunicipalityBriefInfoFragment fragment = new MunicipalityBriefInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, fragment, "brief_fragment")
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                    switchCompat.setText("Brief");
                    fragmentContainerView.setVisibility( View.INVISIBLE );
                } else {
                    MunicipalityInfoFragment fragment = new MunicipalityInfoFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, fragment, "info_fragment")
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                    switchCompat.setText("Detailed");
                    fragmentContainerView.setVisibility( View.INVISIBLE );
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
